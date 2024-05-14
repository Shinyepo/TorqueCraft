package dev.shinyepo.torquecraft.renderers.types;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.capabilities.TorqueCustomCapabilities;
import dev.shinyepo.torquecraft.capabilities.handlers.IRotaryHandler;
import dev.shinyepo.torquecraft.factory.rotary.RotarySource;
import dev.shinyepo.torquecraft.factory.rotary.RotaryTransmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RotaryRenderer {
    public static void renderRotation(String modelPath, BlockEntity blockEntity, PoseStack pose, MultiBufferSource buffer, float partialTick, int packedLight) {
        IRotaryHandler handler = Minecraft.getInstance().level.getCapability(TorqueCustomCapabilities.ROTARY_HANDLER_BLOCK,blockEntity.getBlockPos(),blockEntity.getBlockState(),blockEntity,null);
        if (handler == null) {
            return;
        }
        ModelBlockRenderer renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
        Direction direction = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(TorqueCraft.MODID, "block/partial/"+modelPath));

        RenderType type = RenderType.solid();
        pose.pushPose();
        pose.translate(0.5F, 0.5F, 0.5F);
        double angle = 0;
        if (blockEntity instanceof RotarySource rs) {
            angle = -(rs.getAngle() + (handler.getAngular() / 10) * partialTick) % 360;
        } else if (blockEntity instanceof RotaryTransmitter rt) {
            angle = (rt.getAngle() + (handler.getAngular() / 10) * partialTick) % 360;
        }
        pose.mulPose(new Quaternionf().rotateAxis((float)Math.toRadians(angle % 360), new Vector3f(-direction.getStepX(), 0, -direction.getStepZ())));
        pose.mulPose(direction.getRotation());

        renderer.renderModel(pose.last(), buffer.getBuffer(type), null, model, 1F, 1F, 1F, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, type);
        pose.popPose();
    }


}
