package net.glowstone.entity.passive.horse;

import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.BaseHorse;
import org.bukkit.entity.EntityType;

public class GlowSkeletonHorse extends GlowBaseHorse implements BaseHorse.SkeletonHorse {

    public GlowSkeletonHorse(Location location) {
        this(location, null);
    }

    /**
     * Creates a new skeleton horse.
     *
     * @param location The location of the horse.
     * @param owner    The owner of the horse.
     */
    public GlowSkeletonHorse(Location location, AnimalTamer owner) {
        super(location, EntityType.SKELETON_HORSE, owner);
    }
}
