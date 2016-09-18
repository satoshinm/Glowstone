package net.glowstone.entity.ai.task;

import net.glowstone.entity.GlowCreature;
import net.glowstone.entity.GlowLivingEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class LookAtNearbyTask extends EntityTask {

    public LookAtNearbyTask(GlowLivingEntity entity) {
        super(entity);
    }

    @Override
    public void execute() {
        Entity who = null;
        if (entity instanceof GlowCreature && (((GlowCreature) entity).getTarget() != null)) {
            who = ((GlowCreature) entity).getTarget();
        } else {
            for (Entity e : entity.getNearbyEntities(8, 8, 8)) {
                if (e instanceof Player) {
                    who = e;
                    break;
                }
            }
        }
        if (who != null) {
            Location other = who.getLocation();
            double x = other.getX() - entity.getLocation().getX();
            double z = other.getZ() - entity.getLocation().getZ();
            float yaw = (float) (Math.atan2(z, x) * (180 / Math.PI)) - 90;
            entity.setHeadYaw(yaw);
        }
    }

    @Override
    public int getSpeed() {
        return 2;
    }
}
