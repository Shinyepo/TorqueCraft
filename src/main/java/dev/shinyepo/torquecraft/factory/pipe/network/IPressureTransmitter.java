package dev.shinyepo.torquecraft.factory.pipe.network;

import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.factory.TorqueFluidTank;
import dev.shinyepo.torquecraft.network.fluid.PressureFluidNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.Map;
import java.util.UUID;

public interface IPressureTransmitter {
    BlockPos getBlockPos();

    Map<IPressureTransmitter, Map<BlockPos, BlockCapabilityCache<IFluidHandler, Direction>>> getClientCapabilities();

    SideType[] getSidesConfig();

    Level getLevel();

    UUID getNetworkId();

    void updateNetwork(PressureFluidNetwork network);

    void removeTransmitter();

    TorqueFluidTank getTank();

    void fillTank(FluidStack newFluid);
}
