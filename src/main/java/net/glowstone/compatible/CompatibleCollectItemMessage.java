package net.glowstone.compatible;

import com.flowpowered.network.Message;
import lombok.Data;

@Data
public class CompatibleCollectItemMessage implements Message {

    private final int id, collector, count;

}
