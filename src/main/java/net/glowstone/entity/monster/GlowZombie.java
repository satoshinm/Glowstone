package net.glowstone.entity.monster;

import net.glowstone.entity.meta.MetadataIndex;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

public class GlowZombie extends GlowMonster implements Zombie {

    private boolean canBreakDoors;

    public GlowZombie(Location loc) {
        this(loc, EntityType.ZOMBIE);
    }

    public GlowZombie(Location loc, EntityType type) {
        super(loc, type, 20);
    }

    @Override
    public boolean isBaby() {
        return metadata.getBoolean(MetadataIndex.ZOMBIE_IS_CHILD);
    }

    @Override
    public void setBaby(boolean value) {
        metadata.set(MetadataIndex.ZOMBIE_IS_CHILD, value);
    }

    public boolean isCanBreakDoors() {
        return canBreakDoors;
    }

    public void setCanBreakDoors(boolean canBreakDoors) {
        this.canBreakDoors = canBreakDoors;
    }
}
