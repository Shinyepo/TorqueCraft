package dev.shinyepo.torquecraft.instances.entities;

import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.shinyepo.torquecraft.block.entities.rotary.SprinklerEntity;
import dev.shinyepo.torquecraft.instances.SimpleInstance;
import dev.shinyepo.torquecraft.registries.model.partial.TorquePartialModels;
import net.minecraft.core.Direction;

public class SprinklerInstance extends SimpleInstance<SprinklerEntity> implements SimpleDynamicVisual {
    public SprinklerInstance(VisualizationContext ctx, SprinklerEntity blockEntity, float partialTick) {
        super(TorquePartialModels.SPRINKLER_HEAD, ctx, blockEntity, partialTick);
        model.setRotationAxis(Direction.Axis.Y);
    }
}
