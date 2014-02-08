package net.glowstone.entity.meta;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * A map for entity metadata.
 */
public class MetadataMap {

    private final Map<MetadataIndex, Object> map = new EnumMap<MetadataIndex, Object>(MetadataIndex.class);
    private final Class<? extends Entity> entityClass;

    public MetadataMap(Class<? extends Entity> entityClass) {
        this.entityClass = entityClass;
    }

    public boolean containsKey(MetadataIndex index) {
        return map.containsKey(index);
    }

    public void set(MetadataIndex index, Object value) {
        if (!index.getType().getDataType().isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("Cannot assign " + value + " to " + index + ", expects " + index.getType());
        }

        if (!index.appliesTo(entityClass)) {
            throw new IllegalArgumentException("Index " + index + " does not apply to " + entityClass.getSimpleName() + ", only " + index.getAppliesTo().getSimpleName());
        }

        map.put(index, value);
    }

    public Object get(MetadataIndex index) {
        return map.get(index);
    }

    public boolean getBit(MetadataIndex index, int bit) {
        return (((Number) get(index)).intValue() & bit) != 0;
    }

    public void setBit(MetadataIndex index, int bit) {
        switch (index.getType()) {
            case BYTE:
                set(index, getByte(index) | bit);
                break;
            case INT:
                set(index, getInt(index) | bit);
                break;
        }
    }

    public void clearBit(MetadataIndex index, int bit) {
        switch (index.getType()) {
            case BYTE:
                set(index, getByte(index) & ~bit);
                break;
            case INT:
                set(index, getInt(index) & ~bit);
                break;
        }
    }

    public byte getByte(MetadataIndex index) {
        return _get(index, MetadataType.BYTE, (byte) 0);
    }

    public short getShort(MetadataIndex index) {
        return _get(index, MetadataType.SHORT, (short) 0);
    }

    public int getInt(MetadataIndex index) {
        return _get(index, MetadataType.INT, 0);
    }

    public float getFloat(MetadataIndex index) {
        return _get(index, MetadataType.FLOAT, 0f);
    }

    public String getString(MetadataIndex index) {
        return _get(index, MetadataType.STRING, null);
    }

    public ItemStack getItem(MetadataIndex index) {
        return _get(index, MetadataType.ITEM, null);
    }

    @SuppressWarnings("unchecked")
    private <T> T _get(MetadataIndex index, MetadataType expected, T def) {
        if (index.getType() != expected) {
            throw new IllegalArgumentException("Cannot get " + index + ": is " + index.getType() + ", not " + expected);
        }
        T t = (T) map.get(index);
        if (t == null) {
            return def;
        }
        return t;
    }

    public List<Entry> getEntryList() {
        List<Entry> result = new ArrayList<Entry>(map.size());
        for (Map.Entry<MetadataIndex, Object> entry : map.entrySet()) {
            result.add(new Entry(entry.getKey(), entry.getValue()));
        }
        Collections.sort(result);
        return result;
    }

    @Override
    public String toString() {
        return "MetadataMap{" +
                "map=" + map +
                ", entityClass=" + entityClass +
                '}';
    }

    public static class Entry implements Comparable<Entry> {
        public final MetadataIndex index;
        public final Object value;

        public Entry(MetadataIndex index, Object value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public int compareTo(Entry o) {
            return o.index.getIndex() - index.getIndex();
        }
    }
}
