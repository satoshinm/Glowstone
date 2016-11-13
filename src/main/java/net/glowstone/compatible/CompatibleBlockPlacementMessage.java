package net.glowstone.compatible;

import com.flowpowered.network.Message;
import lombok.Data;

@Data
public class CompatibleBlockPlacementMessage implements Message {

    private final int x, y, z, direction, hand;
    private final float cursorX, cursorY, cursorZ;

}
