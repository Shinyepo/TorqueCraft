package dev.shinyepo.torquecraft.block.prefab.rotary.transmitters.gearboxes;

import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.Gearbox1_4Entity;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.GearboxEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class Gearbox1_4 extends Gearbox {

    public Gearbox1_4(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable GearboxEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new Gearbox1_4Entity(pos, state);
    }
}
