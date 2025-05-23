package dev.shinyepo.torquecraft.instances.entities;

import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.GearboxEntity;
import dev.shinyepo.torquecraft.config.RotaryMode;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
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
        input = instancerProvider().instancer(TorqueInstanceTypes.VARIABLE_ROTATING, Models.partial(PartialModel.of(TorquePartialModels.INPUT_SHAFT_ROD))).createInstance();
        output = instancerProvider().instancer(TorqueInstanceTypes.VARIABLE_ROTATING, Models.partial(PartialModel.of(TorquePartialModels.OUTPUT_SHAFT_ROD))).createInstance();
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
        if (blockEntity instanceof GearboxEntity gearboxEntity) {
            var mode = lastBlockState.getValue(TorqueAttributes.MODE);
            var ratio = gearboxEntity.getRatio();
            var inputSpeed = gearboxEntity.getInAngular();

            float inputAngle = (float) ((blockEntity.getAngle() + (inputSpeed * 0.01F) * ctx.partialTick()) % 360);
            blockEntity.setAngle(inputAngle);

            float outputSpeed;
            if (mode == RotaryMode.ANGULAR) {
                outputSpeed = inputSpeed * ratio;
            } else {
                outputSpeed = inputSpeed / ratio;
            }

            var outputAngle = (float) ((blockEntity.getOutputAngle() + (outputSpeed * 0.01F) * ctx.partialTick()) % 360);
            blockEntity.setOutputAngle(outputAngle);

            input.setRotationalSpeed(inputAngle)
                    .setChanged();

            output.setRotationalSpeed(outputAngle)
                    .setChanged();
        }
        //TODO: introduce mode switch
    }
}
