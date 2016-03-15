package net.glowstone.entity.ai.basic;

import net.glowstone.entity.GlowCreature;
import net.glowstone.entity.ai.AiTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class MoveAiTask<E extends GlowCreature> implements AiTask<E> {

    @Override
    public void reset(E entity) { }

    @Override
    public void pulse(E entity) {
        Location location = entity.getLocation();
        Vector vector = location.getDirection().normalize().multiply(Math.random() + 0.2);

        location.add(vector);
        int id = entity.getWorld().getBlockTypeIdAt(location);
        Material mat = Material.getMaterial(id);
        if (mat.isSolid()) { // todo material check, bounding box
            location.add(0, 1, 0);
            int id2 = entity.getWorld().getBlockTypeIdAt(location);
            Material mat2 = Material.getMaterial(id2);
            if (mat2.isSolid()) { // todo material check, bounding box
                // can't move, so rotate a little ;)
                location = entity.getLocation();
                location.setYaw((float) ((location.getYaw() + (Math.random() * 150) - 75) % 360));
                entity.setRawLocation(location);
                return;
            }
        }

        entity.setRawLocation(location);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public double getChance() {
        return 0.02;
    }
}
