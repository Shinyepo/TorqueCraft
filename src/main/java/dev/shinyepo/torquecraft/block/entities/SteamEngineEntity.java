package dev.shinyepo.torquecraft.block.entities;

import dev.shinyepo.torquecraft.attributes.MachineAttributes;
import dev.shinyepo.torquecraft.capabilities.types.IRotaryHandler;
import dev.shinyepo.torquecraft.handlers.RotaryHandler;
import dev.shinyepo.torquecraft.registries.TorqueBlockEntities;
import dev.shinyepo.torquecraft.rotary.RotarySource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.util.Lazy;

public class SteamEngineEntity extends RotarySource {
    public static float MAX_TORQUE = 50f;
    public static float MAX_ANGULAR = 128f;
    private static int SPEEDUP_TIME = 5;
    public final Lazy<RotaryHandler> rotaryHandler = initRotarySource(MAX_ANGULAR, MAX_TORQUE, Direction.NORTH);

    public SteamEngineEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.STEAM_ENGINE_ENTITY.get(), pPos, pBlockState);
        rotaryHandler.get().setAcceleration(SPEEDUP_TIME);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pState.getValue(MachineAttributes.OPERATIONAL)) {
            if (rotaryHandler.get().getAngular() < MAX_ANGULAR) {
                rotaryHandler.get().spinupSource();
            }
        } else {
            if (rotaryHandler.get().getPower() > 0) {
                rotaryHandler.get().slowDownSource();

            }
        }
    }
}
