package net.glowstone.entity.ai.task;

import net.glowstone.entity.GlowLivingEntity;

public class PathfindEntityTask extends PathfindTask {
    protected GlowLivingEntity other;

    public PathfindEntityTask(GlowLivingEntity entity, GlowLivingEntity other, double speed) {
        super(entity, other == null ? null : other.getLocation().clone(), speed);
        this.other = other;
    }

    @Override
    public void execute() {
        if (other == null || other.isDead()) {
            return;
        }
        target = other.getLocation().clone();
        super.execute();
    }
}
