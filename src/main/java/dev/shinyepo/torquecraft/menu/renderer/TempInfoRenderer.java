package dev.shinyepo.torquecraft.menu.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.config.HeatConfig;
import dev.shinyepo.torquecraft.utils.MouseUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class TempInfoRenderer {
    private static final ResourceLocation TEMP = fromNamespaceAndPath(TorqueCraft.MODID, "textures/gui/temp_info.png");
    private static final int ATLAS_SIZE = 256;
    private static final int BAR_X = 8;
    private static final int BAR_Y = 72;
    private static final int BAR_WIDTH = 4;
    private static final int BAR_HEIGHT = 38;
    private static final int BG_X = 13;
    private static final int BG_Y = 31;
    private final HeatConfig CONFIG;
    private final Font FONT;
    private final int width;
    private final int height;
    private TempType TEMP_TYPE = TempType.NO_HEAT;

    public TempInfoRenderer(HeatConfig config, Font font, int width, int height) {
        this.CONFIG = config;
        this.FONT = font;
        this.width = width;
        this.height = height;
    }


    private void drawTemp(GuiGraphics graphics, int temp) {
        if (temp <= 0) {
            TEMP_TYPE = TempType.NO_HEAT;
            drawInfo(graphics, TEMP_TYPE);
            return;
        }

        int scaledTemp = (int) ((temp * BAR_HEIGHT) / CONFIG.getMaxTemp());

        if (scaledTemp > BAR_HEIGHT) {
            scaledTemp = BAR_HEIGHT;
        }

        TEMP_TYPE = TempType.NOT_ENOUGH;
        if (temp > 999)
            TEMP_TYPE = TempType.SUFFICIENT;
        if (scaledTemp == BAR_HEIGHT) {
            TEMP_TYPE = TempType.MAX_HEAT;
        }

        drawInfo(graphics, TEMP_TYPE);
        fillTempInfo(graphics, BG_X + 2, BG_Y, scaledTemp + 1);
    }

    private void drawBackground(GuiGraphics graphics) {
        graphics.blit(RenderType::guiTextured, TEMP, 5, 5, 0, 110, 24, 74,width, height);
        graphics.blit(RenderType::guiTextured, TEMP, BG_X, BG_Y, 0, 72, 8, BAR_HEIGHT,width, height);
    }

    private void drawInfo(GuiGraphics graphics, TempType type) {
        int x = 8;
        int y = 8;

        graphics.blit(RenderType::guiTextured, TEMP, x, y, 0, 18 * type.ordinal(), 18, 18,width, height);
    }

    public void render(GuiGraphics graphics, int temp, int x, int y) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 100);

        TEMP_TYPE = TempType.NO_HEAT;
        drawBackground(graphics);
        drawUnitName(graphics);
        drawTemp(graphics, temp);

        pose.popPose();
    }

    private void drawUnitName(GuiGraphics graphics) {
        graphics.pose().pushPose();
        graphics.pose().translate(8, 73, 0);
        graphics.pose().scale(0.5F, 0.5F, 0.5F);
        graphics.drawString(FONT, "Temp", 6, 0, 4210752, false);
        graphics.pose().popPose();
    }

    private void fillTempInfo(GuiGraphics graphics, int x, int y, int scaledValue) {
        float uMin = BAR_X / (float) ATLAS_SIZE;
        float vMin = (BAR_Y + BAR_HEIGHT - scaledValue) / (float) ATLAS_SIZE;

        graphics.blit(RenderType::guiTextured, TEMP, x, y + (BAR_HEIGHT - scaledValue), (int) (uMin * ATLAS_SIZE), (int) (vMin * ATLAS_SIZE), BAR_WIDTH, scaledValue,width, height);
    }

    public void renderTempTooltips(GuiGraphics pGuiGraphics, int temp, int pMouseX, int pMouseY, int relX, int relY) {
        if (mouseOverBar(pMouseX, pMouseY, relX, relY, BG_X, BG_Y)) {
            pGuiGraphics.renderTooltip(FONT, getTempTooltip(temp), Optional.empty(), pMouseX - relX, pMouseY - relY);
        }
        if (mouseOverInfo(pMouseX, pMouseY, relX, relY, 8, 8)) {
            pGuiGraphics.renderTooltip(FONT, getInfoTooltip(TEMP_TYPE), Optional.empty(), pMouseX - relX, pMouseY - relY);
        }
    }

    private List<Component> getInfoTooltip(TempType type) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(type.getTITLE());
        tooltip.add(type.getTOOLTIP());
        return tooltip;
    }

    private List<Component> getTempTooltip(int temp) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal("Temperature"));
        tooltip.add(Component.literal(temp + " °C").withStyle(ChatFormatting.GRAY));
        return tooltip;
    }

    private boolean mouseOverBar(int pMouseX, int pMouseY, int relX, int relY, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, relX + offsetX, relY + offsetY, 8, BAR_HEIGHT);
    }

    private boolean mouseOverInfo(int pMouseX, int pMouseY, int relX, int relY, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, relX + offsetX, relY + offsetY, 18, 18);
    }

    private enum TempType {
        MAX_HEAT(Component.translatable("torquecraft.tooltip.temp.info.title.max"), Component.translatable("torquecraft.tooltip.temp.info.tooltip.max")),
        SUFFICIENT(Component.translatable("torquecraft.tooltip.temp.info.title.sufficient"), Component.translatable("torquecraft.tooltip.temp.info.tooltip.sufficient")),
        NOT_ENOUGH(Component.translatable("torquecraft.tooltip.temp.info.title.notenough"), Component.translatable("torquecraft.tooltip.temp.info.tooltip.notenough")),
        NO_HEAT(Component.translatable("torquecraft.tooltip.temp.info.title.noheat"), Component.translatable("torquecraft.tooltip.temp.info.tooltip.noheat"));

        private final Component TITLE;
        private final MutableComponent TOOLTIP;

        TempType(Component title, MutableComponent tooltip) {
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
