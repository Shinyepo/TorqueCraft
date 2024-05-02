package dev.shinyepo.torquecraft.fluid;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.registries.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.TorqueItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, TorqueCraft.MODID);

    public static final Supplier<FlowingFluid> SOURCE_LUBRICANT = FLUIDS.register("lubricant_fluid", () -> new BaseFlowingFluid.Source(TorqueFluids.LUBRICANT_FLUID_PROPERTIES));
    public static final Supplier<FlowingFluid> FLOWING_LUBRICANT = FLUIDS.register("flowing_lubricant_fluid", () -> new BaseFlowingFluid.Flowing(TorqueFluids.LUBRICANT_FLUID_PROPERTIES));

    public static final Supplier<FlowingFluid> SOURCE_JET_FUEL = FLUIDS.register("jet_fuel_fluid", () -> new BaseFlowingFluid.Source(TorqueFluids.JET_FUEL_FLUID_PROPERTIES));
    public static final Supplier<FlowingFluid> FLOWING_JET_FUEL = FLUIDS.register("flowing_jet_fuel_fluid", () -> new BaseFlowingFluid.Flowing(TorqueFluids.JET_FUEL_FLUID_PROPERTIES));

    public static final BaseFlowingFluid.Properties LUBRICANT_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(
            TorqueFluidTypes.LUBRICANT_TYPE, SOURCE_LUBRICANT, FLOWING_LUBRICANT).block(TorqueBlocks.LUBRICANT_BLOCK).bucket(TorqueItems.LUBRICANT_BUCKET);

    public static final BaseFlowingFluid.Properties JET_FUEL_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(
            TorqueFluidTypes.JET_FUEL_TYPE, SOURCE_JET_FUEL, FLOWING_JET_FUEL).block(TorqueBlocks.JET_FUEL_BLOCK).bucket(TorqueItems.JET_FUEL_BUCKET);
}
