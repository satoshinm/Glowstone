package net.glowstone.entity.passive.horse;

import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;

public class GlowMule extends GlowChestedHorse {

    public GlowMule(Location location) {
        this(location, null);
    }

    /**
     * Creates a new mule.
     *
     * @param location The location of the horse.
     * @param owner    The owner of the horse.
     */
    public GlowMule(Location location, AnimalTamer owner) {
        super(location, EntityType.MULE, owner);
    }
}
