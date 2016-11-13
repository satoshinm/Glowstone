package net.glowstone.compatible;

import net.glowstone.GlowServer;
import net.glowstone.entity.GlowEntity;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;

public class EntityTypeAdapter {

    public static int getCompatibleType(GlowEntity entity) {
        if (entity instanceof Guardian) {
            Guardian guardian = (Guardian) entity;
            if (guardian.isElder()) {
                return 4;
            }
        } else if (entity instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) entity;
            GlowServer.logger.info("Skeleton type: " + skeleton.getSkeletonType());
            if (skeleton.getSkeletonType() == Skeleton.SkeletonType.WITHER) {
                return 5;
            } else if (skeleton.getSkeletonType() == Skeleton.SkeletonType.STRAY) {
                return 6;
            }
        } else if (entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;
            if (zombie.getVillagerProfession() == Villager.Profession.HUSK) {
                return 23;
            } else if (zombie.isVillager()) {
                return 27;
            }
        } else if (entity instanceof Horse) {
            Horse horse = (Horse) entity;
            if (horse.getVariant() == Horse.Variant.SKELETON_HORSE) {
                return 28;
            } else if (horse.getVariant() == Horse.Variant.UNDEAD_HORSE) {
                return 29;
            } else if (horse.getVariant() == Horse.Variant.DONKEY) {
                return 31;
            } else if (horse.getVariant() == Horse.Variant.MULE) {
                return 32;
            }
            return 100;
        }
        return entity.getType().getTypeId();
    }

    public static List<MetadataMap.Entry> adaptMetadataEntries(List<MetadataMap.Entry> entries) {
        List<MetadataMap.Entry> newEntries = new ArrayList<>();
        for (MetadataMap.Entry e : entries) {
            MetadataMap.Entry entry = e.clone();
            MetadataIndex meta = entry.getIndex();
            Class<? extends Entity> type = meta.getAppliesTo();
            int index = meta.getIndex();
            if (type == Skeleton.class && index == 13) {
                entry.setIndex(MetadataIndex.COMPATIBLE_SKELETON_HANDS_RISEN_UP);
                newEntries.add(entry);
                break;
            } else if (type == Zombie.class) {
                if (index == 12) {
                    newEntries.add(entry);
                    continue;
                }
                if (index == 13) {
                    newEntries.add(entry);
                    if ((int) entry.getValue() > 0 && (int) entry.getValue() < 6) {
                        newEntries.add(new MetadataMap.Entry(MetadataIndex.COMPATIBLE_ZOMBIE_VILLAGER_PROFESSION, (int) (entry.getValue()) - 1));
                    }
                    continue;
                }
                if (index == 14) {
                    entry.setIndex(MetadataIndex.COMPATIBLE_ZOMBIE_VILLAGER_CONVERTING);
                    newEntries.add(entry);
                    continue;
                }
                if (index == 15) {
                    entry.setIndex(MetadataIndex.COMPATIBLE_ZOMBIE_HANDS_RISED_UP);
                    newEntries.add(entry);
                }
            } else if (type == Guardian.class) {
                if (index == 12) {
                    entry.setIndex(MetadataIndex.COMPATIBLE_GUARDIAN_SPIKES);
                    boolean b = (((Byte) entry.getValue()).intValue() & 0x04) != 0;
                    entry.setValue(b);
                    newEntries.add(entry);
                }
            } else {
                newEntries.add(entry);
            }
        }
        return newEntries;
    }
}
