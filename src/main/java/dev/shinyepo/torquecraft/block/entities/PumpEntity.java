package dev.shinyepo.torquecraft.block.entities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncFluidS2C;
import dev.shinyepo.torquecraft.networking.packets.SyncPumpFluidS2C;
import dev.shinyepo.torquecraft.registries.TorqueBlockEntities;
import dev.shinyepo.torquecraft.utils.TorqueFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

public class PumpEntity extends BlockEntity {
    private final int fluidCapacity = 16000;
    private final TorqueFluidTank fluidTank = new TorqueFluidTank(fluidCapacity) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()) {
                TorqueMessages.sendToAllPlayers(new SyncPumpFluidS2C(worldPosition, this.fluid));
            }
        }
    };
    private final List<BlockPos> validSources = Lists.newArrayList();
    public static final ImmutableList<Direction> POSSIBLE_DIRECTIONS = ImmutableList.of(
            Direction.DOWN, Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST
    );

    public PumpEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.PUMP_ENTITY.get(), pPos, pBlockState);
    }

    public void tick(Level level, BlockPos pos) {
        if(level.getGameTime() % 20 == 0) {
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

    public TorqueFluidTank getFluidTank(Direction dir) {
        if (dir == Direction.UP) {
            return fluidTank;
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
        fluidTank.writeToNBT(provider,tag);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag,provider);
        fluidTank.readFromNBT(provider,tag);
    }
}
