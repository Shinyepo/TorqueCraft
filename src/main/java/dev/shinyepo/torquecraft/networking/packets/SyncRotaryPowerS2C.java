package dev.shinyepo.torquecraft.networking.packets;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.factory.rotary.network.RotarySource;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SyncRotaryPowerS2C(BlockPos pos, float angular, float torque, double temp) implements CustomPacketPayload {
    public static final Type<SyncRotaryPowerS2C> TYPE = new Type<>(new ResourceLocation(TorqueCraft.MODID, "sync.rotary.power.s2c"));

    @Override
    public @NotNull Type<SyncRotaryPowerS2C> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncRotaryPowerS2C> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SyncRotaryPowerS2C::pos,
            ByteBufCodecs.FLOAT,
            SyncRotaryPowerS2C::angular,
            ByteBufCodecs.FLOAT,
            SyncRotaryPowerS2C::torque,
            ByteBufCodecs.DOUBLE,
            SyncRotaryPowerS2C::temp,
            SyncRotaryPowerS2C::new);

    public void handler(IPayloadContext context) {
        context.enqueueWork(()->{
            if (Minecraft.getInstance().level == null) return;
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof RotarySource blockEntity) {
                blockEntity.setRotaryPower(this.angular, this.torque, this.temp);
            }
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof RotaryTransmitter rt) {
                rt.setRotaryPower(this.angular, this.torque, this.temp);
            }
        });
    }
}
