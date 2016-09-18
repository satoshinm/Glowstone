package net.glowstone.entity.ai.task;

import net.glowstone.entity.GlowCreature;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.message.play.entity.AnimateEntityMessage;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

public class AttackNearTask extends EntityTask {
    public AttackNearTask(GlowCreature entity) {
        super(entity);
    }

    @Override
    public void execute() {
        GlowCreature creature = (GlowCreature) entity;
        if (creature.getTarget() == null) {
            return;
        }
        for (Entity e : entity.getNearbyEntities(1, 2, 1)) {
            if (creature.getTarget().getUniqueId() == e.getUniqueId()) {
                entity.getWorld().getRawPlayers().stream().filter(observer -> observer.canSeeEntity(entity)).forEach(observer -> observer.getSession().send(new AnimateEntityMessage(entity.getEntityId(), 0)));
                ((GlowPlayer) e).damage(2, EntityDamageEvent.DamageCause.ENTITY_ATTACK);
                break;
            }
        }
    }

    @Override
    public int getSpeed() {
        return 15;
    }
}
