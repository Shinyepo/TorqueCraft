package dev.shinyepo.torquecraft.network.fluid;

import dev.shinyepo.torquecraft.capabilities.handlers.fluid.PressureFluidHandler;
import dev.shinyepo.torquecraft.factory.pipe.network.IPressureTransmitter;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class PressureFluidNetwork {
    private final UUID network_id;
    private final Map<BlockPos, IPressureTransmitter> transmitters = new Object2ObjectOpenHashMap<>();
    private final Map<IPressureTransmitter, Map<BlockPos, BlockCapabilityCache<IFluidHandler, Direction>>> capabilityCacheMap = new Object2ObjectOpenHashMap<>();

    public PressureFluidNetwork(UUID id) {
        network_id = id;
    }    private final PressureFluidHandler pressureFluidHandler = new PressureFluidHandler(64000) {
        @Override
        public void markDirty() {
            super.markDirty();
            updateTransmitterTank();
        }
    };

    public UUID getNetworkId() {
        return this.network_id;
    }

    public boolean validDevice(IPressureTransmitter transmitter) {
        return !transmitters.containsKey(transmitter.getBlockPos());
    }

    public Map<BlockPos, IPressureTransmitter> getTransmitters() {
        return transmitters;
    }

    public void emitFluid() {
        capabilityCacheMap.forEach((transmitter, capMap) -> {
            if (!capMap.isEmpty())
                capMap.forEach((blockPos, cap) -> {
                    var handler = cap.getCapability();
                    if (handler != null) {
                        var tillFilled = handler.getTankCapacity(0) - handler.getFluidInTank(0).getAmount();
                        if (tillFilled > 0) {
                            var toDrain = Math.min(tillFilled, Math.min(pressureFluidHandler.getTransferRate(), pressureFluidHandler.getTank().getFluidAmount()));
                            var filledAmount = handler.fill(new FluidStack(pressureFluidHandler.getTank().getFluid().getFluid(), toDrain), IFluidHandler.FluidAction.EXECUTE);
                            if (filledAmount > 0)
                                pressureFluidHandler.getTank().drain(filledAmount, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                });
        });
    }

    private void updateTransmitterTank() {
        if (transmitters.isEmpty()) return;
        int transmitterCount = transmitters.size();
        int amountToSplit = ((int) pressureFluidHandler.getTank().getFluidAmount() / transmitterCount);
        transmitters.forEach((blockPos, transmitter) -> transmitter.fillTank(new FluidStack(pressureFluidHandler.getTank().getFluid().getFluid(), amountToSplit)));
    }

    private void splitFluid() {
        pressureFluidHandler.getTank().setCapacity(64000 * transmitters.size());
        AtomicReference<FluidStack> newStack = new AtomicReference<>(FluidStack.EMPTY);
        transmitters.forEach((pos, handler) -> {
            if (!handler.getTank().isEmpty() && !newStack.get().isEmpty()) {
                newStack.get().setAmount(newStack.get().getAmount() + handler.getTank().getFluidAmount());
            } else if (!handler.getTank().isEmpty() && newStack.get().isEmpty()) {
                newStack.set(handler.getTank().getFluid());
            }
        });
        if (!newStack.get().isEmpty()) {
            pressureFluidHandler.getTank().setFluid(newStack.get());
            updateTransmitterTank();
        }
    }

    public void clear() {
        this.transmitters.clear();
        this.capabilityCacheMap.clear();
    }

    public <T extends IPressureTransmitter> void registerTransmitter(T transmitter) {
        if (!validDevice(transmitter)) return;
        BlockPos pos = transmitter.getBlockPos();
        capabilityCacheMap.putAll(transmitter.getClientCapabilities());
        transmitters.put(pos, transmitter);
        splitFluid();
    }

    public <T extends IPressureTransmitter> void unregisterTransmitter(T transmitter) {
        if (validDevice(transmitter)) return;
        BlockPos pos = transmitter.getBlockPos();
        var transmitterCapCache = transmitter.getClientCapabilities();
        transmitterCapCache.forEach((capPos, cache) -> capabilityCacheMap.remove(capPos));
        transmitters.remove(pos, transmitter);
    }

    public IFluidHandler getTank() {
        return pressureFluidHandler.getTank();
    }

    public void updateCapabilities(IPressureTransmitter transmitter, Map<BlockPos, BlockCapabilityCache<IFluidHandler, Direction>> capMap) {
        capabilityCacheMap.put(transmitter, capMap);
    }
}
