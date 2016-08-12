package net.glowstone.entity.passive.horse;

import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.BaseHorse;
import org.bukkit.entity.EntityType;

import java.util.Random;

public class GlowHorse extends GlowBaseHorse implements BaseHorse.Horse {

    private Color horseColor = Color.values()[new Random().nextInt(6)];
    private Style horseStyle = Style.values()[new Random().nextInt(3)];

    public GlowHorse(Location location) {
        this(location, null);
    }

    /**
     * Creates a new horse.
     *
     * @param location The location of the horse.
     * @param owner    The owner of the horse.
     */
    public GlowHorse(Location location, AnimalTamer owner) {
        super(location, EntityType.HORSE, owner);
    }

    @Override
    protected MetadataMap getMetadataMap() {
        MetadataMap map = new MetadataMap(getClass());
        map.set(MetadataIndex.BASE_HORSE_FLAGS, getHorseFlags());
        if (getOwnerUUID() != null) {
            map.set(MetadataIndex.BASE_HORSE_OWNER, getOwnerUUID());
        }
        map.set(MetadataIndex.HORSE_STYLE, getHorseStyleData());
        map.set(MetadataIndex.HORSE_ARMOR, getHorseArmorData());
        return map;
    }

    @Override
    public Color getColor() {
        return horseColor;
    }

    @Override
    public void setColor(Color color) {
        this.horseColor = color;
    }

    @Override
    public Style getStyle() {
        return horseStyle;
    }

    @Override
    public void setStyle(Style style) {
        this.horseStyle = style;
    }

    private int getHorseStyleData() {
        return horseColor.ordinal() & 0xFF | horseStyle.ordinal() << 8;
    }

    private int getHorseArmorData() {
        if (getInventory().getArmor() != null) {
            if (getInventory().getArmor().getType() == Material.DIAMOND_BARDING) {
                return 3;
            } else if (getInventory().getArmor().getType() == Material.GOLD_BARDING) {
                return 2;
            } else if (getInventory().getArmor().getType() == Material.IRON_BARDING) {
                return 1;
            }
        }
        return 0;
    }
}
