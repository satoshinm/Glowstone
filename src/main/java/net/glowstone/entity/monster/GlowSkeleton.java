package net.glowstone.entity.monster;

import net.glowstone.entity.ai.EntityAiBase;
import net.glowstone.entity.ai.basic.MoveAiTask;
import net.glowstone.entity.ai.basic.RotateAiTask;
import net.glowstone.entity.meta.MetadataIndex;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

public class GlowSkeleton extends GlowMonster<GlowSkeleton> implements Skeleton {
    private SkeletonType skeletonType = SkeletonType.NORMAL;

    public GlowSkeleton(Location loc) {
        super(loc, EntityType.SKELETON, 20);
        aiBase = new EntityAiBase<>();
        aiBase.addTask(new MoveAiTask<>());
        aiBase.addTask(new RotateAiTask<>());
    }

    @Override
    public SkeletonType getSkeletonType() {
        return skeletonType;
    }

    @Override
    public void setSkeletonType(SkeletonType type) {
        skeletonType = type;
        metadata.set(MetadataIndex.SKELETON_TYPE, skeletonType.getId());
    }
}
