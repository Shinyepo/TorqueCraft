package dev.shinyepo.torquecraft.utils;

import dev.shinyepo.torquecraft.block.prefab.CoolingRadiator;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HeatSource {
    private static final float baseTemp = 20;

    public static float getAmbientTemp(Level level, BlockPos pos) {
        float tempModifier = 0.5F;
        if (level != null) {
            tempModifier = level.getBiome(pos).value().getBaseTemperature();
        }
        return baseTemp * tempModifier;
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

    public static void adjustTemp(BlockEntity entity) {
        if (entity instanceof IHeatedEntity heatedEntity) {
            Level level = entity.getLevel();
            if (level == null) return;
            float heatSource = getBaseTemp(level.getBlockState(entity.getBlockPos().below()));
            float ambientTemp = getAmbientTemp(level, entity.getBlockPos());
            if (heatSource == 0) heatSource = ambientTemp;
            double coef = heatedEntity.getCoef(ambientTemp);
            double temp = heatedEntity.getTemp();

            double q = 900 * (heatSource - temp);
            double heating = q / (16 * 4180);
            double heatLoss = 0;
            if (temp > 90) heatLoss = coef * (temp - 90);
            double resultTemp = temp + (heating - heatLoss);
            if (entity instanceof ICoolable) {
                BlockState aboveState = level.getBlockState(entity.getBlockPos().above());
                Block radiator = aboveState.getBlock();

                if (radiator instanceof CoolingRadiator && temp > 101) {
                    var usage = aboveState.getValue(TorqueAttributes.USAGE);
                    double cooling = (usage.getPercent() * 0.13 / 100) * (temp - 101);
                    if (resultTemp - cooling == temp) return;
                    heatedEntity.setTemp(resultTemp - cooling);
                    return;
                }
            }
            if (resultTemp == temp) return;
            heatedEntity.setTemp(resultTemp);
        }
    }
}
