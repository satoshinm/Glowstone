package net.glowstone.net;

import com.flowpowered.network.AsyncableMessage;
import com.flowpowered.network.ConnectionManager;
import com.flowpowered.network.Message;
import com.flowpowered.network.MessageHandler;
import com.flowpowered.network.session.BasicSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.CodecException;
import lombok.Getter;
import net.glowstone.EventFactory;
import net.glowstone.GlowServer;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.io.PlayerDataService.PlayerReader;
import net.glowstone.net.message.KickMessage;
import net.glowstone.net.message.play.entity.DestroyEntitiesMessage;
import net.glowstone.net.message.play.game.UserListItemMessage;
import net.glowstone.net.message.play.game.UserListItemMessage.Action;
import net.glowstone.net.message.play.game.UserListItemMessage.Entry;
import net.glowstone.net.message.play.player.BlockPlacementMessage;
import net.glowstone.net.protocol.PlayProtocol;
import net.glowstone.net.protocol.ProtocolType;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.logging.Level;

/**
 * A single connection to the server, which may or may not be associated with a
 * player.
 *
 * @author Graham Edgecombe
 */
public class GlowSession extends BasicSession {

    /**
     * The server this session belongs to.
     */
    private final GlowServer server;

    /**
     * The connection manager this session belongs to.
     */
    private final ConnectionManager connectionManager;

    /**
     * The Random for this session
     */
    private final Random random = new Random();

    /**
     * A queue of incoming and unprocessed messages.
     */
    private final Queue<Message> messageQueue = new ArrayDeque<>();

    /**
     * The remote address of the connection.
     */
    private InetSocketAddress address;

    /**
     * The state of the connection
     */
    private boolean online;

    /**
     * A message describing under what circumstances the connection ended.
     */
    private String quitReason;

    /**
     * The version used to connect.
     */
    @Getter
    private int version = -1;

    /**
     * The player associated with this session (if there is one).
     */
    private GlowPlayer player;

    /**
     * The ID of the last ping message sent, used to ensure the client responded correctly.
     */
    private int pingMessageId;

    /**
     * Stores the last block placement message sent, see BlockPlacementHandler.
     */
    private BlockPlacementMessage previousPlacement;

    /**
     * The number of ticks until previousPlacement must be cleared.
     */
    private int previousPlacementTicks;

    /**
     * If the connection has been disconnected
     */
    private boolean disconnected;

    /**
     * Creates a new session.
     *
     * @param server  The server this session belongs to.
     * @param channel The channel associated with this session.
     * @param connectionManager The connection manager to manage connections for this session.
     */
    public GlowSession(GlowServer server, Channel channel, ConnectionManager connectionManager) {
        super(channel, ProtocolType.PLAY.getProtocol()); // todo: remove
        this.server = server;
        this.connectionManager = connectionManager;
        address = super.getAddress();
    }

