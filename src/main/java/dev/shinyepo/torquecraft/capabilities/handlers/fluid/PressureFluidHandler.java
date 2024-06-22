package dev.shinyepo.torquecraft.capabilities.handlers.fluid;

import dev.shinyepo.torquecraft.factory.TorqueFluidTank;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class PressureFluidHandler implements IPressureFluidHandler {
    protected final TorqueFluidTank fluidTank;
    protected double fluidPressure;
    protected int transferRate;

    public PressureFluidHandler(int maxCapacity) {
        this.fluidTank = new TorqueFluidTank(maxCapacity) {
            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                markDirty();
            }

            @Override
            public FluidTank setCapacity(int capacity) {
                var result = super.setCapacity(capacity);
                updatePressure();
                return result;
            }
        };
    }

    public TorqueFluidTank getTank() {
        return fluidTank;
    }

    public int getTransferRate() {
        return transferRate;
    }

    private void calculatePressure() {
        double pressure = (2.2 * Math.pow(10, 6)) * ((double) (fluidTank.getFluidAmount()) / fluidTank.getCapacity());
        fluidPressure = pressure;
    }

    private void calculateTransferRate() {
        var speed = Math.sqrt((2 * fluidPressure) / 1000);
        transferRate = (int) ((0.7071 * speed) * 1000);
    }

    public void markDirty() {
        updatePressure();
    }

    private void updatePressure() {
        calculatePressure();
        calculateTransferRate();
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        fluidTank.readFromNBT(provider, nbt);
    }

    public void serializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        fluidTank.writeToNBT(provider, tag);
    }
}
