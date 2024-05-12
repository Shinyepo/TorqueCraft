package dev.shinyepo.torquecraft.networking.packets;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.entities.pipes.FluidPipeEntity;
import dev.shinyepo.torquecraft.factory.MachineFactory;
import dev.shinyepo.torquecraft.menu.GrinderContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncFluidS2C(BlockPos pos, FluidStack fluidStack) implements CustomPacketPayload {
    public static final Type<SyncFluidS2C> TYPE = new Type<>(new ResourceLocation(TorqueCraft.MODID, "sync.fluid.s2c"));

    @Override
    public Type<SyncFluidS2C> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncFluidS2C> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SyncFluidS2C::pos,
            FluidStack.OPTIONAL_STREAM_CODEC,
            SyncFluidS2C::fluidStack,
            SyncFluidS2C::new);

    public void handler(IPayloadContext context) {
        context.enqueueWork(()->{
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof MachineFactory blockEntity) {
                blockEntity.setFluidStack(this.fluidStack);
                if(Minecraft.getInstance().player.containerMenu instanceof GrinderContainer container && container.getBlockEntity().getBlockPos().equals(pos)) {
                    container.setFluidStack(this.fluidStack);
                }
            }
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof FluidPipeEntity pipe) {
                pipe.setFluidTank(this.fluidStack);
            }
        });
    }
}
