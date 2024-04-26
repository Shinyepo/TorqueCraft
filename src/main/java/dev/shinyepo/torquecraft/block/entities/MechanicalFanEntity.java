package dev.shinyepo.torquecraft.block.entities;

import dev.shinyepo.torquecraft.registries.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MechanicalFanEntity extends BlockEntity {
    public MechanicalFanEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.MECHANICAL_FAN_ENTITY.get(), pPos, pBlockState);
    }


    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

    }
}
