package net.glowstone.io.entity;

import net.glowstone.entity.GlowAgeable;
import net.glowstone.entity.passive.horse.GlowHorse;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.entity.BaseHorse;
import org.bukkit.entity.EntityType;

public class HorseStore extends BaseHorseStore {

    private static final String VARIANT_KEY = "Variant";

    public HorseStore() {
        super(GlowHorse.class, EntityType.HORSE);
    }

    @Override
    public void load(GlowAgeable ageable, CompoundTag compound) {
        super.load(ageable, compound);
        GlowHorse entity = (GlowHorse) ageable;
        entity.setStyle(BaseHorse.Horse.Style.values()[compound.getInt(VARIANT_KEY) >>> 8]);
        entity.setColor(BaseHorse.Horse.Color.values()[compound.getInt(VARIANT_KEY) & 0xFF]);
    }

    @Override
    public void save(GlowAgeable ageable, CompoundTag tag) {
        super.save(ageable, tag);
        GlowHorse entity = (GlowHorse) ageable;
        if (entity.getStyle() != null && entity.getColor() != null) {
            tag.putInt(VARIANT_KEY, entity.getStyle().ordinal() << 8 | entity.getColor().ordinal() & 0xFF);
        }
    }
}
