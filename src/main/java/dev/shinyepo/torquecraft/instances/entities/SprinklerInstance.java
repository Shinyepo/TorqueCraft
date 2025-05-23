package dev.shinyepo.torquecraft.instances.entities;

import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.shinyepo.torquecraft.block.entities.rotary.SprinklerEntity;
import dev.shinyepo.torquecraft.instances.SimpleInstance;
import dev.shinyepo.torquecraft.instances.TorqueInstanceTypes;
import dev.shinyepo.torquecraft.registries.model.partial.TorquePartialModels;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SprinklerInstance extends SimpleInstance<SprinklerEntity> implements SimpleDynamicVisual {
    public SprinklerInstance(VisualizationContext ctx, SprinklerEntity blockEntity, float partialTick) {
        super(TorquePartialModels.SPRINKLER_HEAD, ctx, blockEntity, partialTick);
    }

    @Override
    public void setupInstance() {
        updateBlockState(blockEntity.getBlockState());

        model = instancerProvider().instancer(TorqueInstanceTypes.ROTATING, Models.partial(PartialModel.of(modelLocation))).createInstance();

        model.setup(blockEntity, Direction.Axis.Y)
                .rotateToFace(blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING))
                .setPosition(getVisualPosition())
                .setChanged();
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        if (blockEntity instanceof SprinklerEntity sprinklerEntity) {
            var handler = sprinklerEntity.getFluidTank(null).getFluidInTank(0);
            if (handler.getAmount() > sprinklerEntity.getUsage()) {
                model.setRotationalSpeed(250F).setChanged();
            } else {
                model.setRotationalSpeed(0).setChanged();
            }
        }
    }
}
