package net.glowstone.entity.ai.task;

import net.glowstone.entity.GlowCreature;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class FindTargetTask extends EntityTask {
    public FindTargetTask(GlowCreature entity) {
        super(entity);
    }

    @Override
    public void execute() {
        GlowCreature creature = (GlowCreature) entity;
        for (Entity e : creature.getNearbyEntities(8, 8, 8)) {
            if (e instanceof Player) {
                if (((Player) e).getGameMode() != GameMode.CREATIVE) {
                    creature.setTarget((LivingEntity) e);
                }
            }
        }
    }

    @Override
    public int getSpeed() {
        return 5;
    }
}
