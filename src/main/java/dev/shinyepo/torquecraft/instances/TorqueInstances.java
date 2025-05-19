package dev.shinyepo.torquecraft.instances;

import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;

public class TorqueInstances {
    public static void init() {
        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.SHAFT_ENTITY.get())
                .neverSkipVanillaRender()
                .factory(ShaftInstance::new)
                .apply();


    }
}
