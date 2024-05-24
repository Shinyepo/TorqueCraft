package dev.shinyepo.torquecraft.block.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shinyepo.torquecraft.block.entities.rotary.MechanicalFanEntity;
import dev.shinyepo.torquecraft.renderers.IORenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import static dev.shinyepo.torquecraft.renderers.types.RotaryRenderer.renderRotation;

public class MechanicalFanRenderer implements BlockEntityRenderer<MechanicalFanEntity> {
    public MechanicalFanRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(MechanicalFanEntity blockEntity, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        renderRotation("fan_blade",blockEntity,pose,buffer,partialTick,packedLight);

        if (blockEntity.getProgress(partialTick) < 3.0F) {
            IORenderer.renderIOHelper(blockEntity, pose);
        }
    }
}
