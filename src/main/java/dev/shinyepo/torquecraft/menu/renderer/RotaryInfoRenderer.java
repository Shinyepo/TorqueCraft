package dev.shinyepo.torquecraft.menu.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.capabilities.handlers.rotary.IRotaryHandler;
import dev.shinyepo.torquecraft.config.ClientConfig;
import dev.shinyepo.torquecraft.utils.MouseUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RotaryInfoRenderer {
    private static final ResourceLocation ROTARY = new ResourceLocation(TorqueCraft.MODID, "textures/gui/rotary_info.png");
    private final ClientConfig CONFIG;

    private static final int ATLAS_SIZE = 256;

    private static final int BAR_X = 18;
    private static final int BAR_Y = 72;

    private static final int BAR_WIDTH = 4;
    private static final int BAR_HEIGHT = 38;

    private static final int BG_X = 8;
    private static final int BG_Y = 31;
    private final Font FONT;
    private RotaryType ROTARY_TYPE = RotaryType.NOT_ENOUGH;

    public RotaryInfoRenderer(ClientConfig config, Font font) {
        this.CONFIG = config;
        this.FONT = font;
    }

    private void drawRotary(GuiGraphics graphics, IRotaryHandler handler) {
        float angular = handler.getAngular();
        float torque = handler.getTorque();
        if (torque <= 0 && angular <= 0) {
            ROTARY_TYPE = RotaryType.NO_POWER;
            drawInfo(graphics, ROTARY_TYPE);
            return;
        }

        int scaledAngular = (int) ((angular * BAR_HEIGHT) / CONFIG.getMinAngular());
        int scaledTorque = (int) ((torque * BAR_HEIGHT) / CONFIG.getMinTorque());

        if (scaledAngular > BAR_HEIGHT) {
            scaledAngular = BAR_HEIGHT;
        }
        if (scaledTorque > BAR_HEIGHT) {
            scaledTorque = BAR_HEIGHT;
        }

        if (scaledTorque == BAR_HEIGHT && scaledAngular == BAR_HEIGHT) {
            ROTARY_TYPE = RotaryType.SUFFICIENT;
        }

        drawInfo(graphics, ROTARY_TYPE);
        //Angular
        fillRotaryInfo(graphics, BG_X + 2, BG_Y, scaledAngular);
        //Torque
        fillRotaryInfo(graphics, BG_X + 12, BG_Y, scaledTorque);
    }

    private void drawBackground(GuiGraphics graphics) {
        graphics.blit(ROTARY, 5, 5, 0, 110, 24, 74);
        graphics.blit(ROTARY, BG_X, BG_Y, 0, 72, 18, BAR_HEIGHT);
    }

    private void drawInfo(GuiGraphics graphics, RotaryType type) {
        int x = 8;
        int y = 8;

        graphics.blit(ROTARY, x, y, 0, 18 * type.ordinal(), 18, 18);
    }

    public void render(GuiGraphics graphics, IRotaryHandler handler, int x, int y) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 100);

        ROTARY_TYPE = RotaryType.NOT_ENOUGH;
        drawBackground(graphics);
        drawUnits(graphics);
        drawRotary(graphics, handler);

        pose.popPose();
    }

    private void drawUnits(GuiGraphics graphics) {
        graphics.pose().pushPose();
        graphics.pose().translate(8, 73, 0);
        graphics.pose().scale(0.5F, 0.5F, 0.5F);
        graphics.drawString(FONT, "RPM", 0, 0, 4210752, false);
        graphics.drawString(FONT, "Nm", 23, 0, 4210752, false);
        graphics.pose().popPose();
    }

    private void fillRotaryInfo(GuiGraphics graphics, int x, int y, int scaledValue) {
        float uMin = BAR_X / (float) ATLAS_SIZE;
        float vMin = (BAR_Y + BAR_HEIGHT - scaledValue) / (float) ATLAS_SIZE;

        graphics.blit(ROTARY, x, y + (BAR_HEIGHT - scaledValue), (int) (uMin * ATLAS_SIZE), (int) (vMin * ATLAS_SIZE), BAR_WIDTH, scaledValue);
    }

    public void renderRotaryTooltips(GuiGraphics pGuiGraphics, IRotaryHandler handler, int pMouseX, int pMouseY, int relX, int relY) {
        if (mouseOverBar(pMouseX, pMouseY, relX, relY, BG_X, BG_Y)) {
            float angular = handler.getAngular();
            pGuiGraphics.renderTooltip(FONT, getAngularTooltip(angular), Optional.empty(), pMouseX - relX, pMouseY - relY);
        }
        if (mouseOverBar(pMouseX, pMouseY, relX, relY, BG_X+9, BG_Y)) {
            float torque = handler.getTorque();
            pGuiGraphics.renderTooltip(FONT, getTorqueTooltip(torque), Optional.empty(), pMouseX - relX, pMouseY - relY);
        }
        if (mouseOverInfo(pMouseX,pMouseY,relX,relY,8,8)) {
            pGuiGraphics.renderTooltip(FONT, getInfoTooltip(ROTARY_TYPE), Optional.empty(), pMouseX - relX, pMouseY - relY);
        }
    }

    private List<Component> getInfoTooltip(RotaryType type) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(type.getTITLE());
        tooltip.add(type.getTOOLTIP());
        return tooltip;
    }

    private List<Component> getAngularTooltip(float angular) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal("Angular velocity"));
        tooltip.add(Component.literal(angular + "/" + CONFIG.getMinAngular()+ " RPM").withStyle(ChatFormatting.GRAY));
        return tooltip;
    }

    private List<Component> getTorqueTooltip(float torque) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal("Torque"));
        tooltip.add(Component.literal(torque + "/" + CONFIG.getMinTorque()+ " Nm").withStyle(ChatFormatting.GRAY));
        return tooltip;
    }

    private boolean mouseOverBar(int pMouseX, int pMouseY, int relX, int relY, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, relX + offsetX, relY + offsetY, 8, BAR_HEIGHT);
    }

    private boolean mouseOverInfo(int pMouseX, int pMouseY, int relX, int relY, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, relX + offsetX, relY + offsetY, 18, 18);
    }

    private enum RotaryType {
        MAX_INPUT (Component.translatable("torquecraft.tooltip.rotary.info.title.max"),Component.translatable("torquecraft.tooltip.rotary.info.tooltip.max")),
        SUFFICIENT(Component.translatable("torquecraft.tooltip.rotary.info.title.sufficient"), Component.translatable("torquecraft.tooltip.rotary.info.tooltip.sufficient")),
        NOT_ENOUGH (Component.translatable("torquecraft.tooltip.rotary.info.title.notenough"), Component.translatable("torquecraft.tooltip.rotary.info.tooltip.notenough")),
        NO_POWER (Component.translatable("torquecraft.tooltip.rotary.info.title.nopower"), Component.translatable("torquecraft.tooltip.rotary.info.tooltip.nopower"));

        private final Component TITLE;
        private final MutableComponent TOOLTIP;

        RotaryType(Component title, MutableComponent tooltip) {
            this.TITLE = title;
            this.TOOLTIP = tooltip;
        }

        public Component getTITLE() {
            return TITLE;
        }

        public MutableComponent getTOOLTIP() {
            return TOOLTIP.withStyle(ChatFormatting.GRAY);
        }
    }
}
