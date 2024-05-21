package dev.shinyepo.torquecraft.factory;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class TorqueFluidTank extends FluidTank {
    public TorqueFluidTank(int capacity) {
        super(capacity);
    }

    public TorqueFluidTank(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }
}
