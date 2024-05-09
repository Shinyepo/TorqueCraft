package dev.shinyepo.torquecraft.networking.packets;

import com.mojang.serialization.Codec;
import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.entities.pipes.FluidPipeEntity;
import dev.shinyepo.torquecraft.capabilities.types.IRotaryHandler;
import dev.shinyepo.torquecraft.factory.MachineFactory;
import dev.shinyepo.torquecraft.handlers.RotaryHandler;
import dev.shinyepo.torquecraft.menu.GrinderContainer;
import dev.shinyepo.torquecraft.rotary.RotarySource;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncRotaryPowerS2C(BlockPos pos, float angular, float torque) implements CustomPacketPayload {
    public static final Type<SyncRotaryPowerS2C> TYPE = new Type<>(new ResourceLocation(TorqueCraft.MODID, "sync.rotary.power.s2c"));

    @Override
    public Type<SyncRotaryPowerS2C> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncRotaryPowerS2C> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SyncRotaryPowerS2C::pos,
            ByteBufCodecs.FLOAT,
            SyncRotaryPowerS2C::angular,
            ByteBufCodecs.FLOAT,
            SyncRotaryPowerS2C::torque,
            SyncRotaryPowerS2C::new);

    public void handler(IPayloadContext context) {
        context.enqueueWork(()->{
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof RotarySource blockEntity) {
                blockEntity.setRotaryPower(this.angular, this.torque);
            }
        });
    }
}
