package dev.shinyepo.torquecraft.registries;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class TorqueCapabilities {

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, TorqueBlockEntities.GRINDER_ENTITY.get(), (o, direction) ->{
            if (direction == null) {
                return o.getItemHandler().get();
            }
            if (direction == Direction.SOUTH) {
                return o.getOutputItemHandler().get();
            }
            return o.getInputItemHandler().get();
            });

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TorqueBlockEntities.GRINDER_ENTITY.get(),(o, context) -> {
            return o.getFluidTank();
        });
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TorqueBlockEntities.PUMP_ENTITY.get(), (o, dir) -> o.getFluidTank(dir));

//        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TorqueBlockEntities.FLUID_PIPE_ENTITY.get(), (o, dir) -> o.getFluidTank());
//        event.registerBlockEntity(Capabilities.SteamHandler.BLOCK, TorqueBlockEntities.STEAM_ENGINE_ENTITY.get(), (o, dir) -> o.getSteamTank(dir));

    }
}
