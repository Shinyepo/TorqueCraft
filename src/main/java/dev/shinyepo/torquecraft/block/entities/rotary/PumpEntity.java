package dev.shinyepo.torquecraft.block.entities.rotary;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import dev.shinyepo.torquecraft.capabilities.handlers.fluid.AdaptedFluidHandler;
import dev.shinyepo.torquecraft.capabilities.handlers.fluid.IFluidBuffer;
import dev.shinyepo.torquecraft.config.ClientConfig;
import dev.shinyepo.torquecraft.factory.TorqueFluidTank;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryClient;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncFluidS2C;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

public class PumpEntity extends RotaryClient implements IFluidBuffer {
    private final int fluidCapacity = 16000;
    private final TorqueFluidTank fluidTank = new TorqueFluidTank(fluidCapacity) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()) {
                TorqueMessages.sendToAllPlayers(new SyncFluidS2C(worldPosition, this.fluid));
            }
        }
    };
    private final AdaptedFluidHandler adaptedFluidTank = new AdaptedFluidHandler(fluidTank) {
        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return 0;
        }
    };

    private final List<BlockPos> validSources = Lists.newArrayList();
    public static final ImmutableList<Direction> POSSIBLE_DIRECTIONS = ImmutableList.of(
            Direction.DOWN, Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST
    );

    public PumpEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.PUMP_ENTITY.get(), pPos, pBlockState, ClientConfig.VACUUM);
    }

    public void tick(Level level, BlockPos pos) {
        if (level.getGameTime() % 5 == 0) {
            if (isSource(level, pos.below()) && canFitWater()) {
                if (validSources.isEmpty()) {
                    fetchSources(level, pos);
                }
                BlockPos sourcePos = getSourceToHarvest();
                if (sourcePos != null) {
                    harvestSource(level, sourcePos);
                    setChanged(level,pos,level.getBlockState(pos));

                }
            }
        }
        distributeFluid();
    }

    private BlockPos getSourceToHarvest() {
        if (!validSources.isEmpty()) {
            return validSources.get((int) Math.floor(Math.random() * validSources.size()));
        }
        return null;
    }

    private boolean canFitWater() {
        return this.fluidTank.getFluid().getAmount() + 1000 <= fluidCapacity;
    }

    private boolean isSource(Level level, BlockPos pos) {
        return level.getFluidState(pos).isSource();
    }

    private void harvestSource(Level level, BlockPos sourcePos) {
        validSources.remove(sourcePos);
        if (isSource(level, sourcePos)) {
            level.setBlockAndUpdate(sourcePos, Blocks.AIR.defaultBlockState());
            fluidTank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    private void fetchSources(Level level, BlockPos pos) {
        if (validSources.size() > 8) {
            return;
        }
            for (Direction dir : POSSIBLE_DIRECTIONS) {
                BlockPos blockpos = pos.relative(dir);
                if (level.getFluidState(blockpos).isSource()) {
                    if (!validSources.contains(blockpos)) {
                        validSources.add(blockpos);
                        fetchSources(level, blockpos);
                    }
                }
            }
    }

    public IFluidHandler getFluidTank(Direction dir) {
        if (dir == null) return fluidTank;
        if (dir == Direction.UP) {
            return adaptedFluidTank;
        }
        return null;
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluidTank.setFluid(fluidStack);
    }

    public FluidStack getFluidStack() {
        return this.fluidTank.getFluid();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (!fluidTank.isEmpty()) {
            fluidTank.writeToNBT(provider,tag);
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag,provider);
        if (tag.contains("Fluid")) {
            fluidTank.readFromNBT(provider,tag);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag nbt = super.getUpdateTag(provider);
        saveAdditional(nbt, provider);
        return nbt;
    }

    private void distributeFluid() {
        if (fluidTank.isEmpty()) {
            return;
        }
        IFluidHandler fHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos().relative(Direction.UP),Direction.UP);
        if (fHandler != null) {
            if (!canAcceptFluid(fHandler)) return;
            int remainingSpace = fHandler.getTankCapacity(0) - fHandler.getFluidInTank(0).getAmount();
            int amount = Math.min(remainingSpace, Math.min(1000, fluidTank.getFluidAmount()));

            var filled = fHandler.fill(new FluidStack(fluidTank.getFluid().getFluid(), amount), IFluidHandler.FluidAction.EXECUTE);
            fluidTank.drain(filled, IFluidHandler.FluidAction.EXECUTE);

            setChanged();
        }
    }

    private boolean canAcceptFluid(IFluidHandler handler) {
        return handler.getFluidInTank(0).isEmpty() || (handler.getFluidInTank(0).is(fluidTank.getFluid().getFluid()) && handler.getTankCapacity(0) > handler.getFluidInTank(0).getAmount());
    }
}
