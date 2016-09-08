package net.glowstone.entity.monster;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

public class GlowHusk extends GlowZombie implements Zombie.Husk {
    public GlowHusk(Location loc) {
        super(loc, EntityType.HUSK);
    }
}
