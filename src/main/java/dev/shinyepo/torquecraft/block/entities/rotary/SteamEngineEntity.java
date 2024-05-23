package dev.shinyepo.torquecraft.block.entities.rotary;

import dev.shinyepo.torquecraft.config.SourceConfig;
import dev.shinyepo.torquecraft.constants.TorqueNBT;
import dev.shinyepo.torquecraft.factory.rotary.network.RotarySource;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static dev.shinyepo.torquecraft.utils.HeatSource.getAmbientTemp;
import static dev.shinyepo.torquecraft.utils.HeatSource.getBaseTemp;

public class SteamEngineEntity extends RotarySource {
    private double temp = 0;
    private final float ambientTemp;
    private int fuse = 40;
    private static final SourceConfig config = SourceConfig.STEAM_ENGINE;

    public SteamEngineEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.STEAM_ENGINE_ENTITY.get(), pPos, pBlockState, config);
        ambientTemp = getAmbientTemp(this.worldPosition);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(state, temp);
        if (level.isClientSide) return;
        if (temp > 150) {
            initExplosion();
        }
        if (level.getGameTime() % 10 != 0) return;
        if (this.fluidTank.get().getFluidAmount() > config.getUsage()) {
            adjustTemp();
        }
    }

    private void initExplosion() {
        System.out.println(fuse);
        fuse--;
        if (fuse < 0) {
            BlockPos pos = this.worldPosition;
            this.level.removeBlock(pos, false);
            ExplosionDamageCalculator dmg = new ExplosionDamageCalculator();
            this.level.explode(null, this.level.damageSources().explosion(null), dmg, pos.getCenter(), 1.0F, false, Level.ExplosionInteraction.BLOCK);
        }
    }

    private void adjustTemp() {
        float heatSource = getBaseTemp(this.level.getBlockState(this.worldPosition.below()));
        if (heatSource == 0) heatSource = ambientTemp;
        double coef = 0.1267 - (ambientTemp / 1500);

        double heating = heatSource + (temp - heatSource) * (1 / Math.pow(Math.E, 0.01867));
        double heatLoss = coef * (temp - ambientTemp);

//        if(this.level.getBlockEntity(this.worldPosition.above()) instanceof Cooler) {
//            double cooling = 0.1 * (temp - 20);
//            temp = heating - heatLoss - cooling;
//            return;
//        }
        temp = heating - heatLoss;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putDouble(TorqueNBT.TEMP, (double) Math.round(temp * 100) / 100);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains(TorqueNBT.TEMP)) {
            temp = tag.getDouble(TorqueNBT.TEMP);
        }
    }
}
