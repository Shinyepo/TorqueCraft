package dev.shinyepo.torquecraft.utils;

import dev.shinyepo.torquecraft.block.pipes.FluidPipe;
import dev.shinyepo.torquecraft.helpers.PipeConnection;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public abstract class PipeUtil {
    public static EnumProperty<PipeConnection> getProp(Direction facing) {
        switch (facing) {
            case Direction.NORTH -> {
                return FluidPipe.NORTH;
            }
            case Direction.SOUTH -> {
                return FluidPipe.SOUTH;
            }
            case Direction.WEST -> {
                return FluidPipe.WEST;
            }
            case Direction.EAST -> {
                return FluidPipe.EAST;
            }
            case Direction.DOWN -> {
                return FluidPipe.DOWN;
            }
            case Direction.UP -> {
                return FluidPipe.UP;
            }
        }
        return null;
    }
}
