package dev.shinyepo.torquecraft.menu.furnace;

import dev.shinyepo.torquecraft.TorqueCraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class AlloyFurnaceScreen extends AbstractContainerScreen<AlloyFurnaceContainer> {
    private final ResourceLocation GUI = new ResourceLocation(TorqueCraft.MODID, "textures/gui/alloy_furnace.png");

    public AlloyFurnaceScreen(AlloyFurnaceContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        renderProgressArrow(graphics, relX, relY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
//        super.renderLabels(pGuiGraphics,pMouseX,pMouseY);
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
    }

    private void renderProgressArrow(GuiGraphics graphics, int relX, int relY) {
        int progress = this.menu.getBlockEntity().progress * 22 / this.menu.getBlockEntity().maxProgress;
        graphics.blit(GUI, relX + 80, relY + 34, 176, 0, progress, 16);
    }
}