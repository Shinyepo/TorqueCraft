package dev.shinyepo.torquecraft.factory.rotary.network;

import dev.shinyepo.torquecraft.capabilities.handlers.rotary.RotaryHandler;
import dev.shinyepo.torquecraft.config.IRotaryConfig;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.constants.TorqueNBT;
import dev.shinyepo.torquecraft.factory.rotary.render.AnimatedEntity;
import dev.shinyepo.torquecraft.network.IRotaryNetworkDevice;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncRotaryPowerS2C;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RotaryNetworkDevice<CONFIG extends IRotaryConfig> extends AnimatedEntity implements IRotaryNetworkDevice {
    private UUID networkId;
    protected CONFIG config;
    private SideType[] sides = new SideType[] {SideType.NONE,SideType.NONE,SideType.NONE,SideType.NONE,SideType.NONE,SideType.NONE};

    protected final Lazy<RotaryHandler> rotaryHandler = Lazy.of(() -> new RotaryHandler(config.getAngular(),config.getTorque()) {
        @Override
        public void markDirty() {
            super.markDirty();
            setChanged();
            if (level != null && !level.isClientSide()) {
                TorqueMessages.sendToAllPlayers(new SyncRotaryPowerS2C(worldPosition, this.ANGULAR, this.TORQUE, this.TEMP));
            }
        }
    });

    public void configureSides(Direction facing, SideType type) {
        sides[facing.ordinal()] = type;
    }
    private void loadSide(int side, int type) {
        sides[side] = SideType.values()[type];
    }

    public SideType[] getSidesConfig() {
        return sides;
    }

    public RotaryNetworkDevice(BlockEntityType<?> type, BlockPos pos, BlockState blockState, CONFIG config) {
        super(type, pos, blockState);
        this.config = config;
    }

    public RotaryHandler getRotaryHandler(Direction dir) {
        return rotaryHandler.get();
    }

    public void setRotaryPower(float angular, float torque, double temp) {
        this.rotaryHandler.get().setAngular(angular);
        this.rotaryHandler.get().setTorque(torque);
        this.rotaryHandler.get().setTemp(temp);
        this.rotaryHandler.get().markDirty();
    }

    @Override
    public void renderTick() {
        super.renderTick();
        setAngle(rotaryHandler.get().getAngular());
    }

    @Override
    public UUID getNetworkId() {
        return this.networkId;
    }

    @Override
    public void updateNetwork(UUID id) {
        this.networkId = id;
        this.setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains(TorqueNBT.POWER)) {
            rotaryHandler.get().deserializeNBT(provider, tag.getCompound(TorqueNBT.POWER));
            rotaryHandler.get().calculatePower();
        }
        if (tag.contains(TorqueNBT.NETWORK_ID)) {
            this.networkId = tag.getUUID(TorqueNBT.NETWORK_ID);
        }
        if (tag.contains(TorqueNBT.SIDES)) {
            var sides = tag.getIntArray(TorqueNBT.SIDES);
            for (int i = 0; i < sides.length; i++) {
                loadSide(i,sides[i]);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (this.rotaryHandler != null) {
            tag.put(TorqueNBT.POWER, rotaryHandler.get().serializeNBT(provider));
        }
        if (this.networkId != null) {
            tag.putUUID(TorqueNBT.NETWORK_ID, this.networkId);
        }
        int[] sides = new int[this.sides.length];
        for (int i = 0; i < this.sides.length; i++) {
            sides[i] = this.sides[i].ordinal();
        }
        tag.putIntArray(TorqueNBT.SIDES,sides);
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
}
