package net.glowstone.entity.ai.task;

import ca.momoperes.pathfinder.IntVector;
import ca.momoperes.pathfinder.NavigationPath;
import ca.momoperes.pathfinder.PathNode;
import net.glowstone.entity.GlowLivingEntity;
import net.glowstone.entity.ai.AIBoundingBox;
import net.glowstone.entity.physics.BlockBoundingBox;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.Collection;

public abstract class PathfindTask extends EntityTask {

    protected Location target;
    private NavigationPath path;
    private AIBoundingBox box;

    public PathfindTask(GlowLivingEntity entity, Location target) {
        super(entity);
        this.target = target;
        path = createPath();
    }

    @Override
    public void execute() {
        path = createPath();
        if (path == null) {
            return;
        }
        Collection<PathNode> pathNodes = path.calculatePath();
        if (pathNodes == null) {
            return;
        }
        lookAtTarget();
        for (PathNode pathNode : pathNodes) {
            IntVector point = pathNode.getPoint();
            Location location = box.getCorner().clone().add(point.x + 0.5, point.z + 0.25, point.y + 0.5);
            walkTorwards(location);
            break;
        }
    }

    @Override
    public int getSpeed() {
        return -1;
    }

    public void walkTorwards(Location location) {
        Location current = entity.getLocation().clone();
        double deltaX = (location.getX() - current.getX()) / 16;
        double deltaZ = (location.getZ() - current.getZ()) / 16;
        current.add(deltaX, 0, deltaZ);
        if (current.getBlock().getType().isSolid()) {
            current.add(0, 1, 0);
        } else if (!current.getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {
            current.subtract(0, 1, 0);
        }
        entity.teleport(current);
    }

    protected void lookAtTarget() {
        double x = target.getX() - entity.getLocation().getX();
        double z = target.getZ() - entity.getLocation().getZ();
        float yaw = (float) (Math.atan2(z, x) * (180 / Math.PI)) - 90;
        entity.setHeadYaw(yaw);
        Location current = entity.getLocation().clone();
        current.setYaw(yaw);
        entity.teleport(current);
    }

    protected NavigationPath createPath() {
        if (target == null) {
            return null;
        }
        if (entity == null) {
            return null;
        }
        Location current = entity.getLocation().clone();
        if (current.getWorld().getUID() != target.getWorld().getUID()) {
            return null;
        }
        Location center = new Location(current.getWorld(), (current.getX() + target.getX()) / 2, (current.getY() + target.getY()) / 2, (current.getZ() + target.getZ()) / 2);
        box = new AIBoundingBox(center, new Vector(32, 24, 32));
        BlockBoundingBox targetBox = new BlockBoundingBox(target.getBlock());
        if (!box.intersects(targetBox) || !entity.intersects(box)) {
            return null;
        }
        Vector currentRelative = box.relativeTo(current.toVector());
        Vector targetRelative = box.relativeTo(target.toVector());
        IntVector currentVector = new IntVector(currentRelative.getBlockX(), currentRelative.getBlockZ(), currentRelative.getBlockY());
        IntVector targetVector = new IntVector(targetRelative.getBlockX(), targetRelative.getBlockZ(), targetRelative.getBlockY());
        Collection<IntVector> map = box.getMap();
        return new NavigationPath(currentVector, targetVector, map);
    }

    public void setTarget(Location target) {
        this.target = target;
    }
}
