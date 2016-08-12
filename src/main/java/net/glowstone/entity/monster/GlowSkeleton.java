package net.glowstone.entity.monster;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

public class GlowSkeleton extends GlowMonster implements Skeleton {
    public GlowSkeleton(Location loc) {
        super(loc, EntityType.SKELETON, 20);
    }
}
