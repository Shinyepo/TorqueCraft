package dev.shinyepo.torquecraft.capabilities.handlers.fluid;

import dev.shinyepo.torquecraft.factory.TorqueFluidTank;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class AdaptedFluidHandler implements IFluidHandler {

    private final TorqueFluidTank handler;

    public AdaptedFluidHandler(TorqueFluidTank handler) {
        this.handler = handler;
    }

    @Override
    public int getTanks() {
        return handler.getTanks();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return handler.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return handler.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return handler.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return handler.fill(resource, action);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return handler.drain(resource, action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return handler.drain(maxDrain, action);
    }
}
