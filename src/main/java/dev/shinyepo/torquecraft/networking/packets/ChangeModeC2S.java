package dev.shinyepo.torquecraft.networking.packets;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.entities.rotary.MechanicalFanEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public record ChangeModeC2S(BlockPos pos, int mode) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ChangeModeC2S> TYPE = new CustomPacketPayload.Type<>(fromNamespaceAndPath(TorqueCraft.MODID, "change.mode.c2s"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChangeModeC2S> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ChangeModeC2S::pos,
            ByteBufCodecs.INT,
            ChangeModeC2S::mode,
            ChangeModeC2S::new);

    public static void handler(final ChangeModeC2S data, IPayloadContext context) {

        context.enqueueWork(() -> {
            var server = Objects.requireNonNull(context.player().getServer()).overworld();
            if (server.getBlockEntity(data.pos()) instanceof MechanicalFanEntity fan) {
                fan.changeMode(data.mode());
            }
        });
    }

    @Override
    public CustomPacketPayload.Type<ChangeModeC2S> type() {
        return TYPE;
    }
}
