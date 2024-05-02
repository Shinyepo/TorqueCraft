package dev.shinyepo.torquecraft.fluid;

import dev.shinyepo.torquecraft.TorqueCraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class TorqueFluidTypes {
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation LUBRICANT_OVERLAY_RL = new ResourceLocation("misc/in_lubricant");
    public static final ResourceLocation JET_FUEL_OVERLAY_RL = new ResourceLocation(TorqueCraft.MODID,"misc/in_jet_fuel");

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, TorqueCraft.MODID);

    public static final Supplier<FluidType> LUBRICANT_TYPE = register("lubricant_fluid", LUBRICANT_OVERLAY_RL, FluidType.Properties.create().density(50),0xA1FFFF84, new Vector3f(1f,1f,132f/255f));
    public static final Supplier<FluidType> JET_FUEL_TYPE = register("jet_fuel_fluid", JET_FUEL_OVERLAY_RL, FluidType.Properties.create().density(10),0xA1ADD8E6, new Vector3f(173f/255f,216f/255f,230f/255f));

    private static Supplier<FluidType> register(String name,ResourceLocation overlay, FluidType.Properties properties, int tintColor, Vector3f fogColor) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(properties, WATER_STILL_RL, WATER_FLOWING_RL, overlay, tintColor, fogColor));
    }

}
