package dev.shinyepo.torquecraft.block.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.BevelGearsEntity;
import dev.shinyepo.torquecraft.renderers.IORenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

public class BevelGearsRenderer implements BlockEntityRenderer<BevelGearsEntity> {
    public BevelGearsRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BevelGearsEntity blockEntity, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay, Vec3 pCameraPos) {
//        renderRotation("shaft_rod",blockEntity,pose,buffer,partialTick,packedLight);

        if (blockEntity.getProgress(partialTick) < 3.0F) {
            IORenderer.renderIOHelper(blockEntity, pose);
        }
    }
}
