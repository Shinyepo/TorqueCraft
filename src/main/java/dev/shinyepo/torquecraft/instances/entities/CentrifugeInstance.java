package dev.shinyepo.torquecraft.instances.entities;

import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.shinyepo.torquecraft.block.entities.rotary.CentrifugeEntity;
import dev.shinyepo.torquecraft.instances.SimpleInstance;
import dev.shinyepo.torquecraft.instances.TorqueInstanceTypes;
import dev.shinyepo.torquecraft.registries.model.partial.TorquePartialModels;
import net.minecraft.core.Direction;

public class CentrifugeInstance extends SimpleInstance<CentrifugeEntity> implements SimpleDynamicVisual {
    public CentrifugeInstance(VisualizationContext ctx, CentrifugeEntity blockEntity, float partialTick) {
        super(TorquePartialModels.CENTRIFUGE, ctx, blockEntity, partialTick);
    }

    @Override
    public void setupInstance() {
        updateBlockState(blockEntity.getBlockState());

        model = instancerProvider().instancer(TorqueInstanceTypes.VARIABLE_ROTATING, Models.partial(PartialModel.of(modelLocation))).createInstance();

        model.setup(blockEntity, Direction.Axis.Y)
                .setPosition(getVisualPosition())
                .setChanged();
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        super.beginFrame(ctx);
//        float angle = (float) ((blockEntity.getAngle() + (1 * 0.01F) * ctx.partialTick()) % 360);
//        blockEntity.setAngle(angle);
//        model.setRotationalSpeed(angle).setChanged();
    }
}
