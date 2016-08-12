package net.glowstone.entity.monster;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;

public class GlowElderGuardian extends GlowMonster implements Guardian {
    public GlowElderGuardian(Location loc) {
        super(loc, EntityType.ELDER_GUARDIAN, 40);
    }
}
