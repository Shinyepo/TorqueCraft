package dev.shinyepo.torquecraft.factory.rotary.render;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AnimatedEntity extends BlockEntity implements IRotaryIO, IRotational {
    private float progress = 3F;
    private float progressOld;
    private float angle = 0;
    private float outputAngle = 0;

    public AnimatedEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public float getProgress() {
        return Mth.lerp(0.2F, this.progressOld, this.progress);
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public double getOutputAngle() {
        return this.outputAngle;
    }

    @Override
    public void updateAnimation() {
        this.progressOld = this.progress;
        this.progress += 0.1F;
        if (this.progress >= 3.0F) {
            this.progress = 3.0F;
        }
    }

    @Override
    public void setProgress(float dur) {
        this.progress = dur;
    }

    @Override
    public double getAngle() {
        return this.angle;
    }

    public void setOutputAngle(float angle) {
        this.outputAngle = angle;
    }
}
