package dev.shinyepo.torquecraft.block.prefab.rotary.transmitters.gearboxes;

import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.Gearbox1_2Entity;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.GearboxEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class Gearbox1_2 extends Gearbox {

    public Gearbox1_2(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable GearboxEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new Gearbox1_2Entity(pos, state);
    }
}
