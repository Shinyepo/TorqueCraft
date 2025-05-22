package dev.shinyepo.torquecraft.instances.entities;

import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.ThreeWayEntity;
import dev.shinyepo.torquecraft.instances.MultiModelInstance;
import dev.shinyepo.torquecraft.instances.TorqueInstanceTypes;
import dev.shinyepo.torquecraft.registries.model.partial.TorquePartialModels;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ThreeWayInstance extends MultiModelInstance<ThreeWayEntity> implements SimpleDynamicVisual {
    public ThreeWayInstance(VisualizationContext ctx, ThreeWayEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
    }

    public void setupInstances() {
        lastBlockState = blockEntity.getBlockState();
        var down = instancerProvider().instancer(TorqueInstanceTypes.ROTATING, Models.partial(PartialModel.of(TorquePartialModels.THREE_WAY_DOWN))).createInstance();
        var output = instancerProvider().instancer(TorqueInstanceTypes.ROTATING, Models.partial(PartialModel.of(TorquePartialModels.THREE_WAY_SIDE))).createInstance();
        var secondInput = instancerProvider().instancer(TorqueInstanceTypes.ROTATING, Models.partial(PartialModel.of(TorquePartialModels.THREE_WAY_SIDE))).createInstance();
        var firstInput = instancerProvider().instancer(TorqueInstanceTypes.ROTATING, Models.partial(PartialModel.of(TorquePartialModels.THREE_WAY_SIDE))).createInstance();
        var facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        down.setup(blockEntity, Direction.Axis.Y)
                .setPosition(getVisualPosition())
                .setSpinDirection(facing == Direction.SOUTH || facing == Direction.EAST ? -1 : 1)
                .setChanged();

        firstInput.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(facing.getOpposite())
                .setChanged();

        secondInput.setup(blockEntity, facing.getClockWise().getAxis())
                .setPosition(getVisualPosition())
                .rotateToFace(facing.getClockWise())
                .setSpinDirection(facing == Direction.SOUTH || facing == Direction.NORTH ? 1 : -1)
                .setChanged();

        output.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(facing)
                .setSpinDirection(-1)
                .setChanged();

        models.add(down);
        models.add(output);
        models.add(secondInput);
        models.add(firstInput);
    }
}
