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
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RotarySource extends BlockEntity implements IRotaryIO, IRotarySource, IRotaryNetworkDevice {
    protected float progress = 3F;
    private float progressOld;
    public float angle = 0;
    private Lazy<RotaryHandler> rotaryHandler;

    private UUID network_id;

    public static Direction OUTPUT;

    public RotarySource(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }

    public Lazy<RotaryHandler> initRotarySource(float angular, float torque, Direction facing) {
        OUTPUT = facing;
        return rotaryHandler = Lazy.of(() -> new RotaryHandler(angular, torque) {
            @Override
            public void markDirty() {
                super.markDirty();
                setChanged();
                if (level != null && !level.isClientSide()) {
                    TorqueMessages.sendToAllPlayers(new SyncRotaryPowerS2C(worldPosition, this.ANGULAR, this.TORQUE));
                }
            }
        });
    }

    public UUID getNetworkId() {
        return this.network_id;
    }

    public RotaryHandler getRotaryHandler(Direction dir) {
        return rotaryHandler.get();
    }

    public void setRotaryPower(float angular, float torque) {
        this.rotaryHandler.get().setAngular(angular);
        this.rotaryHandler.get().setTorque(torque);
        this.rotaryHandler.get().calculatePower();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag nbt = super.getUpdateTag(provider);
        saveAdditional(nbt, provider);
        return nbt;
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

    public float getProgress(float pPartialTicks) {
        return Mth.lerp(pPartialTicks, this.progressOld, this.progress);
    }

    public double getAngle() {
        return angle;
    }

    public void setProgress(float dur) {
        this.progress = dur;
    }

    @Override
    public void renderTick() {
        updateAnimation();
        angle = (angle + rotaryHandler.get().getAngular() / 10) % 360;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level == null || this.level.isClientSide()) return;
        this.network_id = RotaryNetworkRegistry.getInstance().registerSource(this);
    }

    public void updateAnimation() {
        this.progressOld = this.progress;
        this.progress += 0.1F;
        if (this.progress >= 3.0F) {
            this.progress = 3.0F;
        }
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

    public void removeSource() {
        if (this.level != null && !this.level.isClientSide) {
            RotaryNetworkRegistry.getInstance().removeSource(this.network_id, this);
        }
    }
}
