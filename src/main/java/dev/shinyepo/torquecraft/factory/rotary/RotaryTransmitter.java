package dev.shinyepo.torquecraft.factory.rotary;

import dev.shinyepo.torquecraft.capabilities.handlers.RotaryHandler;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncRotaryPowerS2C;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;

public class RotaryTransmitter extends BlockEntity implements IRotaryIO {
    protected float progress = 3F;
    private float progressOld;
    private Lazy<RotaryHandler> rotaryHandler = Lazy.of(() ->new RotaryHandler(0,0){
        @Override
        public void markDirty() {
            super.markDirty();
            setChanged();
            if (!level.isClientSide()) {
                TorqueMessages.sendToAllPlayers(new SyncRotaryPowerS2C(worldPosition, this.ANGULAR, this.TORQUE));
            }
        }
    });;

    public RotaryTransmitter(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
    }



    public RotaryHandler getRotaryHandler(Direction dir) {
        return rotaryHandler.get();
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
        if (this.progress >= 3.0F) {
            this.progress = 3.0F;
        }
    }
}
