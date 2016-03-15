package net.glowstone.entity.ai.basic;

import net.glowstone.entity.GlowCreature;
import net.glowstone.entity.ai.AiTask;
import org.bukkit.Location;

public class RotateAiTask<E extends GlowCreature> implements AiTask<E> {

    @Override
    public void reset(E entity) { }

    @Override
    public void pulse(E entity) {
        Location location = entity.getLocation();
        location.setYaw((float) ((location.getYaw() + (Math.random() * 150) - 75) % 360));
        entity.setRawLocation(location);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public double getChance() {
        return 0.021;
    }
}
