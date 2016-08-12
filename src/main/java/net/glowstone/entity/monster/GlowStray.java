package net.glowstone.entity.monster;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

public class GlowStray extends GlowMonster implements Skeleton.Stray {
    public GlowStray(Location loc) {
        super(loc, EntityType.STRAY, 20);
    }
}
