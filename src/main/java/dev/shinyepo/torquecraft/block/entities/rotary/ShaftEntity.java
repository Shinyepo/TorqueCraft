package dev.shinyepo.torquecraft.block.entities.rotary;

import dev.shinyepo.torquecraft.factory.rotary.IRotaryIO;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import dev.shinyepo.torquecraft.factory.rotary.RotaryTransmitter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ShaftEntity extends RotaryTransmitter {


    public ShaftEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.SHAFT_ENTITY.get(), pPos, pBlockState);
    }

    public void tick(BlockPos pPos, BlockState pState) {
        updateAnimation();
    }

}
