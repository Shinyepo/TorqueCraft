package dev.shinyepo.torquecraft.menu;

import dev.shinyepo.torquecraft.TorqueCraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class GrinderScreen extends AbstractContainerScreen<GrinderContainer> {

    private final ResourceLocation GUI = new ResourceLocation(TorqueCraft.MODID, "textures/gui/grinder.png");

    public GrinderScreen(GrinderContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        renderProgressArrow(graphics, relX, relY);
    }

    private void renderProgressArrow(GuiGraphics graphics, int relX, int relY) {
        int progress = this.menu.grinderEntity.progress * 22 / this.menu.grinderEntity.maxProgress;
        graphics.blit(GUI, relX+80, relY+34,176,0,progress,16);
    }
}