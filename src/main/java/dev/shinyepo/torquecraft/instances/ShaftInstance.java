package dev.shinyepo.torquecraft.instances;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.ShaftEntity;
import dev.shinyepo.torquecraft.instances.types.RotatingInstance;
import dev.shinyepo.torquecraft.registries.model.partial.TorquePartialModels;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ShaftInstance extends IOInstance<ShaftEntity> implements SimpleDynamicVisual {
    final Direction facing;
    private final BlockState blockState;
    private RotatingInstance model;

    public ShaftInstance(VisualizationContext ctx, ShaftEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
        blockState = blockEntity.getBlockState();
        facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        setupInstance();
    }

    public void setupInstance() {
        model = instancerProvider().instancer(TorqueInstanceTypes.ROTATING, Models.partial(PartialModel.of(TorquePartialModels.SHAFT_ROD))).createInstance();

        model.setup(blockEntity)
                .rotateToFace(facing)
                .setPosition(getVisualPosition())
                .setChanged();
    }

    @Override
    public void update(float pt) {
        super.update(pt);
        if (blockState != blockEntity.getBlockState()) {
            model.delete();
            setupInstance();
        } else {
            model.setup(blockEntity)
                    .setChanged();
        }
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        consumer.accept(model);
    }

    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);
        relight(model);
    }

    @Override
    protected void _delete() {
        super._delete();
        model.delete();
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        var speed = blockEntity.getRotaryHandler(null).getAngular();
        model.setRotationalSpeed(speed).setChanged();

    }
}
