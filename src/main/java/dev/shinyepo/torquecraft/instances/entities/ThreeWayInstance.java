package dev.shinyepo.torquecraft.instances.entities;

import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.ThreeWayEntity;
import dev.shinyepo.torquecraft.instances.MultiModelInstance;
import dev.shinyepo.torquecraft.instances.TorqueInstanceTypes;
import dev.shinyepo.torquecraft.registries.model.partial.TorquePartialModels;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ThreeWayInstance extends MultiModelInstance<ThreeWayEntity> implements SimpleDynamicVisual {
    public ThreeWayInstance(VisualizationContext ctx, ThreeWayEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
    }

    public void setupInstances() {
        lastBlockState = blockEntity.getBlockState();
        var input = instancerProvider().instancer(TorqueInstanceTypes.VARIABLE_ROTATING, Models.partial(PartialModel.of(TorquePartialModels.THREE_WAY_INPUT))).createInstance();
        var side = instancerProvider().instancer(TorqueInstanceTypes.VARIABLE_ROTATING, Models.partial(PartialModel.of(TorquePartialModels.THREE_WAY_SIDE))).createInstance();
        var facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        input.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(facing)
                .setChanged();

        side.setup(blockEntity, facing.getClockWise().getAxis())
                .setPosition(getVisualPosition())
                .rotateToFace(facing)
                .reverse()
                .setChanged();

        models.add(input);
        models.add(side);
    }
}
