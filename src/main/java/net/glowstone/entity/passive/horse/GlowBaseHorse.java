package net.glowstone.entity.passive.horse;

import com.flowpowered.network.Message;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.entity.passive.GlowTameable;
import net.glowstone.inventory.GlowHorseInventory;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.BaseHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.HorseInventory;

import java.util.List;
import java.util.UUID;

public class GlowBaseHorse extends GlowTameable implements BaseHorse {

    private int domestication;
    private int maxDomestication;
    private double jumpStrength;
    private boolean eatingHay;
    private boolean hasReproduced;
    private int temper;
    private UUID ownerUUID;
    private HorseInventory inventory = new GlowHorseInventory(this);

    /**
     * Creates a new ageable animal.
     *
     * @param location  The location of the horse.
     * @param type      The type of horse.
     * @param owner     The owner of the horse.
     */
    public GlowBaseHorse(Location location, EntityType type, AnimalTamer owner) {
        super(location, type, 15, owner);
        if (owner != null) {
            setOwnerUUID(owner.getUniqueId());
        }
    }

    protected MetadataMap getMetadataMap() {
        MetadataMap map = new MetadataMap(GlowBaseHorse.class);
        map.set(MetadataIndex.BASE_HORSE_FLAGS, getHorseFlags());
        return map;
    }

    @Override
    public List<Message> createSpawnMessage() {
        List<Message> messages = super.createSpawnMessage();
        MetadataMap map = getMetadataMap();
        map.set(MetadataIndex.BASE_HORSE_FLAGS, getHorseFlags());
        if (getOwnerUUID() != null) {
            map.set(MetadataIndex.BASE_HORSE_OWNER, getOwnerUUID());
        }
        messages.add(new EntityMetadataMessage(id, map.getEntryList()));
        return messages;
    }

    int getHorseFlags() {
        int value = 0;
        if (isTamed()) {
            value |= 0x02;
        }

        if (this instanceof ChestedHorse) {
            ChestedHorse chested = (ChestedHorse) this;
            if (chested.getInventory() != null && chested.getInventory().getSaddle() != null) {
                value |= 0x04;
            }
            if (chested.isCarryingChest()) {
                value |= 0x08;
            }
        }
        if (hasReproduced) {
            value |= 0x10;
        }
        if (eatingHay) {
            value |= 0x20;
        }
        return value;
    }

    @Override
    public int getDomestication() {
        return domestication;
    }

    @Override
    public void setDomestication(int domestication) {
        this.domestication = domestication;
    }

    @Override
    public int getMaxDomestication() {
        return maxDomestication;
    }

    @Override
    public void setMaxDomestication(int maxDomestication) {
        this.maxDomestication = maxDomestication;
    }

    @Override
    public double getJumpStrength() {
        return jumpStrength;
    }

    @Override
    public void setJumpStrength(double jumpStrength) {
        this.jumpStrength = jumpStrength;
    }

    public int getTemper() {
        return temper;
    }

    public void setTemper(int temper) {
        this.temper = temper;
    }

    @Override
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    @Override
    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public boolean isEatingHay() {
        return eatingHay;
    }

    public void setEatingHay(boolean eatingHay) {
        this.eatingHay = eatingHay;
    }

    public boolean hasReproduced() {
        return hasReproduced;
    }

    public void setHasReproduced(boolean hasReproduced) {
        this.hasReproduced = hasReproduced;
    }

    @Override
    public HorseInventory getInventory() {
        return inventory;
    }
}
