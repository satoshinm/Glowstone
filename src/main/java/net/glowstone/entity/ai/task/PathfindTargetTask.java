package net.glowstone.entity.ai.task;

import net.glowstone.entity.GlowCreature;
import net.glowstone.entity.GlowLivingEntity;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PathfindTargetTask extends PathfindEntityTask {
    public PathfindTargetTask(GlowCreature entity, double speed) {
        super(entity, (GlowLivingEntity) entity.getTarget(), speed);
    }

    @Override
    public void execute() {
        if (this.other != null && this.other instanceof Player && ((Player) other).getGameMode() == GameMode.CREATIVE) {
            ((GlowCreature) entity).setTarget(null);
        }
        this.other = (GlowLivingEntity) ((GlowCreature) entity).getTarget();
        super.execute();
    }
}
