package dev.shinyepo.torquecraft.block.entities.pipes;

import dev.shinyepo.torquecraft.registries.TorqueBlockEntities;
import dev.shinyepo.torquecraft.utils.TorqueFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class FluidPipeEntity extends BlockEntity {
    private Set<BlockPos> outputs = null;
    private final int fluidCapacity = 64000;
    private final TorqueFluidTank fluidTank = new TorqueFluidTank(fluidCapacity);


    public FluidPipeEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public FluidPipeEntity(BlockPos pos, BlockState state) {
        super(TorqueBlockEntities.FLUID_PIPE_ENTITY.get(), pos, state);
    }

    public void tickServer() {

    }

    private void checkOutputs() {
        if (outputs == null) {
            outputs = new HashSet<>();
            traverse(worldPosition, pipe -> {
                // Check for all energy receivers around this position (ignore cables)
                for (Direction direction : Direction.values()) {
                    BlockPos p = pipe.getBlockPos().relative(direction);
                    BlockEntity te = level.getBlockEntity(p);
                    if (te != null && !(te instanceof FluidPipeEntity)) {
                        IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, p, null);
                        if (handler != null) {
                            if (handler.getTankCapacity(1) > 0) {
                                outputs.add(p);
                            }
                        }
                    }
                }
            });
        }
    }

    public void markDirty() {
        traverse(worldPosition, pipe -> pipe.outputs = null);
    }

    // This is a generic function that will traverse all cables connected to this cable
    // and call the given consumer for each cable.
    private void traverse(BlockPos pos, Consumer<FluidPipeEntity> consumer) {
        Set<BlockPos> traversed = new HashSet<>();
        traversed.add(pos);
        consumer.accept(this);
        traverse(pos, traversed, consumer);
    }

    private void traverse(BlockPos pos, Set<BlockPos> traversed, Consumer<FluidPipeEntity> consumer) {
        for (Direction direction : Direction.values()) {
            BlockPos p = pos.relative(direction);
            if (!traversed.contains(p)) {
                traversed.add(p);
                if (level.getBlockEntity(p) instanceof FluidPipeEntity pipe) {
                    consumer.accept(pipe);
                    pipe.traverse(p, traversed, consumer);
                }
            }
        }
    }

    public IFluidHandler getFluidTank() {
        return this.fluidTank;
    }
}
