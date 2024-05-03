package dev.shinyepo.torquecraft.item.prefab;

import dev.shinyepo.torquecraft.block.entities.PumpEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class PressureGauge extends Item {
    public PressureGauge(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(!context.getLevel().isClientSide()) {
            return InteractionResult.CONSUME;
        }
        BlockPos blockPos = context.getClickedPos();
        BlockEntity blockEntity = context.getLevel().getBlockEntity(blockPos);
        Direction face = context.getClickedFace();
        IFluidHandler handler = context.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, blockPos, context.getLevel().getBlockState(blockPos), blockEntity, face);
        if (handler != null) {
            FluidStack fluid = handler.getFluidInTank(handler.getTanks());
            Component displayName = fluid.getFluid().isSame(Fluids.EMPTY) ? Component.literal("Empty") : fluid.getHoverName();
            context.getPlayer().displayClientMessage(Component.translatable("torquecraft.chatmsg.fluid_content",displayName, fluid.getAmount()),true);
        }
        return InteractionResult.SUCCESS;
    }
}
