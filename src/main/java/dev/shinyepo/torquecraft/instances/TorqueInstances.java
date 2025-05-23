package dev.shinyepo.torquecraft.instances;

import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import dev.shinyepo.torquecraft.instances.entities.BevelGearsInstance;
import dev.shinyepo.torquecraft.instances.entities.GearboxInstance;
import dev.shinyepo.torquecraft.instances.entities.SprinklerInstance;
import dev.shinyepo.torquecraft.instances.entities.ThreeWayInstance;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import dev.shinyepo.torquecraft.registries.model.partial.TorquePartialModels;

public class TorqueInstances {
    public static void init() {
        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.BEVEL_GEARS_ENTITY.get())
                .factory(BevelGearsInstance::new)
                .apply();

        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.GEARBOX1_2_ENTITY.get())
                .factory(GearboxInstance::new)
                .apply();

        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.GEARBOX1_4_ENTITY.get())
                .factory(GearboxInstance::new)
                .apply();

        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.GEARBOX1_8_ENTITY.get())
                .factory(GearboxInstance::new)
                .apply();

        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.GEARBOX1_16_ENTITY.get())
                .factory(GearboxInstance::new)
                .apply();

        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.GRINDER_ENTITY.get())
                .factory((ctx, blockEntity, partialTick) ->
                        new SimpleInstance<>(TorquePartialModels.GRINDER_SHAFT, ctx, blockEntity, partialTick
                        ))
                .apply();

        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.MECHANICAL_FAN_ENTITY.get())
                .factory((ctx, blockEntity, partialTick) ->
                        new SimpleInstance<>(TorquePartialModels.FAN_BLADE, ctx, blockEntity, partialTick
                        ))
                .apply();

        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.PUMP_ENTITY.get())
                .factory((ctx, blockEntity, partialTick) ->
                        new SimpleInstance<>(TorquePartialModels.INPUT_SHAFT_ROD, ctx, blockEntity, partialTick
                        ))
                .apply();

        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.SHAFT_ENTITY.get())
                .factory((ctx, blockEntity, partialTick) ->
                        new SimpleInstance<>(TorquePartialModels.SHAFT_ROD, ctx, blockEntity, partialTick
                        ))
                .apply();

        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.SPRINKLER_ENTITY.get())
                .factory(SprinklerInstance::new)
                .apply();

        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.STEAM_ENGINE_ENTITY.get())
                .factory((ctx, blockEntity, partialTick) ->
                        new SimpleInstance<>(TorquePartialModels.OUTPUT_SHAFT_ROD, ctx, blockEntity, partialTick
                        ))
                .apply();

        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.THREE_WAY_ENTITY.get())
                .factory(ThreeWayInstance::new)
                .apply();

        SimpleBlockEntityVisualizer
                .builder(TorqueBlockEntities.VACUUM_ENTITY.get())
                .factory((ctx, blockEntity, partialTick) ->
                        new SimpleInstance<>(TorquePartialModels.INPUT_SHAFT_ROD, ctx, blockEntity, partialTick
                        ))
                .apply();

    }
}
