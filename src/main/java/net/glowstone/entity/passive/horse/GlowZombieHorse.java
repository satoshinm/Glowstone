package net.glowstone.entity.passive.horse;

import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.BaseHorse;
import org.bukkit.entity.EntityType;

public class GlowZombieHorse extends GlowBaseHorse implements BaseHorse.ZombieHorse {

    public GlowZombieHorse(Location location) {
        this(location, null);
    }

    /**
     * Creates a new zombie horse.
     *
     * @param location The location of the horse.
     * @param owner    The owner of the horse.
     */
    public GlowZombieHorse(Location location, AnimalTamer owner) {
        super(location, EntityType.ZOMBIE_HORSE, owner);
    }
}
