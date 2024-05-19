package dev.shinyepo.torquecraft.factory.rotary;

import dev.shinyepo.torquecraft.capabilities.handlers.RotaryHandler;
import dev.shinyepo.torquecraft.constants.TorqueNBT;
import dev.shinyepo.torquecraft.network.IRotaryNetworkDevice;
import dev.shinyepo.torquecraft.network.RotaryNetworkRegistry;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncRotaryPowerS2C;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.UUID;

public class RotaryTransmitter extends BlockEntity implements IRotaryIO, IRotaryTransmitter, IRotaryNetworkDevice {
    protected float progress = 3F;
    private float progressOld;
    public float angle = 0;

    private UUID network_id;

    private Lazy<RotaryHandler> rotaryHandler = Lazy.of(() -> new RotaryHandler(512 * 20, 512 * 20) {
        @Override
        public void markDirty() {
            super.markDirty();
            setChanged();
            if (level != null && !level.isClientSide()) {
                TorqueMessages.sendToAllPlayers(new SyncRotaryPowerS2C(worldPosition, this.ANGULAR, this.TORQUE));
            }
        }
    });

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

    public void setRotaryPower(float angular, float torque) {
        this.rotaryHandler.get().setAngular(angular);
        this.rotaryHandler.get().setTorque(torque);
        this.rotaryHandler.get().markDirty();
    }

    @Override
    public void renderTick() {
        updateAnimation();
        angle = (angle + rotaryHandler.get().getAngular() / 10) % 360;
    }

    public double getAngle() {
        return angle;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains(TorqueNBT.POWER)) {
            rotaryHandler.get().deserializeNBT(provider, tag.getCompound(TorqueNBT.POWER));
            rotaryHandler.get().calculatePower();
        }
        if (tag.contains(TorqueNBT.NETWORK_ID)) {
            network_id = tag.getUUID(TorqueNBT.NETWORK_ID);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (rotaryHandler != null) {
            tag.put(TorqueNBT.POWER, rotaryHandler.get().serializeNBT(provider));
        }
        if (network_id != null) {
            tag.putUUID(TorqueNBT.NETWORK_ID, network_id);
        }
    }

    public void updateAnimation() {
        this.progressOld = this.progress;
        this.progress += 0.1F;
        if (this.progress >= 3.0F) {
            this.progress = 3.0F;
        }
    }

    public UUID getNetworkId() {
        return this.network_id;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level == null || this.level.isClientSide()) return;
        Direction direction = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        this.network_id = RotaryNetworkRegistry.getInstance().registerTransmitter(this, direction);
    }

    @Override
    public void updateNetwork(UUID id) {
        this.network_id = id;
        this.setChanged();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    public void onPlaced() {
    }

    public void removeTransmitter() {
        if (this.level != null && !this.level.isClientSide) {
            RotaryNetworkRegistry.getInstance().removeTransmitter(this.network_id, this);
        }
    }
}
