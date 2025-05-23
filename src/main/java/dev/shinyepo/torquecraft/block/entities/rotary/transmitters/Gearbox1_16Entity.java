package dev.shinyepo.torquecraft.block.entities.rotary.transmitters;

import dev.shinyepo.torquecraft.config.GearboxRatio;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class Gearbox1_16Entity extends GearboxEntity {
    public Gearbox1_16Entity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.GEARBOX1_16_ENTITY.get(), pPos, pBlockState, GearboxRatio.RATIO_16);
    }
}
