package net.glowstone.entity.monster;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

public class GlowWitherSkeleton extends GlowMonster implements Skeleton.WitherSkeleton {
    public GlowWitherSkeleton(Location loc) {
        super(loc, EntityType.WITHER_SKELETON, 20);
    }
}
