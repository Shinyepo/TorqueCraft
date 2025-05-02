package dev.shinyepo.torquecraft.renderers.types;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.ShaftEntity;
import dev.shinyepo.torquecraft.capabilities.TorqueCustomCapabilities;
import dev.shinyepo.torquecraft.capabilities.handlers.rotary.IRotaryHandler;
import dev.shinyepo.torquecraft.factory.rotary.render.IRotational;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.neoforged.neoforge.model.data.ModelData;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class RotaryRenderer {
    public static void renderRotation(String modelPath, BlockEntity blockEntity, PoseStack pose, MultiBufferSource buffer, float partialTick, int packedLight) {
        IRotaryHandler handler = Minecraft.getInstance().level.getCapability(TorqueCustomCapabilities.ROTARY_HANDLER_BLOCK,blockEntity.getBlockPos(),blockEntity.getBlockState(),blockEntity,null);
        if (handler == null) {
            return;
        }
        Direction direction = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        BlockStateModel model = Minecraft.getInstance().getModelManager().getStandaloneModel(new StandaloneModelKey<>(fromNamespaceAndPath(TorqueCraft.MODID, "block/partial/" + modelPath)));

        RenderType type = RenderType.solid();
        pose.pushPose();
        pose.translate(0.5F, 0.5F, 0.5F);
        double angle = 0;
        if (blockEntity instanceof IRotational rs) {
            angle = (rs.getAngle() + (handler.getAngular() / 10) * partialTick) % 360;
        }
        pose.mulPose(new Quaternionf().rotateAxis((float)Math.toRadians(angle % 360), new Vector3f(-direction.getStepX(), 0, -direction.getStepZ())));
        pose.mulPose(direction.getRotation());

        ModelBlockRenderer.renderModel(pose.last(), buffer, model,1.0F,1.0F,1.0F, packedLight,OverlayTexture.NO_OVERLAY,Minecraft.getInstance().level,blockEntity.getBlockPos(),blockEntity.getBlockState());
//        ModelBlockRenderer.renderModel(pose.last(), buffer.getBuffer(type), null, model, 1F, 1F, 1F, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, type);
        pose.popPose();
    }

    public static void renderMonitor(ShaftEntity blockEntity, PoseStack pose, MultiBufferSource buffer, int packedLight, Font font) {
        ModelBlockRenderer renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
        Direction direction = blockEntity.getMonitorFacing();
        if (direction == null) return;
        BlockStateModel model = Minecraft.getInstance().getModelManager().getStandaloneModel(new StandaloneModelKey<>(fromNamespaceAndPath(TorqueCraft.MODID, "block/partial/rotary_monitor")));

        RenderType type = RenderType.solid();
        pose.pushPose();

        pose.translate(0.5F, 0.5F, 0.5F);
        pose.mulPose(direction.getRotation());

        ModelBlockRenderer.renderModel(pose.last(), buffer, model,1.0F,1.0F,1.0F, packedLight,OverlayTexture.NO_OVERLAY,Minecraft.getInstance().level,blockEntity.getBlockPos(),blockEntity.getBlockState());
//        renderer.renderModel(pose.last(), buffer.getBuffer(type), null, model, 0F, 0F, 0F, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, type);
        pose.popPose();
        pose.pushPose();

        pose.translate(0.5F, 0.5F, 0.5F);
        var offset = direction.getAxis() == Direction.Axis.X ? 180F : 0F;
        pose.mulPose(Axis.YP.rotationDegrees((direction.toYRot() + offset) % 360F));
        pose.translate(-0.5F, -0.5F, -0.5F);

        Vec3 pos = new Vec3(0F, 1F, 1.005F);
        float f = 0.015625F * 0.5666667F;
        pose.translate(pos.x, pos.y, pos.z);
        pose.scale(f, -f, f);
        String Torque = blockEntity.getRotaryHandler(null).getTorque() + " Nm";
        String Angular = blockEntity.getRotaryHandler(null).getAngular() + " rad/s";
        List<String> renderText = List.of("Torque", Torque, "Speed", Angular);
        for (int i = 0; i < 4; i++) {
            int spacing = i > 1 ? 12 * i + 8 : 12 * i;
            int color = i == 1 || i == 3 ? -1761607681 : -1;
            font.drawInBatch(renderText.get(i),
                    (float) 16,
                    (float) 20 + spacing,
                    color,
                    false,
                    pose.last().pose(),
                    buffer,
                    Font.DisplayMode.POLYGON_OFFSET,
                    0,
                    15728880);

        }
        pose.popPose();
    }
}
