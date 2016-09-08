package net.glowstone.io.entity;

import net.glowstone.entity.monster.GlowZombieVillager;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

class ZombieVillagerStore<T extends GlowZombieVillager> extends MonsterStore<GlowZombieVillager> {
    public ZombieVillagerStore() {
        super(GlowZombieVillager.class, EntityType.ZOMBIE_VILLAGER.getId());
    }

    public ZombieVillagerStore(Class<GlowZombieVillager> clazz, String id) {
        super(clazz, id);
    }

    @Override
    public void load(GlowZombieVillager entity, CompoundTag tag) {
        super.load(entity, tag);
        if (tag.isInt("Profession")) {
            entity.setVillagerProfession(Villager.Profession.values()[tag.getInt("Profession")]);
        }
    }

    @Override
    public void save(GlowZombieVillager entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putInt("Profession", entity.getVillagerProfession().ordinal());
    }
}
