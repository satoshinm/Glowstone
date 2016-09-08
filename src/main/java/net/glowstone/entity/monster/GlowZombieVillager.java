package net.glowstone.entity.monster;

import net.glowstone.entity.meta.MetadataIndex;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;

public class GlowZombieVillager extends GlowZombie implements Zombie.ZombieVillager {

    private Villager.Profession profession;

    public GlowZombieVillager(Location loc) {
        super(loc, EntityType.ZOMBIE_VILLAGER);
        setVillagerProfession(Villager.Profession.NORMAL); //Todo: Random
    }

    @Override
    public void setVillagerProfession(Villager.Profession profession) {
        this.profession = profession;
        metadata.set(MetadataIndex.ZOMBIE_VILLAGER_PROFESSION, profession.ordinal());
    }

    @Override
    public Villager.Profession getVillagerProfession() {
        return profession;
    }
}
