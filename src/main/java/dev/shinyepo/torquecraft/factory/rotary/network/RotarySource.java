package dev.shinyepo.torquecraft.factory.rotary.network;

import dev.shinyepo.torquecraft.config.SourceConfig;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
import dev.shinyepo.torquecraft.factory.TorqueFluidTank;
import dev.shinyepo.torquecraft.network.RotaryNetwork;
import dev.shinyepo.torquecraft.network.RotaryNetworkRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class RotarySource extends RotaryNetworkDevice<SourceConfig> {
    private RotaryNetwork network;
    private final SourceConfig sourceConfig;
    private Lazy<TorqueFluidTank> fluidTank;
    private boolean spinup = false;


    public RotarySource(BlockEntityType<?> type, BlockPos pos, BlockState blockState, SourceConfig config) {
        super(type, pos, blockState, config);
        this.sourceConfig = config;
        initFluidTank(config);
        this.rotaryHandler.get().setAcceleration(config.getWindup());
    }

    private void initFluidTank(SourceConfig config) {
        this.fluidTank = Lazy.of(() -> new TorqueFluidTank(config.getCapacity(), fluid -> fluid.is(config.getFuel())));
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (network == null || network.getNetworkId() != this.getNetworkId()) {
            network = RotaryNetworkRegistry.getInstance().getNetwork(this.getNetworkId());
        } else {
            network.emitPower(this.rotaryHandler.get().getAngular(), this.rotaryHandler.get().getTorque());
        }

        if (state.getValue(TorqueAttributes.OPERATIONAL) && fluidTank.get().getFluidAmount() > config.getUsage()) {
            if (rotaryHandler.get().getAngular() < sourceConfig.getAngular()) {
                spinup = true;
                rotaryHandler.get().spinupSource();
            } else {
                spinup = false;
            }
            consumeFuel();
        } else {
            if (rotaryHandler.get().getPower() > 0) {
                rotaryHandler.get().slowDownSource();
            }
        }
    }

    private void consumeFuel() {
        if (spinup) {
            fluidTank.get().drain(config.getUsage() * 4, IFluidHandler.FluidAction.EXECUTE);
        } else {
            fluidTank.get().drain(config.getUsage(), IFluidHandler.FluidAction.EXECUTE);
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

    public IFluidHandler getFluidTank(Direction dir) {
        if (dir == getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite()) {
            return this.fluidTank.get();
        }
        return null;
    }
}
