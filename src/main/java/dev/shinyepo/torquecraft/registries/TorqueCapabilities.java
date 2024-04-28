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
    }
}
