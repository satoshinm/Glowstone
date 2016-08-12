package net.glowstone.entity.passive.horse;

import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;

public class GlowDonkey extends GlowChestedHorse {

    public GlowDonkey(Location location) {
        this(location, null);
    }

    /**
     * Creates a new donkey.
     *
     * @param location The location of the horse.
     * @param owner    The owner of the horse.
     */
    public GlowDonkey(Location location, AnimalTamer owner) {
        super(location, EntityType.DONKEY, owner);
    }

}
