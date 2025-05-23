package dev.shinyepo.torquecraft.networking.packets;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.GearboxEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public record SyncGearboxRotaryPowerS2C(BlockPos pos, float angular, float torque) implements CustomPacketPayload {
    public static final Type<SyncGearboxRotaryPowerS2C> TYPE = new Type<>(fromNamespaceAndPath(TorqueCraft.MODID, "sync.gearbox.rotary.power.s2c"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncGearboxRotaryPowerS2C> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SyncGearboxRotaryPowerS2C::pos,
            ByteBufCodecs.FLOAT,
            SyncGearboxRotaryPowerS2C::angular,
            ByteBufCodecs.FLOAT,
            SyncGearboxRotaryPowerS2C::torque,
            SyncGearboxRotaryPowerS2C::new);

    @Override
    public @NotNull Type<SyncGearboxRotaryPowerS2C> type() {
        return TYPE;
    }

    public void handler(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) return;
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof GearboxEntity gearboxEntity) {
                gearboxEntity.backupRotaryValues(this.angular, this.torque);
            }
        });
    }
}
