package dev.shinyepo.torquecraft.block.entities;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.factory.MachineFactory;
import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
import dev.shinyepo.torquecraft.registries.TorqueBlockEntities;
import dev.shinyepo.torquecraft.registries.TorqueItems;
import dev.shinyepo.torquecraft.registries.TorqueRecipes;
import dev.shinyepo.torquecraft.utils.TorqueFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.List;
import java.util.Optional;

public class GrinderEntity extends MachineFactory {
    public static final List<TagKey<Item>> validInputs = List.of(Tags.Items.SEEDS);

    private final int fluidCapacity = 2000;

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_INPUT_COUNT = 1;

    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_OUTPUT_COUNT = 1;
    public static final int SLOT_COUNT = SLOT_INPUT_COUNT + SLOT_OUTPUT_COUNT;

    private final Lazy<CombinedInvWrapper> itemHandler = createItemHandler(SLOT_INPUT_COUNT, SLOT_OUTPUT_COUNT);

    private final TorqueFluidTank fluidTank = createFluidTank(fluidCapacity);

    public GrinderEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.GRINDER_ENTITY.get(), pPos, pBlockState);
        setValidInputs(validInputs);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(pLevel,pPos,pState);

            if(hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            //debug reasons
//            fluidTank.fill(new FluidStack(Fluids.WATER,500), IFluidHandler.FluidAction.EXECUTE);
            if (pLevel.getGameTime() % 20 == 0) {
                //debug reasons
//                fluidTank.drain(2, IFluidHandler.FluidAction.EXECUTE);
                updateFluidStack();
                setChanged(pLevel,pPos,pState);
            }
            resetProgress();
        }
        if (pLevel.getBlockEntity(pPos) instanceof GrinderEntity gE) {
            if(hasFluidItemInSourceSlot(gE)) {
                transferItemFluidToFluidTank(gE);
            }
        }
    }

    private static boolean hasFluidItemInSourceSlot(GrinderEntity pEntity) {
        return pEntity.itemHandler.get().getStackInSlot(SLOT_INPUT).getCount() > 0;
    }

    private static void transferItemFluidToFluidTank(GrinderEntity pEntity) {
        IFluidHandlerItem fHandler = pEntity.itemHandler.get().getStackInSlot(SLOT_INPUT).getCapability(Capabilities.FluidHandler.ITEM);
        if (fHandler != null){
            int drainAmount = Math.min(pEntity.fluidTank.getSpace(), 1000);

        FluidStack stack = fHandler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
        if (pEntity.fluidTank.isFluidValid(stack)) {
            stack = fHandler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
            fillTankWithFluid(pEntity, stack, fHandler.getContainer());
        }
        }
    }

    private static void fillTankWithFluid(GrinderEntity pEntity, FluidStack stack, ItemStack container) {
        pEntity.fluidTank.fill(stack, IFluidHandler.FluidAction.EXECUTE);

        pEntity.itemHandler.get().extractItem(SLOT_INPUT, 1, false);
        pEntity.itemHandler.get().insertItem(SLOT_INPUT, container, false);
    }

    public void updateFluidStack() {
        setFluidStack(fluidTank.getFluid());
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluidTank.setFluid(fluidStack);
    }

    public FluidStack getFluidStack() {
        return this.fluidTank.getFluid();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void craftItem() {
        Optional<RecipeHolder<GrinderRecipe>> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().value().getResultItem(null);
        FluidStack resultFluid = recipe.get().value().getResultFluid(null);
        int fluidAmount = resultFluid.getAmount();
        if (this.itemHandler.get().getStackInSlot(SLOT_INPUT).is(TorqueItems.CANOLA_SEEDS.get())) {
            fluidAmount = fluidAmount * 2;
        }
        this.itemHandler.get().extractItem(SLOT_INPUT,1, false);
        this.itemHandler.get().setStackInSlot(SLOT_OUTPUT, new ItemStack(result.getItem(),
                this.itemHandler.get().getStackInSlot(SLOT_OUTPUT).getCount() + result.getCount()));
        this.fluidTank.fill(new FluidStack(resultFluid.getFluid(), fluidAmount), IFluidHandler.FluidAction.EXECUTE);
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<GrinderRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) return false;

        ItemStack result = recipe.get().value().getResultItem(null);
        FluidStack resultFluid = recipe.get().value().getResultFluid(null);
        return canFitInOutput(result.getCount()) && canOutputItem(result.getItem()) && fluidsMatch(resultFluid) && canFitInTank(resultFluid);
    }

    private Optional<RecipeHolder<GrinderRecipe>> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(1);
        for (int i = 0; i < 1; i++) {
            inventory.setItem(i, this.itemHandler.get().getStackInSlot(0));
        }
        return this.level.getRecipeManager().getRecipeFor(TorqueRecipes.Types.GRINDING, inventory, this.level);
    }

    private boolean canOutputItem(Item item) {
            return this.itemHandler.get().getStackInSlot(SLOT_OUTPUT).isEmpty() || this.itemHandler.get().getStackInSlot(1).is(item);
    }

    private boolean canFitInOutput (int count) {
        return this.itemHandler.get().getStackInSlot(SLOT_OUTPUT).getCount() + count <= this.itemHandler.get().getStackInSlot(1).getMaxStackSize();
    }

    private boolean fluidsMatch(FluidStack resultFluid) {
        return this.fluidTank.getFluid().is(resultFluid.getFluid()) || this.fluidTank.isEmpty();
    }

    private boolean canFitInTank (FluidStack resultFluid) {
        return this.fluidTank.getFluid().getAmount() + resultFluid.getAmount() <= this.fluidTank.getCapacity();
    }
}
