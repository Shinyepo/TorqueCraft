package dev.shinyepo.torquecraft.instances;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.AbstractInstance;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.shinyepo.torquecraft.capabilities.handlers.rotary.IRotaryHandler;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryNetworkDevice;
import dev.shinyepo.torquecraft.instances.types.RotatingInstance;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.function.Consumer;

public abstract class MultiModelInstance<T extends RotaryNetworkDevice<?>> extends IOInstance<T> implements SimpleDynamicVisual {
    private final IRotaryHandler handler;
    public HashSet<RotatingInstance> models = new HashSet<>();
    public RotaryNetworkDevice<?> blockEntity;

    protected BlockState lastBlockState;

    public MultiModelInstance(VisualizationContext ctx, T blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
        this.blockEntity = blockEntity;
        this.handler = this.blockEntity.getRotaryHandler(null);
        lastBlockState = blockEntity.getBlockState();

        setupInstances();

    }

    public abstract void setupInstances();

    protected IRotaryHandler getHandler() {
        return this.handler;
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        float angle = (float) ((blockEntity.getAngle() + (handler.getAngular() * 0.01F) * ctx.partialTick()) % 360);
        blockEntity.setAngle(angle);

        for (RotatingInstance model : models) {
            model.setRotationalSpeed(angle).setChanged();
        }
    }


    @Override
    public void update(float pt) {
        if (lastBlockState != blockEntity.getBlockState()) {
            models.forEach(AbstractInstance::delete);
            setupInstances();
        } else {
            models.forEach(model -> model.setup(blockEntity).setChanged());
        }
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        models.forEach(consumer);
    }

    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);
        models.forEach(this::relight);
    }

    @Override
    protected void _delete() {
        super._delete();
        models.forEach(AbstractInstance::delete);
    }
}
