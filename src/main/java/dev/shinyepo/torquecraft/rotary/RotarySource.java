package dev.shinyepo.torquecraft.rotary;

import dev.shinyepo.torquecraft.capabilities.types.IRotaryHandler;
import dev.shinyepo.torquecraft.handlers.RotaryHandler;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncRotaryPowerS2C;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

public class RotarySource extends BlockEntity {
    private static Lazy<RotaryHandler> rotaryHandler;

    public static Direction OUTPUT;

    public RotarySource(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }

    public Lazy<RotaryHandler> initRotarySource(float angular, float torque, Direction facing) {
        OUTPUT = facing;
        return rotaryHandler = Lazy.of(() -> new RotaryHandler(angular, torque){
            @Override
            public void markDirty() {
                super.markDirty();
                setChanged();
                if (!level.isClientSide()) {
                    TorqueMessages.sendToAllPlayers(new SyncRotaryPowerS2C(worldPosition, this.ANGULAR, this.TORQUE));
                }
            }
        }) ;
    }

    public Lazy<RotaryHandler> getRotaryHandler(Direction dir) {
        return rotaryHandler;
    }

    public void setRotaryPower(float angular, float torque) {
        rotaryHandler.get().setAngular(angular);
        rotaryHandler.get().setTorque(torque);
        rotaryHandler.get().calculatePower();
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
