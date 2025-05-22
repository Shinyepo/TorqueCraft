package dev.shinyepo.torquecraft.instances.entities;

import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.GearboxEntity;
import dev.shinyepo.torquecraft.instances.MultiModelInstance;

public class Gearbox1_2Instance extends MultiModelInstance<GearboxEntity> implements SimpleDynamicVisual {
    public Gearbox1_2Instance(VisualizationContext ctx, GearboxEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
    }

    public void setupInstances() {
        lastBlockState = blockEntity.getBlockState();
        //TODO: create new models for gearboxes
    }
}
