package dev.shinyepo.torquecraft.block.entities.rotary;

import dev.shinyepo.torquecraft.factory.MachineFactory;
import dev.shinyepo.torquecraft.factory.TorqueFluidTank;
import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
import dev.shinyepo.torquecraft.registries.TorqueRecipes;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
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

import java.util.List;
import java.util.Optional;

public class GrinderEntity extends MachineFactory {
    public static final List<TagKey<Item>> validInputs = List.of(Tags.Items.SEEDS);
    public static final List<TagKey<Item>> validFluidSlotInputs = List.of(Tags.Items.BUCKETS);

    private final int fluidCapacity = 8000;

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_INPUT_COUNT = 1;

    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_OUTPUT_COUNT = 1;

    public static final int SLOT_DRAIN_FLUID = 2;
    public static final int SLOT_DRAIN_COUNT = 1;

    public static final int SLOT_COUNT = SLOT_INPUT_COUNT + SLOT_OUTPUT_COUNT + SLOT_DRAIN_COUNT;

    private final Lazy<CombinedInvWrapper> itemHandler = createItemHandler(SLOT_INPUT_COUNT, SLOT_OUTPUT_COUNT, SLOT_DRAIN_COUNT);

    private final TorqueFluidTank fluidTank = createFluidTank(fluidCapacity);

    public GrinderEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.GRINDER_ENTITY.get(), pPos, pBlockState);
        setValidInputs(validInputs);
        setValidFluidSlotInputs(validFluidSlotInputs);
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
            resetProgress();
        }
        if (pLevel.getBlockEntity(pPos) instanceof GrinderEntity gE) {
            if(hasBucketItem()) {
                fillBucket();
            }
        }
        distributeFluid();
    }

    private void distributeFluid() {
        if (fluidTank.isEmpty()) {
            return;
        }
        IFluidHandler fHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos().relative(Direction.DOWN),Direction.DOWN);
        if (fHandler != null) {
            canAcceptFluid(fHandler);
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

    private void fillBucket() {
        IFluidHandlerItem fHandler = itemHandler.get().getStackInSlot(SLOT_DRAIN_FLUID).getCapability(Capabilities.FluidHandler.ITEM);
        if (fHandler != null){
            if (isBucketEmpty(fHandler)) {
                if (fluidTank.getFluid().getAmount() >= 1000) {
                    ItemStack filledBucket = FluidUtil.getFilledBucket(fluidTank.getFluid());
                    if (!filledBucket.isEmpty()) {
                        fluidTank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                        itemHandler.get().setStackInSlot(SLOT_DRAIN_FLUID,filledBucket);
                    }
                }
            }
        }
    }

    private boolean isBucketEmpty(IFluidHandlerItem fHandler) {
        return fHandler.getContainer().is(Items.BUCKET);
    }

    private boolean hasBucketItem() {
        return itemHandler.get().getStackInSlot(SLOT_DRAIN_FLUID).getCount() > 0 && itemHandler.get().getStackInSlot(SLOT_DRAIN_FLUID).is(Tags.Items.BUCKETS_EMPTY);
    }

    private static boolean hasFluidItemInSourceSlot(GrinderEntity pEntity) {

        return pEntity.itemHandler.get().getStackInSlot(SLOT_INPUT).getCount() > 0;
    }

    private void transferItemFluidToFluidTank(GrinderEntity pEntity) {
        IFluidHandlerItem fHandler = pEntity.itemHandler.get().getStackInSlot(SLOT_INPUT).getCapability(Capabilities.FluidHandler.ITEM);
        if (fHandler != null){
            int drainAmount = Math.min(pEntity.fluidTank.getSpace(), 1000);

        FluidStack stack = fHandler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
        if (pEntity.fluidTank.isFluidValid(stack)) {
            stack = fHandler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
            fillTankWithFluid(stack, fHandler.getContainer());
        }
        }
    }

    private void fillTankWithFluid(FluidStack stack, ItemStack container) {
        fluidTank.fill(stack, IFluidHandler.FluidAction.EXECUTE);

        itemHandler.get().extractItem(SLOT_INPUT, 1, false);
        itemHandler.get().insertItem(SLOT_INPUT, container, false);
    }

    public FluidStack getFluidStack() {
        return this.fluidTank.getFluid();
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
