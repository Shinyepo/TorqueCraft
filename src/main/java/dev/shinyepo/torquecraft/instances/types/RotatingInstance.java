package dev.shinyepo.torquecraft.instances.types;

import dev.engine_room.flywheel.api.instance.InstanceHandle;
import dev.engine_room.flywheel.api.instance.InstanceType;
import dev.engine_room.flywheel.lib.instance.ColoredLitOverlayInstance;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryNetworkDevice;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RotatingInstance extends ColoredLitOverlayInstance {
    /**
     * Base rotation of the instance, applied before kinetic rotation
     */
    public final Quaternionf rotation = new Quaternionf();
    public byte rotationAxisX;
    public byte rotationAxisY;
    public byte rotationAxisZ;
    public float x;
    public float y;
    public float z;
    /**
     * Speed in degrees per second
     */
    public float rotationalSpeed;

    public int spinDirection = 1;
    /**
     * Offset in degrees
     */
    public float rotationOffset;

    public RotatingInstance(InstanceType<? extends RotatingInstance> type, InstanceHandle handle) {
        super(type, handle);
    }

    public static int colorFromBE() {
        return 0xFFFFFF;
    }

    public RotatingInstance setup(RotaryNetworkDevice<?> blockEntity) {
        var blockState = blockEntity.getBlockState();
        var axis = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis();
        return setup(axis, blockEntity.getRotaryHandler(null).getAngular());
    }

    public RotatingInstance setup(RotaryNetworkDevice<?> blockEntity, Direction.Axis axis) {
        return setup(axis, blockEntity.getRotaryHandler(null).getAngular());
    }

    public RotatingInstance setup(RotaryNetworkDevice<?> blockEntity, float speed) {
        var blockState = blockEntity.getBlockState();
        var axis = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis();
        return setup(axis, speed);
    }

    public RotatingInstance setup(Direction.Axis axis, float speed) {
        return setRotationAxis(axis)
                .setRotationalSpeed(speed * spinDirection);
    }

    public RotatingInstance rotateToFace(Direction.Axis axis) {
        Direction orientation = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        return rotateToFace(orientation);
    }

    public RotatingInstance rotateToFace(Direction from, Direction.Axis axis) {
        Direction orientation = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        return rotateToFace(from, orientation);
    }

    public RotatingInstance rotateToFace(Direction orientation) {
        return rotateToFace(orientation.getStepX(), orientation.getStepY(), orientation.getStepZ());
    }

    public RotatingInstance rotateToFace(Direction from, Direction orientation) {
        return rotateTo(from.getStepX(), from.getStepY(), from.getStepZ(), orientation.getStepX(), orientation.getStepY(), orientation.getStepZ());
    }

    public RotatingInstance rotateToFace(float stepX, float stepY, float stepZ) {
        return rotateTo(0, 0, -1, stepX, stepY, stepZ);
    }

    public RotatingInstance rotateTo(float fromX, float fromY, float fromZ, float toX, float toY, float toZ) {
        rotation.rotateTo(fromX, fromY, fromZ, toX, toY, toZ);
        return this;
    }

    public RotatingInstance setRotationAxis(Direction.Axis axis) {
        Direction orientation = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        return setRotationAxis(orientation.step());
    }

    public RotatingInstance setRotationAxis(Vector3f axis) {
        return setRotationAxis(axis.x(), axis.y(), axis.z());
    }

    public RotatingInstance setRotationAxis(float rotationAxisX, float rotationAxisY, float rotationAxisZ) {
        this.rotationAxisX = (byte) (rotationAxisX * 127);
        this.rotationAxisY = (byte) (rotationAxisY * 127);
        this.rotationAxisZ = (byte) (rotationAxisZ * 127);
        return this;
    }

    public RotatingInstance reverse() {
        spinDirection *= -1;
        return this;
    }

    public RotatingInstance setPosition(Vec3i pos) {
        return setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    public RotatingInstance setPosition(Vector3f pos) {
        return setPosition(pos.x(), pos.y(), pos.z());
    }

    public RotatingInstance setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public RotatingInstance nudge(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public RotatingInstance setColor() {
        colorRgb(colorFromBE());
        return this;
    }

    public RotatingInstance setRotationalSpeed(float rotationalSpeed) {
        this.rotationalSpeed = rotationalSpeed * spinDirection;
        return this;
    }

    public RotatingInstance setRotationOffset(float rotationOffset) {
        this.rotationOffset = rotationOffset;
        return this;
    }

    public RotatingInstance setSpinDirection(int i) {
        spinDirection *= i;
        return this;
    }
}
