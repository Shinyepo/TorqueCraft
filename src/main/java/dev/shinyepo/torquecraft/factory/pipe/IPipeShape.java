package dev.shinyepo.torquecraft.factory.pipe;

import dev.shinyepo.torquecraft.model.baker.helpers.PipeConnection;
import dev.shinyepo.torquecraft.utils.PipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;

import javax.annotation.Nonnull;

public interface IPipeShape  {
    EnumProperty<PipeConnection> NORTH = EnumProperty.create("north", PipeConnection.class);
    EnumProperty<PipeConnection> SOUTH = EnumProperty.create("south", PipeConnection.class);
    EnumProperty<PipeConnection> WEST = EnumProperty.create("west", PipeConnection.class);
    EnumProperty<PipeConnection> EAST = EnumProperty.create("east", PipeConnection.class);
    EnumProperty<PipeConnection> UP = EnumProperty.create("up", PipeConnection.class);
    EnumProperty<PipeConnection> DOWN = EnumProperty.create("down", PipeConnection.class);

    VoxelShape SHAPE_CABLE_NORTH = Shapes.box(.3, .3, 0, .7, .7, .5);
    VoxelShape SHAPE_CABLE_SOUTH = Shapes.box(.3, .3, .5, .7, .7, 1);
    VoxelShape SHAPE_CABLE_WEST = Shapes.box(0, .3, .3, .5, .7, .7);
    VoxelShape SHAPE_CABLE_EAST = Shapes.box(.5, .3, .3, 1, .7, .7);
    VoxelShape SHAPE_CABLE_UP = Shapes.box(.3, .5, .3, .7, 1, .7);
    VoxelShape SHAPE_CABLE_DOWN = Shapes.box(.3, 0, .3, .7, .5, .7);

    VoxelShape SHAPE_BLOCK_NORTH = Shapes.box(.3, .3, 0, .7, .7, .1);
    VoxelShape SHAPE_BLOCK_SOUTH = Shapes.box(.3, .3, .9, .7, .7, 1);
    VoxelShape SHAPE_BLOCK_WEST = Shapes.box(0, .3, .3, .1, .7, .7);
    VoxelShape SHAPE_BLOCK_EAST = Shapes.box(.9, .3, .3, 1, .7, .7);
    VoxelShape SHAPE_BLOCK_UP = Shapes.box(.3, .9, .3, .7, 1, .7);
    VoxelShape SHAPE_BLOCK_DOWN = Shapes.box(.3, 0, .3, .7, .1, .7);

    default int calculateShapeIndex(PipeConnection north, PipeConnection south, PipeConnection west, PipeConnection east, PipeConnection up, PipeConnection down) {
        int l = PipeConnection.values().length;
        return ((((south.ordinal() * l + north.ordinal()) * l + west.ordinal()) * l + east.ordinal()) * l + up.ordinal()) * l + down.ordinal();
    }

    default VoxelShape[] makeShapes() {
            int length = PipeConnection.values().length;
        VoxelShape[] shapeCache = new VoxelShape[length * length * length * length * length * length];

            for (PipeConnection up : PipeConnection.VALUES) {
                for (PipeConnection down : PipeConnection.VALUES) {
                    for (PipeConnection north : PipeConnection.VALUES) {
                        for (PipeConnection south : PipeConnection.VALUES) {
                            for (PipeConnection east : PipeConnection.VALUES) {
                                for (PipeConnection west : PipeConnection.VALUES) {
                                    int idx = calculateShapeIndex(north, south, west, east, up, down);
                                    shapeCache[idx] = makeShape(north, south, west, east, up, down);
                                }
                            }
                        }
                    }
                }
        }
        return shapeCache;
    }

    default VoxelShape makeShape(PipeConnection north, PipeConnection south, PipeConnection west, PipeConnection east, PipeConnection up, PipeConnection down) {
        VoxelShape shape = Shapes.box(.3, .3, .3, .7, .7, .7);
        shape = combineShape(shape, north, SHAPE_CABLE_NORTH, SHAPE_BLOCK_NORTH);
        shape = combineShape(shape, south, SHAPE_CABLE_SOUTH, SHAPE_BLOCK_SOUTH);
        shape = combineShape(shape, west, SHAPE_CABLE_WEST, SHAPE_BLOCK_WEST);
        shape = combineShape(shape, east, SHAPE_CABLE_EAST, SHAPE_BLOCK_EAST);
        shape = combineShape(shape, up, SHAPE_CABLE_UP, SHAPE_BLOCK_UP);
        shape = combineShape(shape, down, SHAPE_CABLE_DOWN, SHAPE_BLOCK_DOWN);
        return shape;
    }

    default VoxelShape combineShape(VoxelShape shape, PipeConnection PipeConnection, VoxelShape cableShape, VoxelShape blockShape) {
        if (PipeConnection == PipeConnection.PIPE) {
            return Shapes.join(shape, cableShape, BooleanOp.OR);
        } else if (PipeConnection == PipeConnection.BLOCK || PipeConnection == PipeConnection.INPUT || PipeConnection == PipeConnection.OUTPUT) {
            return Shapes.join(shape, Shapes.join(blockShape, cableShape, BooleanOp.OR), BooleanOp.OR);
        } else {
            return shape;
        }
    }

    default PipeConnection getConnectorType(BlockGetter world, BlockPos connectorPos, Direction facing) {
        BlockPos pos = connectorPos.relative(facing);
        BlockState fromState = world.getBlockState(connectorPos);
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof IPipeShape && fromState.getBlock().equals(state.getBlock())) {
            return PipeConnection.PIPE;
        } else if (isConnectable(world, connectorPos, facing)) {
            if (fromState.is(Blocks.AIR)) return PipeConnection.BLOCK;
            return fromState.getValue(PipeUtil.getProp(facing)) != PipeConnection.NONE ? fromState.getValue(PipeUtil.getProp(facing)) : PipeConnection.BLOCK;
        } else {
            return PipeConnection.NONE;
        }
    }

    default boolean isConnectable(BlockGetter world, BlockPos connectorPos, Direction facing) {
        BlockPos pos = connectorPos.relative(facing);
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) {
            return false;
        }
        BlockEntity te = world.getBlockEntity(pos);
        if (te == null) {
            return false;
        }
        return te.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, pos, facing.getOpposite()) != null;
    }

    @Nonnull
    default BlockState calculateState(LevelAccessor world, BlockPos pos, BlockState state) {
        PipeConnection north = getConnectorType(world, pos, Direction.NORTH);
        PipeConnection south = getConnectorType(world, pos, Direction.SOUTH);
        PipeConnection west = getConnectorType(world, pos, Direction.WEST);
        PipeConnection east = getConnectorType(world, pos, Direction.EAST);
        PipeConnection up = getConnectorType(world, pos, Direction.UP);
        PipeConnection down = getConnectorType(world, pos, Direction.DOWN);

        return state
                .setValue(NORTH, north)
                .setValue(SOUTH, south)
                .setValue(WEST, west)
                .setValue(EAST, east)
                .setValue(UP, up)
                .setValue(DOWN, down);
    }

}
