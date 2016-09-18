package net.glowstone.entity.ai.task;

import net.glowstone.entity.GlowPlayer;
import net.glowstone.entity.monster.GlowEnderman;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class StareAgressiveTask extends EntityTask {
    public StareAgressiveTask(GlowEnderman entity) {
        super(entity);
    }

    @Override
    public void execute() {
        boolean needsAggressive = false;
        Player target = null;
        for (Entity e : entity.getNearbyEntities(16, 16, 16)) {
            if (e instanceof Player) {
                GlowPlayer player = (GlowPlayer) e;
                if (player.getGameMode() == GameMode.CREATIVE || !player.canSeeEntity(entity)) {
                    continue;
                }
                Vector playerDirection = player.getLocation().getDirection().normalize();
                Vector lookVector = new Vector(entity.getLocation().getX() - player.getLocation().getX(), entity.getEyeLocation().getY() - player.getEyeLocation().getY(), entity.getLocation().getZ() - player.getLocation().getZ());
                double length = lookVector.length();
                double dot = playerDirection.dot(lookVector.normalize());
                if (dot > 1 - (0.025 / length)) {
                    needsAggressive = true;
                    target = player;
                    break;
                }
            }
        }
        if (!((GlowEnderman) entity).isScreaming()) {
            ((GlowEnderman) entity).setScreaming(true);
        }
        ((GlowEnderman) entity).setTarget(needsAggressive ? target : ((GlowEnderman) entity).getTarget());
    }

    @Override
    public int getSpeed() {
        return 15;
    }
}
