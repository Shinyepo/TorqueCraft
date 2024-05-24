package dev.shinyepo.torquecraft.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class DirectionUtils {

    public static Vec3 getBlockHOffsetCoords(Vec3 center, Direction facing, double offX, double offY, double offZ) {
        double newX = center.x;
        double newY = center.y + offY;
        double newZ = center.z;

        switch (facing) {
            case Direction.NORTH:
                newX = center.x + offX;
                newZ = center.z + offZ;
                break;
            case Direction.SOUTH:
                newX = center.x - offX;
                newZ = center.z - offZ;
                break;
            case Direction.WEST:
                newX = center.x + offZ;
                newZ = center.z - offX;
                break;
            case Direction.EAST:
                newX = center.x - offZ;
                newZ = center.z + offX;
                break;
        }

        return new Vec3(newX, newY, newZ);
    }
}
