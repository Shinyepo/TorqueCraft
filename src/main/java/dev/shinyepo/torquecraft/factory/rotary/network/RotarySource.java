package dev.shinyepo.torquecraft.factory.rotary.network;

import dev.shinyepo.torquecraft.config.SourceConfig;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
import dev.shinyepo.torquecraft.network.RotaryNetwork;
import dev.shinyepo.torquecraft.network.RotaryNetworkRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RotarySource extends RotaryNetworkDevice<SourceConfig> {
    private RotaryNetwork network;
    private final SourceConfig sourceConfig;

    public RotarySource(BlockEntityType<?> type, BlockPos pos, BlockState blockState, SourceConfig config) {
        super(type, pos, blockState, config);
        this.sourceConfig = config;
        this.rotaryHandler.get().setAcceleration(config.getWindup());
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (network == null || network.getNetworkId() != this.getNetworkId()) {
            network = RotaryNetworkRegistry.getInstance().getNetwork(this.getNetworkId());
        } else {
            network.emitPower(this.rotaryHandler.get().getAngular(), this.rotaryHandler.get().getTorque());
        }

        if (state.getValue(TorqueAttributes.OPERATIONAL)) {
            if (rotaryHandler.get().getAngular() < sourceConfig.getAngular()) {
                rotaryHandler.get().spinupSource();
            }
        } else {
            if (rotaryHandler.get().getPower() > 0) {
                rotaryHandler.get().slowDownSource();
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level == null || this.level.isClientSide()) return;
        updateNetwork(RotaryNetworkRegistry.getInstance().registerSource(this));
    }

    public void removeSource() {
        if (this.level != null && !this.level.isClientSide) {
            RotaryNetworkRegistry.getInstance().removeSource(this.getNetworkId(), this);
        }
    }
}
