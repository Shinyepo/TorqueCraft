package dev.shinyepo.torquecraft.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.menu.renderer.FluidTankRenderer;
import dev.shinyepo.torquecraft.utils.MouseUtil;
import dev.shinyepo.torquecraft.utils.TorqueFluidTank;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.fluids.FluidStack;

import java.awt.*;
import java.util.Optional;


public class GrinderScreen extends AbstractContainerScreen<GrinderContainer> {

    private final ResourceLocation GUI = new ResourceLocation(TorqueCraft.MODID, "textures/gui/grinder.png");
    private FluidTankRenderer tankRenderer;

    public GrinderScreen(GrinderContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        assignTankRenderer();
    }

    private void assignTankRenderer() {
        tankRenderer = new FluidTankRenderer(this.menu.grinderEntity.fluidCapacity, true, 16, 61);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        renderProgressArrow(graphics, relX, relY);
        tankRenderer.render(graphics.pose(), relX+152, relY+13,this.menu.grinderEntity.getFluidTank().getFluid());
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        renderFluidTooltips(pGuiGraphics, pMouseX, pMouseY, relX, relY);
//        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
    }

    private void renderFluidTooltips(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int relX, int relY) {
        if (mouseAboveArea(pMouseX, pMouseY, relX, relY, 152, 13)){
            FluidStack fluid = this.menu.grinderEntity.getFluidTank().getFluid();
            pGuiGraphics.renderTooltip(this.font,tankRenderer.getTooltip(fluid, TooltipFlag.NORMAL), Optional.empty(), pMouseX - relX, pMouseY - relY);
        }
    }

    private boolean mouseAboveArea(int pMouseX, int pMouseY, int relX, int relY, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, relX + offsetX, relY + offsetY, tankRenderer.getWidth(), tankRenderer.getHeight());
    }

    private void renderProgressArrow(GuiGraphics graphics, int relX, int relY) {
        int progress = this.menu.grinderEntity.progress * 22 / this.menu.grinderEntity.maxProgress;
        graphics.blit(GUI, relX+80, relY+34,176,0,progress,16);
    }
}