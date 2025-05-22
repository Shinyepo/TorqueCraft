package dev.shinyepo.torquecraft.instances;

import dev.engine_room.flywheel.api.instance.InstanceType;
import dev.engine_room.flywheel.api.layout.FloatRepr;
import dev.engine_room.flywheel.api.layout.IntegerRepr;
import dev.engine_room.flywheel.api.layout.LayoutBuilder;
import dev.engine_room.flywheel.lib.instance.SimpleInstanceType;
import dev.engine_room.flywheel.lib.util.ExtraMemoryOps;
import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.instances.types.RotatingInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.lwjgl.system.MemoryUtil;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

@OnlyIn(Dist.CLIENT)
public class TorqueInstanceTypes {
    public static final InstanceType<RotatingInstance> VARIABLE_ROTATING = SimpleInstanceType.builder(RotatingInstance::new)
            .cullShader(fromNamespaceAndPath(TorqueCraft.MODID, "instance/cull/rotating.glsl"))
            .vertexShader(fromNamespaceAndPath(TorqueCraft.MODID, "instance/variable_rotating.vert"))
            .layout(LayoutBuilder.create()
                    .vector("color", FloatRepr.NORMALIZED_UNSIGNED_BYTE, 4)
                    .vector("light", IntegerRepr.SHORT, 2)
                    .vector("overlay", IntegerRepr.SHORT, 2)
                    .vector("rotation", FloatRepr.FLOAT, 4)
                    .vector("pos", FloatRepr.FLOAT, 3)
                    .scalar("speed", FloatRepr.FLOAT)
                    .scalar("offset", FloatRepr.FLOAT)
                    .vector("axis", FloatRepr.NORMALIZED_BYTE, 3)
                    .build())
            .writer((ptr, instance) -> {
                MemoryUtil.memPutByte(ptr, instance.red);
                MemoryUtil.memPutByte(ptr + 1, instance.green);
                MemoryUtil.memPutByte(ptr + 2, instance.blue);
                MemoryUtil.memPutByte(ptr + 3, instance.alpha);
                ExtraMemoryOps.put2x16(ptr + 4, instance.light);
                ExtraMemoryOps.put2x16(ptr + 8, instance.overlay);
                ExtraMemoryOps.putQuaternionf(ptr + 12, instance.rotation);
                MemoryUtil.memPutFloat(ptr + 28, instance.x);
                MemoryUtil.memPutFloat(ptr + 32, instance.y);
                MemoryUtil.memPutFloat(ptr + 36, instance.z);
                MemoryUtil.memPutFloat(ptr + 40, instance.rotationalSpeed);
                MemoryUtil.memPutFloat(ptr + 44, instance.rotationOffset);
                MemoryUtil.memPutByte(ptr + 48, instance.rotationAxisX);
                MemoryUtil.memPutByte(ptr + 49, instance.rotationAxisY);
                MemoryUtil.memPutByte(ptr + 50, instance.rotationAxisZ);
            })
            .build();

    public static final InstanceType<RotatingInstance> ROTATING = SimpleInstanceType.builder(RotatingInstance::new)
            .cullShader(fromNamespaceAndPath(TorqueCraft.MODID, "instance/cull/rotating.glsl"))
            .vertexShader(fromNamespaceAndPath(TorqueCraft.MODID, "instance/rotating.vert"))
            .layout(LayoutBuilder.create()
                    .vector("color", FloatRepr.NORMALIZED_UNSIGNED_BYTE, 4)
                    .vector("light", IntegerRepr.SHORT, 2)
                    .vector("overlay", IntegerRepr.SHORT, 2)
                    .vector("rotation", FloatRepr.FLOAT, 4)
                    .vector("pos", FloatRepr.FLOAT, 3)
                    .scalar("speed", FloatRepr.FLOAT)
                    .scalar("offset", FloatRepr.FLOAT)
                    .vector("axis", FloatRepr.NORMALIZED_BYTE, 3)
                    .build())
            .writer((ptr, instance) -> {
                MemoryUtil.memPutByte(ptr, instance.red);
                MemoryUtil.memPutByte(ptr + 1, instance.green);
                MemoryUtil.memPutByte(ptr + 2, instance.blue);
                MemoryUtil.memPutByte(ptr + 3, instance.alpha);
                ExtraMemoryOps.put2x16(ptr + 4, instance.light);
                ExtraMemoryOps.put2x16(ptr + 8, instance.overlay);
                ExtraMemoryOps.putQuaternionf(ptr + 12, instance.rotation);
                MemoryUtil.memPutFloat(ptr + 28, instance.x);
                MemoryUtil.memPutFloat(ptr + 32, instance.y);
                MemoryUtil.memPutFloat(ptr + 36, instance.z);
                MemoryUtil.memPutFloat(ptr + 40, instance.rotationalSpeed);
                MemoryUtil.memPutFloat(ptr + 44, instance.rotationOffset);
                MemoryUtil.memPutByte(ptr + 48, instance.rotationAxisX);
                MemoryUtil.memPutByte(ptr + 49, instance.rotationAxisY);
                MemoryUtil.memPutByte(ptr + 50, instance.rotationAxisZ);
            })
            .build();


}
