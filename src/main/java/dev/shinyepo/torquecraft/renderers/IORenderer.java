package dev.shinyepo.torquecraft.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryNetworkDevice;
import dev.shinyepo.torquecraft.renderers.types.TorqueRenders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.joml.Vector4d;

import java.util.HashMap;
import java.util.Map;

public class IORenderer {

    private static Map<Direction, SideType> getIOs(BlockEntity entity) {
        Map<Direction, SideType> outputs = new HashMap<>();
        if (entity instanceof RotaryNetworkDevice<?> device) {
            SideType[] sidesConfig = device.getSidesConfig();
            for (int i = 0; i < sidesConfig.length; i++) {
                if (sidesConfig[i] != SideType.NONE) {
                    outputs.put(Direction.values()[i], sidesConfig[i]);
                }
            }
        }
        return outputs;
    }

    public static void renderIOHelper(BlockEntity blockEntity, PoseStack pose) {
        double drawRadius = 0.5D;
        double minCorner = 0.5D - drawRadius;
        double maxCorner = 0.5D + drawRadius;
        Vector4d vector4dMin = new Vector4d(minCorner, minCorner, minCorner, 1.0D);
        Vector4d vector4dMax = new Vector4d(maxCorner, maxCorner, maxCorner, 1.0D);

        BlockPos.MutableBlockPos worldSpot = new BlockPos.MutableBlockPos(blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ());

//        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//        RenderSystem.enableBlend();
//        RenderSystem.enableCull();
//        RenderSystem.enableDepthTest();

        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        pose.pushPose();
        if (!Minecraft.getInstance().levelRenderer.getFrustum().isVisible(new AABB(
                worldSpot.getX(),
                worldSpot.getY(),
                worldSpot.getZ(),
                worldSpot.getX() + maxCorner,
                worldSpot.getY() + maxCorner,
                worldSpot.getZ() + maxCorner))) {
            pose.popPose();
            bufferbuilder.buildOrThrow().close();
//            RenderSystem.disableDepthTest();
//            RenderSystem.disableCull();
//            RenderSystem.disableBlend();
            return;
        }
        Map<Direction, SideType> ios = getIOs(blockEntity);

        ios.forEach((direction, type) -> {
            BlockPos pos = blockEntity.getBlockPos().relative(direction.getOpposite());
            float r = 0f;
            float g = 0f;
            float b = 0f;
            if (type == SideType.INPUT) {
                r = 0f;
                g = 137f;
                b = 255f;
            } else if (type == SideType.OUTPUT) {
                r = 252f;
                g = 168f;
                b = 0f;
            }
            pose.pushPose();
            pose.translate(-pos.getX(), -pos.getY(), -pos.getZ());

            TorqueRenders.renderQuadBox(bufferbuilder, pose.last().pose(),
                    (float) (vector4dMin.x() + worldSpot.getX()),
                    (float) (vector4dMin.y() + worldSpot.getY()),
                    (float) (vector4dMin.z() + worldSpot.getZ()),
                    (float) (vector4dMax.x() + worldSpot.getX()),
                    (float) (vector4dMax.y() + worldSpot.getY()),
                    (float) (vector4dMax.z() + worldSpot.getZ()),
                    (int) r,
                    (int) g,
                    (int) b,
                    (int) (40f));
            pose.popPose();
        });

        pose.popPose();
        bufferbuilder.buildOrThrow().close();
//        RenderSystem.disableDepthTest();
//        RenderSystem.disableCull();
//        RenderSystem.disableBlend();
    }
}
