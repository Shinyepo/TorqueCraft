package dev.shinyepo.torquecraft.block.entities;

import dev.shinyepo.torquecraft.registries.TorqueBlockEntities;
import dev.shinyepo.torquecraft.utils.TorqueFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public class PumpEntity extends BlockEntity {
    private final int fluidCapacity = 16000;
    private final TorqueFluidTank fluidTank = new TorqueFluidTank(fluidCapacity);

    public PumpEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.PUMP_ENTITY.get(), pPos, pBlockState);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

    }

    public TorqueFluidTank getFluidTank() {
        return fluidTank;
    }
}
