package dev.shinyepo.torquecraft.item.prefab;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
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
        IFluidHandler handler = context.getLevel().getCapability(Capabilities.FluidHandler.BLOCK,context.getClickedPos(),null);
        if(handler != null) {
            boolean isClient = context.getLevel().isClientSide();
            if (isClient) {
                FluidStack fluid = handler.getFluidInTank(0);
                Component displayName = fluid.getFluid().isSame(Fluids.EMPTY) ? Component.literal("Empty") : fluid.getHoverName();
                context.getPlayer().displayClientMessage(Component.translatable("torquecraft.chatmsg.fluid_content", displayName, fluid.getAmount()), true);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
