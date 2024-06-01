package dev.shinyepo.torquecraft.block.entities.rotary.transmitters;

import dev.shinyepo.torquecraft.config.TransmitterConfig;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ShaftEntity extends RotaryTransmitter {
    public ShaftEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.SHAFT_ENTITY.get(), pPos, pBlockState, TransmitterConfig.SHAFT);
    }
}
