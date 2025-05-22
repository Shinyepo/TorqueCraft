package dev.shinyepo.torquecraft.instances;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.shinyepo.torquecraft.capabilities.handlers.rotary.IRotaryHandler;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryNetworkDevice;
import dev.shinyepo.torquecraft.instances.types.RotatingInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SimpleInstance<T extends RotaryNetworkDevice<?>> extends IOInstance<T> implements SimpleDynamicVisual {
    private final IRotaryHandler handler;
    public RotatingInstance model;
    public RotaryNetworkDevice<?> blockEntity;
    public ResourceLocation modelLocation;

    private BlockState lastBlockState;

    public SimpleInstance(ResourceLocation model, VisualizationContext ctx, T blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
        this.blockEntity = blockEntity;
        this.handler = this.blockEntity.getRotaryHandler(null);
        lastBlockState = blockEntity.getBlockState();
        modelLocation = model;

        setupInstance();

    }

    public void setupInstance() {
        updateBlockState(blockEntity.getBlockState());

        model = instancerProvider().instancer(TorqueInstanceTypes.VARIABLE_ROTATING, Models.partial(PartialModel.of(modelLocation))).createInstance();

        model.setup(blockEntity)
                .rotateToFace(blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING))
                .setPosition(getVisualPosition())
                .setChanged();
    }

    protected void updateBlockState(BlockState newBlockState) {
        lastBlockState = newBlockState;
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        float angle = (float) ((blockEntity.getAngle() + (handler.getAngular() * 0.01F) * ctx.partialTick()) % 360);
        blockEntity.setAngle(angle);
        model.setRotationalSpeed(angle).setChanged();
    }


    @Override
    public void update(float pt) {
        if (lastBlockState != blockEntity.getBlockState()) {
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
}
