package dev.shinyepo.torquecraft.rotary;

import dev.shinyepo.torquecraft.factory.RotaryIO;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RotaryTransmitter extends RotaryIO {

    public RotaryTransmitter(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
    }
}
