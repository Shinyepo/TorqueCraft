package dev.shinyepo.torquecraft.networking.packets;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncMeltdownS2C(BlockPos pos, boolean state) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncMeltdownS2C> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(TorqueCraft.MODID, "sync.meltdown.s2c"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncMeltdownS2C> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SyncMeltdownS2C::pos,
            ByteBufCodecs.BOOL,
            SyncMeltdownS2C::state,
            SyncMeltdownS2C::new);

    @Override
    public CustomPacketPayload.Type<SyncMeltdownS2C> type() {
        return TYPE;
    }

    public void handler(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof RotaryTransmitter transmitter) {
                transmitter.clientMeltdown(state);
            }
        });
    }
}
