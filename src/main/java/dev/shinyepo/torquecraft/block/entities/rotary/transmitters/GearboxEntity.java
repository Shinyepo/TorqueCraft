package dev.shinyepo.torquecraft.block.entities.rotary.transmitters;

import dev.shinyepo.torquecraft.config.GearboxRatio;
import dev.shinyepo.torquecraft.config.TransmitterConfig;
import dev.shinyepo.torquecraft.factory.IModeMachine;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GearboxEntity extends RotaryTransmitter implements IModeMachine {
    private GearboxRatio RATIO;

    public GearboxEntity(BlockPos pPos, BlockState pBlockState, GearboxRatio ratio) {
        super(TorqueBlockEntities.GEARBOX1_2_ENTITY.get(), pPos, pBlockState, TransmitterConfig.GEARBOX);
        RATIO = ratio;
    }

    public int getRatio() {
        return RATIO.getRatio();
    }
}
