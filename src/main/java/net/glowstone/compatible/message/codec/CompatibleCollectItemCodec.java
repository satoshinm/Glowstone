package net.glowstone.compatible.message.codec;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.glowstone.compatible.CompatibleCollectItemMessage;

import java.io.IOException;

public class CompatibleCollectItemCodec implements Codec<CompatibleCollectItemMessage> {
    @Override
    public CompatibleCollectItemMessage decode(ByteBuf buf) throws IOException {
        int id = ByteBufUtils.readVarInt(buf);
        int collector = ByteBufUtils.readVarInt(buf);
        int count = ByteBufUtils.readVarInt(buf);
        return new CompatibleCollectItemMessage(id, collector, count);
    }

    @Override
    public ByteBuf encode(ByteBuf buf, CompatibleCollectItemMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buf, message.getId());
        ByteBufUtils.writeVarInt(buf, message.getCollector());
        ByteBufUtils.writeVarInt(buf, message.getCount());
        return buf;
    }
}
