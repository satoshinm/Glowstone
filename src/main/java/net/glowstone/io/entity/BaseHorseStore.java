package net.glowstone.io.entity;

import net.glowstone.entity.GlowAgeable;
import net.glowstone.entity.passive.horse.GlowBaseHorse;
import net.glowstone.io.nbt.NbtSerialization;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class BaseHorseStore extends AgeableStore {

    private static final String EATING_HAYSTACK_KEY = "EatingHaystack";
    private static final String TEMPER_KEY = "Temper";
    private static final String TAME_KEY = "Tame";
    private static final String OWNER_UUID_KEY = "OwnerUUID";
    private static final String ARMOR_ITEM_KEY = "ArmorItem";
    private static final String SADDLE_ITEM_KEY = "SaddleItem";

    public BaseHorseStore(Class<? extends GlowBaseHorse> clazz, EntityType type) {
        super(clazz, type.getId());
    }

    @Override
    public void load(GlowAgeable ageable, CompoundTag compound) {
        super.load(ageable, compound);
        GlowBaseHorse entity = (GlowBaseHorse) ageable;
        entity.setEatingHay(compound.getBool(EATING_HAYSTACK_KEY));
        entity.setTemper(compound.getInt(TEMPER_KEY));
        entity.setTamed(compound.getBool(TAME_KEY));
        if (compound.containsKey(OWNER_UUID_KEY)) {
            String uuidKey = compound.getString(OWNER_UUID_KEY);
            if (uuidKey.isEmpty()) {
                entity.setOwnerUUID(null);
            } else {
                entity.setOwnerUUID(UUID.fromString(uuidKey));
            }
        }
        if (compound.containsKey(ARMOR_ITEM_KEY)) {
            entity.getInventory().setArmor(NbtSerialization.readItem(compound.getCompound(ARMOR_ITEM_KEY)));
        }
        if (compound.containsKey(SADDLE_ITEM_KEY)) {
            entity.getInventory().setSaddle(NbtSerialization.readItem(compound.getCompound(SADDLE_ITEM_KEY)));
        }
    }

    @Override
    public void save(GlowAgeable ageable, CompoundTag tag) {
        super.load(ageable, tag);
        GlowBaseHorse entity = (GlowBaseHorse) ageable;
        tag.putBool(EATING_HAYSTACK_KEY, entity.isEatingHay());
        tag.putInt(TEMPER_KEY, entity.getTemper());
        tag.putBool(TAME_KEY, entity.isTamed());
        if (entity.getOwnerUUID() == null) {
            tag.putString(OWNER_UUID_KEY, "");
        } else {
            tag.putString(OWNER_UUID_KEY, entity.getOwnerUUID().toString());
        }
        if (entity.getInventory() != null) {
            if (entity.getInventory().getArmor() != null) {
                tag.putCompound(ARMOR_ITEM_KEY, NbtSerialization.writeItem(entity.getInventory().getArmor(), -1));
            }
            if (entity.getInventory().getSaddle() != null) {
                tag.putCompound(SADDLE_ITEM_KEY, NbtSerialization.writeItem(entity.getInventory().getSaddle(), -1));
            }
        }
    }
}
