package dev.shinyepo.torquecraft.block.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shinyepo.torquecraft.block.entities.rotary.SteamEngineEntity;
import dev.shinyepo.torquecraft.renderers.IORenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import static dev.shinyepo.torquecraft.renderers.types.RotaryRenderer.renderRotation;

public class SteamEngineRenderer implements BlockEntityRenderer<SteamEngineEntity> {

    public SteamEngineRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public boolean shouldRenderOffScreen(SteamEngineEntity pBlockEntity) {
        return true;
    }

    @Override
    public void render(SteamEngineEntity blockEntity, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        renderRotation("hsla_short_shaft_rod", blockEntity, pose, buffer, partialTick, packedLight);

        if (blockEntity.getProgress(partialTick) < 3.0F) {
            IORenderer.renderIOHelper(blockEntity, pose);
        }
    }
}