    /**
     * Gets the server associated with this session.
     *
     * @return The server.
     */
    public GlowServer getServer() {
        return server;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Auxiliary state

    public void setVersion(int version) {
        if (this.version != -1) throw new IllegalStateException("Cannot set version twice");
        this.version = version;
    }

    /**
     * Note that the client has responded to a keep-alive.
     *
     * @param pingId The pingId to check for validity.
     */
    public void pong(long pingId) {
        if (pingId == pingMessageId) {
            pingMessageId = 0;
        }
    }

    /**
     * Get the saved previous BlockPlacementMessage for this session.
     *
     * @return The message.
     */
    public BlockPlacementMessage getPreviousPlacement() {
        return previousPlacement;
    }

    /**
     * Set the previous BlockPlacementMessage for this session.
     *
     * @param message The message.
     */
    public void setPreviousPlacement(BlockPlacementMessage message) {
        previousPlacement = message;
        previousPlacementTicks = 2;
    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Player and state management

    /**
     * Get session online state
     *
     * @return true if this session's state is online
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Gets the player associated with this session.
     *
     * @return The player, or {@code null} if no player is associated with it.
     */
    public GlowPlayer getPlayer() {
        return player;
    }

    /**
     * Sets the player associated with this session.
     *
     * @param profile The player's profile with name and UUID information.
     * @throws IllegalStateException if there is already a player associated
     *                               with this session.
     */
    public void setPlayer(PlayerProfile profile) {
        if (player != null) {
            throw new IllegalStateException("Cannot set player twice");
        }

        // isActive check here in case player disconnected during authentication
        if (!isActive()) {
            // no need to call onDisconnect() since it only does anything if there's a player set
            return;
        }

        // initialize the player
        PlayerReader reader = server.getPlayerDataService().beginReadingData(profile.getUniqueId());
        player = new GlowPlayer(this, profile, reader);

        // isActive check here in case player disconnected after authentication,
        // but before the GlowPlayer initialization was completed
        if (!isActive()) {
            reader.close();
            onDisconnect();
            return;
        }

        // Kick other players with the same UUID
        for (GlowPlayer other : getServer().getRawOnlinePlayers()) {
            if (other != player && other.getUniqueId().equals(player.getUniqueId())) {
                other.getSession().disconnect("You logged in from another location.", true);
                break;
            }
        }

        // login event
        PlayerLoginEvent event = EventFactory.onPlayerLogin(player, "");
        if (event.getResult() != Result.ALLOWED) {
            disconnect(event.getKickMessage(), true);
            return;
        }

        //joins the player
        player.join(this, reader);

        player.getWorld().getRawPlayers().add(player);

        online = true;

        GlowServer.logger.info(player.getName() + " [" + address + "] connected, UUID: " + player.getUniqueId());

        // message and user list
        String message = EventFactory.onPlayerJoin(player).getJoinMessage();
        if (message != null && !message.isEmpty()) {
            server.broadcastMessage(message);
        }

        Message addMessage = new UserListItemMessage(Action.ADD_PLAYER, player.getUserListEntry());
        List<Entry> entries = new ArrayList<>();
        for (GlowPlayer other : server.getRawOnlinePlayers()) {
            if (other != player && other.canSee(player)) {
                other.getSession().send(addMessage);
            }
            if (player.canSee(other)) {
                entries.add(other.getUserListEntry());
            }
        }
        send(new UserListItemMessage(Action.ADD_PLAYER, entries));
    }

    @Override
    public ChannelFuture sendWithFuture(Message message) {
        if (!isActive()) {
            // discard messages sent if we're closed, since this happens a lot
            return null;
        }
        return super.sendWithFuture(message);
    }

    @Override
    @Deprecated
    public void disconnect() {
        disconnect("No reason specified.");
    }

    /**
     * Disconnects the session with the specified reason. This causes a
     * KickMessage to be sent. When it has been delivered, the channel
     * is closed.
     *
     * @param reason The reason for disconnection.
     */
    public void disconnect(String reason) {
        disconnect(reason, false);
    }

    /**
     * Disconnects the session with the specified reason. This causes a
     * KickMessage to be sent. When it has been delivered, the channel
     * is closed.
     *
     * @param reason       The reason for disconnection.
     * @param overrideKick Whether to skip the kick event.
     */
    public void disconnect(String reason, boolean overrideKick) {
        if (player != null && !overrideKick) {
            PlayerKickEvent event = EventFactory.onPlayerKick(player, reason);
            if (event.isCancelled()) {
                return;
            }

            reason = event.getReason();

            if (player.isOnline() && event.getLeaveMessage() != null) {
                server.broadcastMessage(event.getLeaveMessage());
            }
        }

        // log that the player was kicked
        if (player != null) {
            GlowServer.logger.info(player.getName() + " kicked: " + reason);
        } else {
            GlowServer.logger.info("[" + address + "] kicked: " + reason);
        }

        if (quitReason == null) {
            quitReason = "kicked";
        }

        // perform the kick, sending a kick message if possible
        if (isActive() && (getProtocol() instanceof PlayProtocol)) {
            // channel is both currently connected and in a protocol state allowing kicks
            sendWithFuture(new KickMessage(reason)).addListener(ChannelFutureListener.CLOSE);
        } else {
            getChannel().close();
        }
    }

    /**
     * Pulse this session, performing any updates needed.
     */
    void pulse() {
        // drop the previous placement if needed
        if (previousPlacementTicks > 0 && --previousPlacementTicks == 0) {
            previousPlacement = null;
        }

        // process messages
        Message message;
        while ((message = messageQueue.poll()) != null) {
            if (disconnected) {
                // disconnected, we are just seeing extra messages now
                break;
            }

            super.messageReceived(message);
        }

        // check if the client is disconnected
        if (disconnected) {
            connectionManager.sessionInactivated(this);

            if (player == null) {
                return;
            }

            player.remove();

            Message userListMessage = UserListItemMessage.removeOne(player.getUniqueId());
            for (GlowPlayer player : server.getRawOnlinePlayers()) {
                if (player.canSee(this.player)) {
                    player.getSession().send(userListMessage);
                } else {
                    player.stopHidingDisconnectedPlayer(this.player);
                }
            }

            GlowServer.logger.info(player.getName() + " [" + address + "] lost connection");

            if (player.isSleeping()) {
                player.leaveBed(false);
            }

            server.getBossBarManager().clearBossBars(player);

            String text = EventFactory.onPlayerQuit(player).getQuitMessage();
            if (online && text != null && !text.isEmpty()) {
                server.broadcastMessage(text);
            }
            // statistics
            player.incrementStatistic(Statistic.LEAVE_GAME);
            for (Player p : server.getOnlinePlayers()) {
                if (p.getUniqueId().equals(player.getUniqueId())) {
                    continue;
                }
                GlowPlayer other = (GlowPlayer) p;
                if (!other.canSee(player)) {
                    continue;
                }
                other.getSession().send(new DestroyEntitiesMessage(Collections.singletonList(player.getEntityId())));
            }
            player = null; // in case we are disposed twice
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Handler overrides

    @Override
    public void onDisconnect() {
        disconnected = true;
    }

    @Override
    public void messageReceived(Message message) {
        if (message instanceof AsyncableMessage && ((AsyncableMessage) message).isAsync()) {
            // async messages get their handlers called immediately
            super.messageReceived(message);
        } else {
            messageQueue.add(message);
        }
    }

    @Override
    public void onInboundThrowable(Throwable t) {
        if (t instanceof CodecException) {
            // generated by the pipeline, not a network error
            GlowServer.logger.log(Level.SEVERE, "Error in network input", t);
        } else {
            // probably a network-level error - consider the client gone
            if (quitReason == null) {
                quitReason = "read error: " + t;
            }
            getChannel().close();
        }
    }

    @Override
    public void onOutboundThrowable(Throwable t) {
        if (t instanceof CodecException) {
            // generated by the pipeline, not a network error
            GlowServer.logger.log(Level.SEVERE, "Error in network output", t);
        } else {
            // probably a network-level error - consider the client gone
            if (quitReason == null) {
                quitReason = "write error: " + t;
            }
            getChannel().close();
        }
    }

    @Override
    public void onHandlerThrowable(Message message, MessageHandler<?, ?> handle, Throwable t) {
        // can be safely logged and the connection maintained
        GlowServer.logger.log(Level.SEVERE, "Error while handling " + message + " (handler: " + handle.getClass().getSimpleName() + ")", t);
    }

    @Override
    public String toString() {
        if (player != null) {
            return player.getName() + "[" + address + "]";
        } else {
            return "[" + address + "]";
        }
    }
}
