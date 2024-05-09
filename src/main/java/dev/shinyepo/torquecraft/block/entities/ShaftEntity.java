package dev.shinyepo.torquecraft.block.entities;

import dev.shinyepo.torquecraft.attributes.MachineAttributes;
import dev.shinyepo.torquecraft.registries.TorqueBlockEntities;
import dev.shinyepo.torquecraft.rotary.RotarySource;
import dev.shinyepo.torquecraft.rotary.RotaryTransmitter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ShaftEntity extends RotaryTransmitter {


    public ShaftEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.STEAM_ENGINE_ENTITY.get(), pPos, pBlockState);

    }

    public void tick(BlockPos pPos, BlockState pState) {

    }

}
