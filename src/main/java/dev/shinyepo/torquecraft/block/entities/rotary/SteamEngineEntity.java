package dev.shinyepo.torquecraft.block.entities.rotary;

import dev.shinyepo.torquecraft.block.prefab.CoolingRadiator;
import dev.shinyepo.torquecraft.config.SourceConfig;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
import dev.shinyepo.torquecraft.factory.rotary.network.RotarySource;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import dev.shinyepo.torquecraft.utils.DirectionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static dev.shinyepo.torquecraft.utils.HeatSource.getAmbientTemp;
import static dev.shinyepo.torquecraft.utils.HeatSource.getBaseTemp;

public class SteamEngineEntity extends RotarySource {
    private float ambientTemp;
    private int fuse = 120;
    private static final SourceConfig config = SourceConfig.STEAM_ENGINE;

    public SteamEngineEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.STEAM_ENGINE_ENTITY.get(), pPos, pBlockState, config);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(state);
        if (level.isClientSide) return;
        if (this.rotaryHandler.get().getTemp() > 150) {
            initExplosion();
        }
        if (level.getGameTime() % 20 != 0) return;
        if (this.fluidTank.get().getFluidAmount() > config.getUsage()) {
            adjustTemp();
        }
        if (rotaryHandler.get().getTemp() < 150 && fuse != 120) {
            resetExplosion();
        }

    }

    private void resetExplosion() {
        this.fuse = 120;
    }

    @Override
    public void renderTick() {
        super.renderTick();
        if (this.getLevel() == null) return;

        Level level = this.getLevel();
        double temp = this.rotaryHandler.get().getTemp();
        int animSpeed = temp > 150 ? 1 : 3;

        if (level.getGameTime() % animSpeed != 0 || temp < 100) return;

        Direction dir = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        Vec3 pos = this.worldPosition.getCenter();
        Vec3 offset1 = DirectionUtils.getBlockHOffsetCoords(pos,dir,0.25,0.3,-0.13);
        Vec3 offset2 = DirectionUtils.getBlockHOffsetCoords(pos,dir,-0.25,0.3,-0.13);

        ParticleOptions particle = temp > 150 ? ParticleTypes.SMOKE : ParticleTypes.WHITE_SMOKE;
        float speed = temp > 150 ? 0.1F : 0.01F;
        level.addParticle(particle,offset1.x, offset1.y, offset1.z,0,speed,0);
        level.addParticle(particle,offset2.x, offset2.y, offset2.z,0,speed,0);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level != null) {
            ambientTemp = getAmbientTemp(this.level, this.worldPosition);
        }
    }

    private void initExplosion() {
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
        double temp = rotaryHandler.get().getTemp();

        double heating = heatSource + (temp - heatSource) * (1 / Math.pow(Math.E, 0.01867));
        double heatLoss = coef * (temp - ambientTemp);

        BlockState aboveState = this.level.getBlockState(this.worldPosition.above());
        Block radiator = aboveState.getBlock();

        if(radiator instanceof CoolingRadiator && temp > 101) {
            var usage = aboveState.getValue(TorqueAttributes.USAGE);
            double cooling = (usage.getPercent()*0.13/100) * (temp - 101);
            rotaryHandler.get().setTemp(heating - heatLoss - cooling);
            return;
        }
        rotaryHandler.get().setTemp(heating - heatLoss);
    }
}
