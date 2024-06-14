package dev.shinyepo.torquecraft.factory.pipe.network;

import dev.shinyepo.torquecraft.capabilities.handlers.fluid.PressureFluidHandler;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.constants.TorqueNBT;
import dev.shinyepo.torquecraft.factory.TorqueFluidTank;
import dev.shinyepo.torquecraft.network.fluid.PressureFluidNetwork;
import dev.shinyepo.torquecraft.network.fluid.PressureFluidNetworkRegistry;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class PressureFluidTransmitter extends BlockEntity implements IPressureTransmitter {
    private static final SideType[] sides = new SideType[]{SideType.NONE, SideType.NONE, SideType.NONE, SideType.NONE, SideType.NONE, SideType.NONE};
    private final Map<BlockPos, BlockCapabilityCache<IFluidHandler, Direction>> capCacheMap = new Object2ObjectOpenHashMap<>();
    private final PressureFluidHandler pressureFluidHandler = new PressureFluidHandler(64000) {
        @Override
        public void markDirty() {
            super.markDirty();
            setChanged();
        }
    };
    private PressureFluidNetwork network;
    private UUID networkId;

    public PressureFluidTransmitter(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.FLUID_PIPE_ENTITY.get(), pPos, pBlockState);
    }

    public TorqueFluidTank getTank() {
        return pressureFluidHandler.getTank();
    }

    public void fillTank(FluidStack newStack) {
        pressureFluidHandler.getTank().setFluid(newStack);
//        pressureFluidHandler.markDirty();
    }

    @Override
    public Map<IPressureTransmitter, Map<BlockPos, BlockCapabilityCache<IFluidHandler, Direction>>> getClientCapabilities() {
        return Map.of(this, capCacheMap);
    }

    @Override
    public SideType[] getSidesConfig() {
        return sides;
    }

    @Override
    public UUID getNetworkId() {
        return networkId;
    }

    @Override
    public void updateNetwork(PressureFluidNetwork network) {
        this.network = network;
        this.networkId = network.getNetworkId();
        setChanged();
    }

    public IFluidHandler getFluidTank(Direction dir) {
        if (network != null)
            return network.getTank();
        return null;
    }

    @Override
    public void removeTransmitter() {
        PressureFluidNetworkRegistry.getInstance().unregisterDevice(getNetworkId(), this);
    }

    public void fetchCapabilities() {
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] != SideType.BLOCKED) {
                Direction dir = Direction.values()[i];
                BlockPos relativeCap = this.getBlockPos().relative(dir);
                if (getLevel().getBlockEntity(relativeCap) instanceof IPressureTransmitter) continue;
                var cap = BlockCapabilityCache.create(
                        Capabilities.FluidHandler.BLOCK,
                        (ServerLevel) getLevel(),
                        relativeCap,
                        dir.getOpposite()
                );
                if (cap.getCapability() != null) {
                    this.capCacheMap.put(relativeCap, cap);
                }
            }
        }
        if (network != null)
            network.updateCapabilities(this, capCacheMap);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        pressureFluidHandler.deserializeNBT(provider, tag);
        if (tag.contains(TorqueNBT.NETWORK_ID)) {
            networkId = tag.getUUID(TorqueNBT.NETWORK_ID);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level == null || this.level.isClientSide()) return;
        fetchCapabilities();
        updateNetwork(PressureFluidNetworkRegistry.getInstance().registerDevice(this));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        pressureFluidHandler.serializeNBT(provider, tag);
        if (network != null) {
            tag.putUUID(TorqueNBT.NETWORK_ID, network.getNetworkId());
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag nbt = super.getUpdateTag(provider);
        saveAdditional(nbt, provider);
        return nbt;
    }
}
