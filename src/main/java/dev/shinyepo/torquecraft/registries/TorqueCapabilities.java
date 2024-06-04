package dev.shinyepo.torquecraft.registries;

import dev.shinyepo.torquecraft.capabilities.TorqueCustomCapabilities;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class TorqueCapabilities {

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, TorqueBlockEntities.GRINDER_ENTITY.get(), (o, direction) ->{
            if (direction == null) {
                return o.getItemHandler().get();
            }
            if (direction == Direction.DOWN) {
                return o.getOutputItemHandler().get();
            }
                return o.getInputItemHandler().get();
        });
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TorqueBlockEntities.GRINDER_ENTITY.get(), (o, direction) -> {
            if (direction != Direction.UP && direction != Direction.NORTH) {
                return o.getFluidTank();
            }
            return null;
        });

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, TorqueBlockEntities.ALLOY_FURNACE_ENTITY.get(), (o, dir) -> o.getItemHandler(dir));

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TorqueBlockEntities.GRINDER_ENTITY.get(),(o, context) -> o.getFluidTank());
        event.registerBlockEntity(TorqueCustomCapabilities.ROTARY_HANDLER_BLOCK, TorqueBlockEntities.GRINDER_ENTITY.get(),(o, dir) -> o.getRotaryHandler(dir));

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TorqueBlockEntities.PUMP_ENTITY.get(), (o, dir) -> o.getFluidTank(dir));

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TorqueBlockEntities.FLUID_TANK_ENTITY.get(), (o, dir) -> o.getFluidTank(dir));

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TorqueBlockEntities.FLUID_PIPE_ENTITY.get(), (o, dir) -> o.getFluidTank(dir));

        event.registerBlockEntity(TorqueCustomCapabilities.ROTARY_HANDLER_BLOCK, TorqueBlockEntities.STEAM_ENGINE_ENTITY.get(), (o, dir) -> o.getRotaryHandler(dir));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TorqueBlockEntities.STEAM_ENGINE_ENTITY.get(), (o, dir) -> o.getFluidTank(dir));

        event.registerBlockEntity(TorqueCustomCapabilities.ROTARY_HANDLER_BLOCK, TorqueBlockEntities.SHAFT_ENTITY.get(), (o,dir) -> o.getRotaryHandler(dir));
        event.registerBlockEntity(TorqueCustomCapabilities.ROTARY_HANDLER_BLOCK, TorqueBlockEntities.MECHANICAL_FAN_ENTITY.get(), (o,dir) -> o.getRotaryHandler(dir));
    }
}
