package dev.shinyepo.torquecraft.block.entities;

import dev.shinyepo.torquecraft.registries.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class FluidPipeEntity extends BlockEntity {
    private Set<BlockPos> outputs = null;


    public FluidPipeEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public FluidPipeEntity(BlockPos pos, BlockState state) {
        super(TorqueBlockEntities.WATER_PIPE_ENTITY.get(), pos, state);
    }

    public void tickServer() {

    }

    // Cached outputs

    // This function will cache all outputs for this cable network. It will do this
    // by traversing all cables connected to this cable and then check for all energy
    // receivers around those cables.
    private void checkOutputs() {
        if (outputs == null) {
            outputs = new HashSet<>();
            traverse(worldPosition, pipe -> {
                // Check for all energy receivers around this position (ignore cables)
                for (Direction direction : Direction.values()) {
                    BlockPos p = pipe.getBlockPos().relative(direction);
                    BlockEntity te = level.getBlockEntity(p);
                    if (te != null && !(te instanceof FluidPipeEntity)) {
                        IEnergyStorage handler = level.getCapability(Capabilities.EnergyStorage.BLOCK, p, null);
                        if (handler != null) {
                            if (handler.canReceive()) {
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
}
