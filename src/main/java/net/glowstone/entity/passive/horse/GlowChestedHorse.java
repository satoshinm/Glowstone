package net.glowstone.entity.passive.horse;

import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.BaseHorse;
import org.bukkit.entity.EntityType;

public class GlowChestedHorse extends GlowBaseHorse implements BaseHorse.ChestedHorse {

    private boolean hasChest;

    /**
     * Creates a new chested horse.
     *
     * @param location The location of the horse.
     * @param type     The type of horse.
     * @param owner    The owner of the horse.
     */
    public GlowChestedHorse(Location location, EntityType type, AnimalTamer owner) {
        super(location, type, owner);
    }

    @Override
    public boolean isCarryingChest() {
        return hasChest;
    }

    @Override
    public void setCarryingChest(boolean hasChest) {
        this.hasChest = hasChest;
    }
}
