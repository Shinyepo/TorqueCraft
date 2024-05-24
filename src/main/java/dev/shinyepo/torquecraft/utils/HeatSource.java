package dev.shinyepo.torquecraft.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HeatSource {
    private static final float ambientTemp = 20;

    public static float getAmbientTemp(Level level, BlockPos pos) {
        float tempModifier = 0.5F;
        if (level != null) {
            tempModifier = level.getBiome(pos).value().getBaseTemperature();
        }
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
