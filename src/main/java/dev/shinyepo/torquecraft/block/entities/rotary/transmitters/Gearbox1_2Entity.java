package dev.shinyepo.torquecraft.block.entities.rotary.transmitters;

import dev.shinyepo.torquecraft.config.GearboxRatio;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class Gearbox1_2Entity extends GearboxEntity {
    public Gearbox1_2Entity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.GEARBOX1_2_ENTITY.get(), pPos, pBlockState, GearboxRatio.RATIO_2);
    }
}
