package net.glowstone.net.protocol;

import net.glowstone.compatible.CompatiblePlayProtocol;

/**
 * Enumeration of the different Minecraft protocol states.
 */
public enum ProtocolType {
    HANDSHAKE(new HandshakeProtocol()),
    STATUS(new StatusProtocol()),
    LOGIN(new LoginProtocol()),
    PLAY(new PlayProtocol()),
    PLAY_COMPATIBLE(new CompatiblePlayProtocol());

    private final GlowProtocol protocol;

    ProtocolType(GlowProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * Get a GlowProtocol corresponding to this protocol type.
     *
     * @return A matching GlowProtocol.
     */
    public GlowProtocol getProtocol() {
        return protocol;
    }
}
