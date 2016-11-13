package net.glowstone.compatible.message.codec;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.game.TitleMessage;
import net.glowstone.util.TextMessage;

import java.io.IOException;

public class CompatibleTitleCodec implements Codec<TitleMessage> {
    @Override
    public TitleMessage decode(ByteBuf buffer) throws IOException {
        int actionId = ByteBufUtils.readVarInt(buffer);
        TitleMessage.Action action = TitleMessage.Action.getAction(actionId);
        switch (actionId) {
            case 0:
            case 1:
                String text = ByteBufUtils.readUTF8(buffer);
                return new TitleMessage(action, TextMessage.decode(text));
            case 3:
                int fadeIn = buffer.readInt();
                int stay = buffer.readInt();
                int fadeOut = buffer.readInt();
                return new TitleMessage(action, fadeIn, stay, fadeOut);
            default:
                return new TitleMessage(action);
        }
    }

    @Override
    public ByteBuf encode(ByteBuf buf, TitleMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buf, message.getAction().ordinal());
        switch (message.getAction().ordinal()) {
            case 0:
            case 1:
                ByteBufUtils.writeUTF8(buf, message.getText().encode());
                break;
            case 3:
                buf.writeInt(message.getFadeIn());
                buf.writeInt(message.getStay());
                buf.writeInt(message.getFadeOut());
                break;
        }
        return buf;
    }
}
