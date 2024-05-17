package dev.shinyepo.torquecraft.block.entities.rotary;

import dev.shinyepo.torquecraft.capabilities.handlers.RotaryHandler;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
import dev.shinyepo.torquecraft.factory.rotary.RotarySource;
import dev.shinyepo.torquecraft.network.RotaryNetwork;
import dev.shinyepo.torquecraft.network.RotaryNetworkRegistry;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;

public class SteamEngineEntity extends RotarySource {
    public static float MAX_TORQUE = 50f;
    public static float MAX_ANGULAR = 512f;
    private static int SPEEDUP_TIME = 15;
    public final Lazy<RotaryHandler> rotaryHandler = initRotarySource(MAX_ANGULAR, MAX_TORQUE, Direction.NORTH);
    private RotaryNetwork network;

    public SteamEngineEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.STEAM_ENGINE_ENTITY.get(), pPos, pBlockState);
        rotaryHandler.get().setAcceleration(SPEEDUP_TIME);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pState.getValue(TorqueAttributes.OPERATIONAL)) {
            if (rotaryHandler.get().getAngular() < MAX_ANGULAR) {
                rotaryHandler.get().spinupSource();
            }
        } else {
            if (rotaryHandler.get().getPower() > 0) {
                rotaryHandler.get().slowDownSource();

            }
        }
        if (network == null || network.getNetworkId() != this.getNetworkId()) {
            network = RotaryNetworkRegistry.getInstance().getNetwork(this.getNetworkId());
        } else {
            network.emitPower(this.rotaryHandler.get().getAngular(), this.rotaryHandler.get().getTorque());
        }
    }
}
