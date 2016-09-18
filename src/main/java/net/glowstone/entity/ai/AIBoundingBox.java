package net.glowstone.entity.ai;

import ca.momoperes.pathfinder.IntVector;
import net.glowstone.entity.physics.BoundingBox;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

public class AIBoundingBox extends BoundingBox {

    private final Location start;
    private final Vector size;

    public AIBoundingBox(Location start, Vector size) {
        this.start = start;
        this.size = size;
        minCorner.setX(start.getX() - getSize().getX() / 2);
        minCorner.setY(start.getY() - getSize().getY() / 2);
        minCorner.setZ(start.getZ() - getSize().getZ() / 2);
        maxCorner.setX(start.getX() + getSize().getX() / 2);
        maxCorner.setY(start.getY() + getSize().getY() / 2);
        maxCorner.setZ(start.getZ() + getSize().getZ() / 2);
    }

    @Override
    public Vector getSize() {
        return size;
    }

    public Vector relativeTo(Vector other) {
        int x = other.getBlockX() - minCorner.getBlockX();
        int y = other.getBlockY() - minCorner.getBlockY();
        int z = other.getBlockZ() - minCorner.getBlockZ();
        return new Vector(x, y, z);
    }

    public Location getCorner() {
        return new Location(start.getWorld(), minCorner.getBlockX(), minCorner.getBlockY(), minCorner.getBlockZ());
    }

    public Collection<IntVector> getMap() {
        Collection<IntVector> map = new ArrayList<>();
        Location corner = getCorner();
        for (int x = 0; x < getSize().getBlockX(); x++) {
            for (int z = 0; z < getSize().getBlockZ(); z++) {
                int y = corner.getWorld().getHighestBlockYAt(corner.getBlockX() + x, corner.getBlockZ() + z);
                map.add(new IntVector(x, z, y - start.getBlockY() + getSize().getBlockY() / 2));
            }
        }
        return map;
    }
}
