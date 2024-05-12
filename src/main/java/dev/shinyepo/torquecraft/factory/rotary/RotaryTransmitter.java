package dev.shinyepo.torquecraft.factory.rotary;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RotaryTransmitter extends BlockEntity implements IRotaryIO {
    protected float progress = 2F;
    private float progressOld;

    public RotaryTransmitter(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
    }


    public float getProgress(float pPartialTicks) {
        return Mth.lerp(pPartialTicks, this.progressOld, this.progress);
    }

    public void setProgress(float dur) {
        this.progress = dur;
    }

    @Override
    public void renderTick() {
        updateAnimation();
    }

    public void updateAnimation() {
        this.progressOld = this.progress;
        this.progress += 0.1F;
        if (this.progress >= 2.0F) {
            this.progress = 2.0F;
        }
    }
}
