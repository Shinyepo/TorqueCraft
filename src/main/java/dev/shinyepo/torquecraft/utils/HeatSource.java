package dev.shinyepo.torquecraft.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HeatSource {
    private static final float ambientTemp = 20;

    public static float getAmbientTemp(BlockPos pos) {
        float tempModifier = Minecraft.getInstance().level.getBiome(pos).value().getBaseTemperature();
        return ambientTemp * tempModifier;
    }

    public static float getBaseTemp(BlockState heatSource) {
        if (heatSource.is(BlockTags.FIRE) || heatSource.is(BlockTags.CAMPFIRES)) {
            return 800;
        }
        if (heatSource.is(Blocks.LAVA)) {
            return 1200;
        }
        return 0;
    }
}
