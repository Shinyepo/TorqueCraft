package dev.shinyepo.torquecraft.block.entities.rotary;

import dev.shinyepo.torquecraft.config.SourceConfig;
import dev.shinyepo.torquecraft.factory.rotary.network.RotarySource;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import dev.shinyepo.torquecraft.utils.DirectionUtils;
import dev.shinyepo.torquecraft.utils.ICoolable;
import dev.shinyepo.torquecraft.utils.IHeatedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static dev.shinyepo.torquecraft.utils.HeatSource.adjustTemp;

public class SteamEngineEntity extends RotarySource implements IHeatedEntity, ICoolable {
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
            adjustTemp(this);
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

    private void initExplosion() {
        fuse--;
        if (fuse < 0) {
            BlockPos pos = this.worldPosition;
            this.level.removeBlock(pos, false);
            ExplosionDamageCalculator dmg = new ExplosionDamageCalculator();
            this.level.explode(null, this.level.damageSources().explosion(null), dmg, pos.getCenter(), 1.0F, false, Level.ExplosionInteraction.BLOCK);
        }
    }

    public double getTemp() {
        return rotaryHandler.get().getTemp();
    }

    public void setTemp(double temp) {
        rotaryHandler.get().setTemp(temp);
    }

    public double getCoef(double ambient) {
        return 0.2167 - (ambient / 1500);
    }
}
