package dev.shinyepo.torquecraft.menu.mechanicalfan;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.entities.rotary.MechanicalFanEntity;
import dev.shinyepo.torquecraft.config.ClientConfig;
import dev.shinyepo.torquecraft.menu.renderer.RotaryInfoRenderer;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.ChangeModeC2S;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;


public class MechanicalFanScreen extends AbstractContainerScreen<MechanicalFanContainer> {
    private final ResourceLocation GUI = fromNamespaceAndPath(TorqueCraft.MODID, "textures/gui/mechanical_fan.png");
    private RotaryInfoRenderer rotaryInforRenderer;

    public MechanicalFanScreen(MechanicalFanContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = this.imageHeight - 94;
        this.titleLabelX = 32;
    }

    @Override
    protected void init() {
        super.init();
        rotaryInforRenderer = new RotaryInfoRenderer(ClientConfig.MECHANICAL_FAN, this.font);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        addRenderableWidget(Button.builder(Component.literal(this.menu.getMode().name()), pButton -> cycleMode(pButton)).width(40).pos(relX + 129, relY + 59).build());
    }

    @Override
    protected void containerTick() {
        super.containerTick();
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
        rotaryInforRenderer.render(graphics, this.menu.getRotaryHandler(), relX, relY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        rotaryInforRenderer.renderRotaryTooltips(pGuiGraphics, this.menu.getRotaryHandler(), pMouseX, pMouseY, relX, relY);
    }

    private void cycleMode(Button button) {
        int newMode = MechanicalFanEntity.FanMode.values()[this.menu.getMode().ordinal()].getNext().ordinal();
        TorqueMessages.sendToServer(new ChangeModeC2S(this.menu.getBlockPos(), newMode));
        this.menu.changeMode(newMode);
        button.setMessage(Component.literal(this.menu.getMode().name()));
    }
}