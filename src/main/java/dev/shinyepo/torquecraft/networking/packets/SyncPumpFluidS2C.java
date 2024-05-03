package dev.shinyepo.torquecraft.networking.packets;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.entities.GrinderEntity;
import dev.shinyepo.torquecraft.block.entities.PumpEntity;
import dev.shinyepo.torquecraft.menu.GrinderContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncPumpFluidS2C(BlockPos pos, FluidStack fluidStack) implements CustomPacketPayload {
    public static final Type<SyncPumpFluidS2C> TYPE = new Type<>(new ResourceLocation(TorqueCraft.MODID, "sync.pump_fluid.s2c"));

    @Override
    public Type<SyncPumpFluidS2C> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPumpFluidS2C> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SyncPumpFluidS2C::pos,
            FluidStack.STREAM_CODEC,
            SyncPumpFluidS2C::fluidStack,
            SyncPumpFluidS2C::new);

    public void handler(IPayloadContext context) {
        context.enqueueWork(()->{
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof PumpEntity blockEntity) {
                blockEntity.setFluidStack(this.fluidStack);
            }
        });
    }
}
