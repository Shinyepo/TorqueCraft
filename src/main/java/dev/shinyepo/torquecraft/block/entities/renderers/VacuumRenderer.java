package dev.shinyepo.torquecraft.block.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shinyepo.torquecraft.block.entities.rotary.VacuumEntity;
import dev.shinyepo.torquecraft.renderers.IORenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class VacuumRenderer implements BlockEntityRenderer<VacuumEntity> {
    public VacuumRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(VacuumEntity blockEntity, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (blockEntity.getProgress(partialTick) < 3.0F) {
            IORenderer.renderIOHelper(blockEntity, pose);
        }
    }
}
