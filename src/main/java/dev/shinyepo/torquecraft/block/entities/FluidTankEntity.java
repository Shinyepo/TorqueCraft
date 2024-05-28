package dev.shinyepo.torquecraft.block.entities;

import dev.shinyepo.torquecraft.factory.TorqueFluidTank;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncFluidS2C;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class FluidTankEntity extends BlockEntity {
    private final int fluidCapacity = 64000;

    private final TorqueFluidTank fluidTank = new TorqueFluidTank(fluidCapacity) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()) {
                TorqueMessages.sendToAllPlayers(new SyncFluidS2C(worldPosition, this.fluid));
            }
        }
    };

    private BlockCapabilityCache<IFluidHandler, @Nullable Direction> capCache;

    public FluidTankEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.FLUID_TANK_ENTITY.get(), pPos, pBlockState);
    }

    public void tick() {
        distributeFluid();
    }

    private void distributeFluid() {
        if (fluidTank.isEmpty()) {
            return;
        }
        IFluidHandler fHandler = this.capCache.getCapability();
        if (fHandler != null) {
            if (!canAcceptFluid(fHandler)) return;
            int remainingSpace = fHandler.getTankCapacity(0) - fHandler.getFluidInTank(0).getAmount();
            int amount = Math.min(remainingSpace, 250);

            fHandler.fill(new FluidStack(fluidTank.getFluid().getFluid(), amount), IFluidHandler.FluidAction.EXECUTE);
            fluidTank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
            setChanged();
        }
    }

    private boolean canAcceptFluid(IFluidHandler handler) {
        return handler.getFluidInTank(0).isEmpty() || (handler.getFluidInTank(0).is(fluidTank.getFluid().getFluid()) && handler.getTankCapacity(0) > handler.getFluidInTank(0).getAmount());
    }

    public TorqueFluidTank getFluidTank(Direction dir) {
        if (dir == null) return fluidTank;
        if(dir == Direction.UP || dir == Direction.DOWN) {
            return fluidTank;
        }
        return null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.capCache = BlockCapabilityCache.create(
                Capabilities.FluidHandler.BLOCK,
                (ServerLevel) this.getLevel(),
                this.worldPosition.relative(Direction.DOWN),
                null
        );
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluidTank.setFluid(fluidStack);
    }
}
