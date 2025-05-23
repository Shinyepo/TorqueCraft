package dev.shinyepo.torquecraft.block.entities.rotary.transmitters;

import dev.shinyepo.torquecraft.config.TransmitterConfig;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class BevelGearsEntity extends RotaryTransmitter {
    public BevelGearsEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.BEVEL_GEARS_ENTITY.get(), pPos, pBlockState, TransmitterConfig.BEVEL_GEARS);
    }

    @Override
    public void emitPower() {
        getNetwork().emitPower(this.getBlockPos().relative(Direction.UP), this.rotaryHandler.get().getAngular(), this.rotaryHandler.get().getTorque());
    }
}
