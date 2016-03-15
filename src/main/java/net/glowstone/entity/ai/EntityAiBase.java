package net.glowstone.entity.ai;

import net.glowstone.entity.GlowEntity;

import java.util.HashSet;
import java.util.Set;

public class EntityAiBase<E extends GlowEntity> {

    protected E entity;
    private AiTask<E> currentTask;

    private Set<AiTask<E>> avalibleTasks = new HashSet<>();

    public void addTask(AiTask<E> task) {
        avalibleTasks.add(task);
    }

    public void pulse(E entity) {
        if (currentTask != null) {
            currentTask.pulse(entity);

            if (currentTask.isFinished()) {
                currentTask = null;
            }
            return;
        }

        for (AiTask<E> task : avalibleTasks) {
            if (Math.random() < task.getChance()) {
                currentTask = task;
                currentTask.reset(entity);
                return;
            }
        }
    }

}
