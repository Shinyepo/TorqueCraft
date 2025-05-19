package dev.shinyepo.torquecraft.instances;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.TickableVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.AbstractInstance;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleTickableVisual;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryNetworkDevice;
import dev.shinyepo.torquecraft.registries.model.partial.TorquePartialModels;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class IOInstance<T extends RotaryNetworkDevice<?>> extends AbstractBlockEntityVisual<T> implements SimpleTickableVisual {
    protected final RotaryNetworkDevice<?> blockEntity;
    private final List<OrientedInstance> input = new ArrayList<>();
    private final List<OrientedInstance> output = new ArrayList<>();

    public IOInstance(VisualizationContext ctx, T blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
        this.blockEntity = blockEntity;

        setupInstances();
    }

    private void setupInstances() {
        var sides = blockEntity.getSidesConfig();
        for (SideType side : sides) {
            if (side == SideType.INPUT) {
                input.add(instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(PartialModel.of(TorquePartialModels.HIGHLIGHT_BOX))).createInstance());
            }
            if (side == SideType.OUTPUT) {
                output.add(instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(PartialModel.of(TorquePartialModels.HIGHLIGHT_BOX))).createInstance());
            }
        }
        adjustPosition();
    }

    private void adjustPosition() {
        var sides = blockEntity.getSidesConfig();
        var inputs = 0;
        var outputs = 0;
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] == SideType.INPUT) {
                var instance = input.get(inputs++);
                instance.position(getVisualPosition().relative(Direction.values()[i]))
                        .color(13, 149, 252, 100)
                        .setChanged();
            }
            if (sides[i] == SideType.OUTPUT) {
                var instance = output.get(outputs++);
                instance.position(getVisualPosition().relative(Direction.values()[i]))
                        .color(245, 205, 27, 100)
                        .setChanged();
            }
        }
    }

    @Override
    protected void _delete() {
        if (!input.isEmpty()) input.forEach(AbstractInstance::delete);
        if (!output.isEmpty()) output.forEach(AbstractInstance::delete);
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        if (!input.isEmpty()) input.forEach(consumer);
        if (!output.isEmpty()) output.forEach(consumer);
    }

    @Override
    public void updateLight(float partialTick) {
        relight();
        if (!input.isEmpty()) input.forEach(this::relight);
        if (!output.isEmpty()) output.forEach(this::relight);
    }

    @Override
    public void tick(TickableVisual.Context context) {
        if (blockEntity.getProgress() < 3.0F) {
            adjustPosition();
            blockEntity.updateAnimation();
        } else {
            if (!input.isEmpty()) input.forEach(AbstractInstance::delete);
            if (!output.isEmpty()) output.forEach(AbstractInstance::delete);
        }
    }
}
