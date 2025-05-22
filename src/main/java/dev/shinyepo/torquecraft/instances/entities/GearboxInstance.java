package dev.shinyepo.torquecraft.instances.entities;

import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.GearboxEntity;
import dev.shinyepo.torquecraft.instances.MultiModelInstance;
import dev.shinyepo.torquecraft.instances.TorqueInstanceTypes;
import dev.shinyepo.torquecraft.instances.types.RotatingInstance;
import dev.shinyepo.torquecraft.registries.model.partial.TorquePartialModels;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class GearboxInstance extends MultiModelInstance<GearboxEntity> implements SimpleDynamicVisual {
    protected RotatingInstance input;
    protected RotatingInstance output;

    public GearboxInstance(VisualizationContext ctx, GearboxEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
    }

    public void setupInstances() {
        lastBlockState = blockEntity.getBlockState();
        input = instancerProvider().instancer(TorqueInstanceTypes.ROTATING, Models.partial(PartialModel.of(TorquePartialModels.INPUT_SHAFT_ROD))).createInstance();
        output = instancerProvider().instancer(TorqueInstanceTypes.ROTATING, Models.partial(PartialModel.of(TorquePartialModels.OUTPUT_SHAFT_ROD))).createInstance();
        var facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        input.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(facing)
                .setChanged();

        output.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(facing)
                .setChanged();

        models.add(input);
        models.add(output);
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        super.beginFrame(ctx);
        //TODO: introduce mode switch
    }
}
