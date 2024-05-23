package dev.shinyepo.torquecraft.factory.rotary.network;

import dev.shinyepo.torquecraft.capabilities.handlers.fluid.IFluidBuffer;
import dev.shinyepo.torquecraft.config.SourceConfig;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
import dev.shinyepo.torquecraft.factory.TorqueFluidTank;
import dev.shinyepo.torquecraft.network.RotaryNetwork;
import dev.shinyepo.torquecraft.network.RotaryNetworkRegistry;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncFluidS2C;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class RotarySource extends RotaryNetworkDevice<SourceConfig> implements IFluidBuffer {
    private RotaryNetwork network;
    public final SourceConfig sourceConfig;
    protected Lazy<TorqueFluidTank> fluidTank;
    private boolean spinup = false;


    public RotarySource(BlockEntityType<?> type, BlockPos pos, BlockState blockState, SourceConfig config) {
        super(type, pos, blockState, config);
        this.sourceConfig = config;
        initFluidTank(config);
        this.rotaryHandler.get().setAcceleration(config.getWindup());
    }

    private void initFluidTank(SourceConfig config) {
        this.fluidTank = Lazy.of(() -> new TorqueFluidTank(config.getCapacity(), fluid -> fluid.is(config.getFuel())) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if(!level.isClientSide()) {
                    TorqueMessages.sendToAllPlayers(new SyncFluidS2C(worldPosition, this.fluid));
                }
            }
        });
    }

    public void tick(BlockState state, double temp) {
        if (network == null || network.getNetworkId() != this.getNetworkId()) {
            network = RotaryNetworkRegistry.getInstance().getNetwork(this.getNetworkId());
        } else {
            network.emitPower(this.rotaryHandler.get().getAngular(), this.rotaryHandler.get().getTorque());
        }

        if (state.getValue(TorqueAttributes.OPERATIONAL) && fluidTank.get().getFluidAmount() > config.getUsage() && temp > 100) {
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
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (!fluidTank.get().isEmpty()) {
            fluidTank.get().writeToNBT(provider,tag);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("Fluid")) {
            fluidTank.get().readFromNBT(provider,tag);
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
        if (dir == null) return this.fluidTank.get();
        if (dir == getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite()) {
            return this.fluidTank.get();
        }
        return null;
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluidTank.get().setFluid(fluidStack);
    }
}
