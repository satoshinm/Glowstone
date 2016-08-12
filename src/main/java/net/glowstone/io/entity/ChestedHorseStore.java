package net.glowstone.io.entity;

import net.glowstone.entity.GlowAgeable;
import net.glowstone.entity.passive.horse.GlowBaseHorse;
import net.glowstone.entity.passive.horse.GlowChestedHorse;
import net.glowstone.io.nbt.NbtSerialization;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.TagType;
import org.bukkit.entity.EntityType;

public class ChestedHorseStore extends BaseHorseStore {

    private static final String CHESTED_HORSE_KEY = "ChestedHorse";
    private static final String ITEMS_KEY = "Items";

    public ChestedHorseStore(Class<? extends GlowBaseHorse> clazz, EntityType type) {
        super(clazz, type);
    }

    @Override
    public void load(GlowAgeable ageable, CompoundTag compound) {
        super.load(ageable, compound);
        GlowChestedHorse entity = (GlowChestedHorse) ageable;
        entity.setCarryingChest(compound.getBool(CHESTED_HORSE_KEY));
        //Todo: Load items
    }

    @Override
    public void save(GlowAgeable ageable, CompoundTag tag) {
        super.save(ageable, tag);
        GlowChestedHorse entity = (GlowChestedHorse) ageable;
        tag.putBool(CHESTED_HORSE_KEY, entity.isCarryingChest());
        if (entity.isCarryingChest()) {
            tag.putList(ITEMS_KEY, TagType.COMPOUND,
                    NbtSerialization.writeInventory(entity.getInventory().getContents(), entity.getInventory().getContents().length));
        }
    }
}
