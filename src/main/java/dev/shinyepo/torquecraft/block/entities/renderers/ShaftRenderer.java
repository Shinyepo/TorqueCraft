package dev.shinyepo.torquecraft.block.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shinyepo.torquecraft.block.entities.rotary.ShaftEntity;
import dev.shinyepo.torquecraft.renderers.IORenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ShaftRenderer implements BlockEntityRenderer<ShaftEntity> {

    public ShaftRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public boolean shouldRenderOffScreen(ShaftEntity pBlockEntity) {
        return true;
    }

    @Override
    public void render(ShaftEntity blockEntity, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (blockEntity.getProgress(partialTick) < 2.0F) {
            IORenderer.renderIOHelper(blockEntity, pose);
        }
    }
}
