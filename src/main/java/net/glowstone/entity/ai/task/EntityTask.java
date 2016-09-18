package net.glowstone.entity.ai.task;

import net.glowstone.entity.GlowLivingEntity;

public abstract class EntityTask {

    private int ticks;
    protected final GlowLivingEntity entity;

    public EntityTask(GlowLivingEntity entity) {
        ticks = getSpeed();
        this.entity = entity;
    }

    public void pulse() {
        if (getSpeed() == -1) {
            execute();
            return;
        }
        ticks--;
        if (ticks == 0) {
            execute();
            ticks = getSpeed();
        }
    }

    public abstract void execute();

    public abstract int getSpeed();

    public int getTicks() {
        return ticks;
    }
}
