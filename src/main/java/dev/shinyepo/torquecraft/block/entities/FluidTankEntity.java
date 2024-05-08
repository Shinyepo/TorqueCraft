package dev.shinyepo.torquecraft.block.entities;

import dev.shinyepo.torquecraft.factory.MachineFactory;
import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
import dev.shinyepo.torquecraft.registries.TorqueBlockEntities;
import dev.shinyepo.torquecraft.registries.TorqueItems;
import dev.shinyepo.torquecraft.registries.TorqueRecipes;
import dev.shinyepo.torquecraft.utils.TorqueFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.Optional;

public class FluidTankEntity extends MachineFactory {
    private final int fluidCapacity = 64000;

    private final TorqueFluidTank fluidTank = createFluidTank(fluidCapacity);

    public FluidTankEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.FLUID_TANK_ENTITY.get(), pPos, pBlockState);
    }

    public void tick() {
        distributeFluid();
    }

    private void distributeFluid() {
        if (fluidTank.isEmpty()) {
            return;
        }
        IFluidHandler fHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos().relative(Direction.DOWN),null);
        if (fHandler != null) {
            if (!canAcceptFluid(fHandler)) return;
            int remainingSpace = fHandler.getTankCapacity(0) - fHandler.getFluidInTank(0).getAmount();
            int amount = Math.min(remainingSpace, 250);

            fHandler.fill(new FluidStack(fluidTank.getFluid().getFluid(), amount), IFluidHandler.FluidAction.EXECUTE);
            fluidTank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
            setChanged();
        }
    }

    private boolean canAcceptFluid(IFluidHandler handler) {
//        int tanks = handler.getTanks();
//        for (int i = 0; i < tanks; i++) {
//            if (!handler.isFluidValid(i,fluidTank.getFluid())) continue;
//            if (handler.getFluidInTank(i).isEmpty()) return true;
//            if (handler.getFluidInTank(i).is(fluidTank.getFluid().getFluid()) && handler.getTankCapacity(i) < handler.getFluidInTank(i).getAmount()) {
//                return true;
//            }
//        }
        return handler.getFluidInTank(0).isEmpty() || (handler.getFluidInTank(0).is(fluidTank.getFluid().getFluid()) && handler.getTankCapacity(0) > handler.getFluidInTank(0).getAmount());
    }

    public TorqueFluidTank getFluidTank(Direction dir) {
        if (dir == null) return fluidTank;
        if(dir == Direction.UP || dir == Direction.DOWN) {
            return fluidTank;
        }
        return null;
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluidTank.setFluid(fluidStack);
    }
}
