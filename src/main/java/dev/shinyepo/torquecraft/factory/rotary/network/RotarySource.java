package dev.shinyepo.torquecraft.factory.rotary.network;

import dev.shinyepo.torquecraft.capabilities.handlers.fluid.IFluidBuffer;
import dev.shinyepo.torquecraft.config.SourceConfig;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
import dev.shinyepo.torquecraft.factory.IWrenchInteraction;
import dev.shinyepo.torquecraft.factory.TorqueFluidTank;
import dev.shinyepo.torquecraft.network.RotaryNetworkRegistry;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncFluidS2C;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class RotarySource extends RotaryNetworkDevice<SourceConfig> implements IFluidBuffer, IWrenchInteraction {
    protected Lazy<TorqueFluidTank> fluidTank;
    private boolean spinup = false;


    public RotarySource(BlockEntityType<?> type, BlockPos pos, BlockState blockState, SourceConfig config) {
        super(type, pos, blockState, config);
        initFluidTank(config);
        this.rotaryHandler.get().setAcceleration(config.getWindup());
        configureSides(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    public void configureSides(Direction facing) {
        configureSides(facing, SideType.OUTPUT);
    }

    private void initFluidTank(SourceConfig config) {
        this.fluidTank = Lazy.of(() -> new TorqueFluidTank(config.getCapacity(), fluid -> fluid.is(config.getFuel())) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if(level != null && !level.isClientSide()) {
                    TorqueMessages.sendToAllPlayers(new SyncFluidS2C(worldPosition, this.fluid));
                }
            }
        });
    }

    public void tick(BlockState state) {
        if (network == null || network.getNetworkId() != this.getNetworkId()) {
            network = RotaryNetworkRegistry.getInstance().getNetwork(this.getNetworkId());
        } else {
            if (this.rotaryHandler.get().getPower() > 0) {
                network.emitPower(getBlockPos().relative(getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)), this.rotaryHandler.get().getAngular(), this.rotaryHandler.get().getTorque(), this);
            }
        }

        if (this.rotaryHandler.get().getPower() == 0) {
            network.validateTransmittingSources(this);
        }

        if (state.getValue(TorqueAttributes.OPERATIONAL) && fluidTank.get().getFluidAmount() > config.getUsage() && rotaryHandler.get().getTemp() > 100) {
            if (rotaryHandler.get().getAngular() < config.getAngular()) {
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
