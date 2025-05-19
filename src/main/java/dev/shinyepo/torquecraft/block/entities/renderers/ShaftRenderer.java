package dev.shinyepo.torquecraft.block.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.ShaftEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import static dev.shinyepo.torquecraft.renderers.types.RotaryRenderer.renderMonitor;

public class ShaftRenderer implements BlockEntityRenderer<ShaftEntity> {
    private final Font font;

    public ShaftRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
    }

    @Override
    public void render(ShaftEntity blockEntity, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (blockEntity.hasMonitor()) {
            renderMonitor(blockEntity, pose, buffer, packedLight, this.font);
        }

    }
}
