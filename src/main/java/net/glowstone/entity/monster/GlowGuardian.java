package net.glowstone.entity.monster;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;

public class GlowGuardian extends GlowMonster implements Guardian {
    public GlowGuardian(Location loc) {
        super(loc, EntityType.GUARDIAN, 30);
    }
}
