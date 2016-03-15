package net.glowstone.entity.ai;

import net.glowstone.entity.GlowEntity;

public interface AiTask<E extends GlowEntity> {

    void reset(E entity);

    void pulse(E entity);

    boolean isFinished();

    double getChance();
}
