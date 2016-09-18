package net.glowstone.entity.ai.task;

import net.glowstone.entity.GlowCreature;
import net.glowstone.entity.GlowLivingEntity;

public class PathfindTargetTask extends PathfindEntityTask {
    public PathfindTargetTask(GlowCreature entity) {
        super(entity, (GlowLivingEntity) entity.getTarget());
    }

    @Override
    public void execute() {
        this.other = (GlowLivingEntity) ((GlowCreature) entity).getTarget();
        super.execute();
    }
}
