package dev.shinyepo.torquecraft.instances.entities;

import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.BevelGearsEntity;
import dev.shinyepo.torquecraft.instances.MultiModelInstance;
import dev.shinyepo.torquecraft.instances.TorqueInstanceTypes;
import dev.shinyepo.torquecraft.registries.model.partial.TorquePartialModels;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BevelGearsInstance extends MultiModelInstance<BevelGearsEntity> implements SimpleDynamicVisual {
    public BevelGearsInstance(VisualizationContext ctx, BevelGearsEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
    }

    public void setupInstances() {
        lastBlockState = blockEntity.getBlockState();
        var input = instancerProvider().instancer(TorqueInstanceTypes.ROTATING, Models.partial(PartialModel.of(TorquePartialModels.BEVEL_INPUT))).createInstance();
        var output = instancerProvider().instancer(TorqueInstanceTypes.ROTATING, Models.partial(PartialModel.of(TorquePartialModels.BEVEL_OUTPUT))).createInstance();
        var facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        input.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(facing)
                .setChanged();

        output.setup(blockEntity, Direction.Axis.Y)
                .setPosition(getVisualPosition())
                .setChanged();

        models.add(input);
        models.add(output);
    }
}
