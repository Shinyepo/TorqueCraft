package dev.shinyepo.torquecraft.menu.grinder;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.config.ClientConfig;
import dev.shinyepo.torquecraft.menu.renderer.FluidTankRenderer;
import dev.shinyepo.torquecraft.menu.renderer.RotaryInfoRenderer;
import dev.shinyepo.torquecraft.utils.MouseUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Optional;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;


public class GrinderScreen extends AbstractContainerScreen<GrinderContainer> {
    private final ResourceLocation GUI = fromNamespaceAndPath(TorqueCraft.MODID, "textures/gui/grinder.png");
    private FluidTankRenderer tankRenderer;
    private RotaryInfoRenderer rotaryRenderer;

    public GrinderScreen(GrinderContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = this.imageHeight - 94;
        this.titleLabelX = 30;
    }

    @Override
    protected void init() {
        super.init();
        assignTankRenderer();
        rotaryRenderer = new RotaryInfoRenderer(ClientConfig.GRINDER, this.font);
    }


    private void assignTankRenderer() {
        tankRenderer = new FluidTankRenderer(this.menu.getBlockEntity().getFluidTank().getCapacity(), true, 16, 61);
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
        tankRenderer.render(graphics.pose(), relX + 152, relY + 13, this.menu.getFluidStack());
        rotaryRenderer.render(graphics,this.menu.getBlockEntity().getRotaryHandler(null),relX, relY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
//        super.renderLabels(pGuiGraphics,pMouseX,pMouseY);
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        renderFluidTooltips(pGuiGraphics, pMouseX, pMouseY, relX, relY);
        rotaryRenderer.renderRotaryTooltips(pGuiGraphics,this.menu.getBlockEntity().getRotaryHandler(null), pMouseX,pMouseY,relX,relY);
    }

    private void renderFluidTooltips(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int relX, int relY) {
        if (mouseOver(pMouseX, pMouseY, relX, relY, 152, 13)) {
            FluidStack fluid = this.menu.getFluidStack();
            pGuiGraphics.renderTooltip(this.font, tankRenderer.getTooltip(fluid, TooltipFlag.NORMAL), Optional.empty(), pMouseX - relX, pMouseY - relY);
        }
    }

    private boolean mouseOver(int pMouseX, int pMouseY, int relX, int relY, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, relX + offsetX, relY + offsetY, tankRenderer.getWidth(), tankRenderer.getHeight());
    }

    private void renderProgressArrow(GuiGraphics graphics, int relX, int relY) {
        var maxProgress = this.menu.getMaxProgress();
        if (maxProgress < 1) return;
        int progress = this.menu.getProgress() * 22 / this.menu.getMaxProgress();
        graphics.blit(GUI, relX + 80, relY + 34, 176, 0, progress, 16);
    }
}