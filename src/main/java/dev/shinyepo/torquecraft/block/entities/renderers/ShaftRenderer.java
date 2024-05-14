package dev.shinyepo.torquecraft.block.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shinyepo.torquecraft.block.entities.rotary.ShaftEntity;
import dev.shinyepo.torquecraft.renderers.IORenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import static dev.shinyepo.torquecraft.renderers.types.RotaryRenderer.renderRotation;

public class ShaftRenderer implements BlockEntityRenderer<ShaftEntity> {
    public ShaftRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ShaftEntity blockEntity, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        renderRotation("shaft_rod",blockEntity,pose,buffer,partialTick,packedLight);

        if (blockEntity.getProgress(partialTick) < 3.0F) {
            IORenderer.renderIOHelper(blockEntity, pose);
        }
    }
}
