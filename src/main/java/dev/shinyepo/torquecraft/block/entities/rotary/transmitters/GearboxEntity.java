package dev.shinyepo.torquecraft.block.entities.rotary.transmitters;

import dev.shinyepo.torquecraft.config.TransmitterConfig;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GearboxEntity extends RotaryTransmitter {
    public GearboxEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.GEARBOX1_2_ENTITY.get(), pPos, pBlockState, TransmitterConfig.GEARBOX);
    }
}
