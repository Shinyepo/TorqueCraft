package dev.shinyepo.torquecraft.block.entities.pipes;

import dev.shinyepo.torquecraft.helpers.PipeConnection;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncFluidS2C;
import dev.shinyepo.torquecraft.registries.TorqueBlockEntities;
import dev.shinyepo.torquecraft.utils.PipeUtil;
import dev.shinyepo.torquecraft.utils.TorqueFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class FluidPipeEntity extends BlockEntity {
    private Set<BlockPos> outputs = null;
    private Set<BlockPos> inputs = null;
    private final int fluidCapacity = 16000;
    private final TorqueFluidTank fluidTank = new TorqueFluidTank(fluidCapacity) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()) {
                TorqueMessages.sendToAllPlayers(new SyncFluidS2C(worldPosition, fluidTank.getFluid()));
            }
        }
    };;

    private final int maxTransfer = 1000;


    public FluidPipeEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public FluidPipeEntity(BlockPos pos, BlockState state) {
        super(TorqueBlockEntities.FLUID_PIPE_ENTITY.get(), pos, state);
    }

    public void tickServer() {
        if (!fluidTank.isEmpty()) {
            // Only do something if we have energy
            checkOutputs();
            if (!outputs.isEmpty()) {
                // Distribute energy over all outputs
                for (BlockPos p : outputs) {
                    IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, p, null);
                    if (handler != null) {
                        if (canAcceptFluid(handler)) {
                            int remainingSpace = handler.getTankCapacity(0) - handler.getFluidInTank(0).getAmount();
                            int amount = Math.min(Math.min(maxTransfer, fluidTank.getFluidAmount()), remainingSpace);
                                handler.fill(new FluidStack(fluidTank.getFluid().getFluid(), amount), IFluidHandler.FluidAction.EXECUTE);
                                fluidTank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
        }
    }

    private boolean canAcceptFluid(IFluidHandler handler) {
//        int tanks = handler.getTanks();
//        for (int i = 0; i < tanks; i++) {
//            if (!handler.isFluidValid(i,fluidTank.getFluid())) continue;
//            if (handler.getFluidInTank(i).isEmpty()) return true;
//            if (handler.getFluidInTank(i).is(fluidTank.getFluid().getFluid()) && handler.getTankCapacity(i) < handler.getFluidInTank(i).getAmount()) {
//                return true;
//            }
//        }
        return handler.getFluidInTank(0).isEmpty() || (handler.getFluidInTank(0).is(fluidTank.getFluid().getFluid()) && handler.getTankCapacity(0) > handler.getFluidInTank(0).getAmount());
    }

    private void checkOutputs() {
        if (outputs == null) {
            outputs = new HashSet<>();
            traverse(worldPosition, pipe -> {
                // Check for all energy receivers around this position (ignore cables)
                for (Direction direction : Direction.values()) {
                    PipeConnection connection = pipe.getBlockState().getValue(PipeUtil.getProp(direction));
                        BlockPos p = pipe.getBlockPos().relative(direction);
                        BlockEntity te = level.getBlockEntity(p);
                        if (te != null && !(te instanceof FluidPipeEntity)) {
                            IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, p, null);
                            if (handler != null) {
                                if (handler.getTankCapacity(0) > 0) {
                                    if(connection == PipeConnection.INPUT) {
                                        outputs.add(p);
                                    }
                                }
                            }
                        }
                }
            });
        }
    }

    public void markDirty() {
        traverse(worldPosition, pipe -> {
            pipe.outputs = null;
        });
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

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (fluidTank != null) {
            fluidTank.writeToNBT(provider,tag);
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag,provider);
        if (fluidTank != null) {
            fluidTank.readFromNBT(provider,tag);
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag nbt = super.getUpdateTag(provider);
        saveAdditional(nbt, provider);
        return nbt;
    }

    public IFluidHandler getFluidTank(Direction dir) {
        if (dir == null) return fluidTank;
        PipeConnection con = getBlockState().getValue(PipeUtil.getProp(dir.getOpposite()));
        if (con == PipeConnection.OUTPUT) {
            return this.fluidTank;
        }
        return null;
    }

    public void setFluidTank(FluidStack fluid) {
        this.fluidTank.setFluid(fluid);
    }
}
