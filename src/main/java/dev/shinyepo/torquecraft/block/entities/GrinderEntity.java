package dev.shinyepo.torquecraft.block.entities;

import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncFluidS2C;
import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
import dev.shinyepo.torquecraft.registries.TorqueBlockEntities;
import dev.shinyepo.torquecraft.registries.TorqueRecipes;
import dev.shinyepo.torquecraft.utils.AdaptedItemHandler;
import dev.shinyepo.torquecraft.utils.TorqueFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;

public class GrinderEntity extends BlockEntity {
    public static final String ITEMS_INPUT_TAG = "Input";
    public static final String ITEMS_OUTPUT_TAG = "Output";

    public int progress = 0;
    public int maxProgress = 64;

    private final int fluidCapacity = 2000;

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_INPUT_COUNT = 1;

    public static final int SLOT_OUTPUT = 0;
    public static final int SLOT_OUTPUT_COUNT = 1;
    public static final int SLOT_COUNT = SLOT_INPUT_COUNT + SLOT_OUTPUT_COUNT;

    private final ItemStackHandler inputItems = createItemHandler(SLOT_INPUT_COUNT);
    private final ItemStackHandler outputItems = createItemHandler(SLOT_OUTPUT_COUNT);
    private final Lazy<IItemHandler> itemHandler = Lazy.of(() -> new CombinedInvWrapper(inputItems, outputItems));
    private final Lazy<IItemHandler> inputItemHandler = Lazy.of(() -> new AdaptedItemHandler(inputItems) {
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    });
    private final Lazy<IItemHandler> outputItemHandler = Lazy.of(() -> new AdaptedItemHandler(outputItems) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }
    });

    private final TorqueFluidTank fluidTank = new TorqueFluidTank(fluidCapacity) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()) {
                TorqueMessages.sendToAllPlayers(new SyncFluidS2C(worldPosition, this.fluid));
            }
        }
    };

    public GrinderEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.GRINDER_ENTITY.get(), pPos, pBlockState);
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
        return pEntity.itemHandler.get().getStackInSlot(0).getCount() > 0;
    }

    private static void transferItemFluidToFluidTank(GrinderEntity pEntity) {
        IFluidHandlerItem fHandler = pEntity.itemHandler.get().getStackInSlot(0).getCapability(Capabilities.FluidHandler.ITEM);
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

        pEntity.itemHandler.get().extractItem(0, 1, false);
        pEntity.itemHandler.get().insertItem(0, container, false);
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

        this.itemHandler.get().extractItem(SLOT_INPUT,1, false);
        this.outputItems.setStackInSlot(SLOT_OUTPUT, new ItemStack(result.getItem(),
                this.outputItems.getStackInSlot(SLOT_OUTPUT).getCount() + result.getCount()));
        this.fluidTank.fill(resultFluid, IFluidHandler.FluidAction.EXECUTE);
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
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.get().getSlots());
        for (int i = 0; i < itemHandler.get().getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.get().getStackInSlot(i));
        }
        return this.level.getRecipeManager().getRecipeFor(TorqueRecipes.Types.GRINDING, inventory, level);
    }

    private boolean canOutputItem(Item item) {
            return this.outputItemHandler.get().getStackInSlot(SLOT_OUTPUT).isEmpty() || this.outputItemHandler.get().getStackInSlot(SLOT_OUTPUT).is(item);
    }

    private boolean canFitInOutput (int count) {
        return this.outputItemHandler.get().getStackInSlot(SLOT_OUTPUT).getCount() + count <= this.outputItemHandler.get().getStackInSlot(SLOT_OUTPUT).getMaxStackSize();
    }

    private boolean fluidsMatch(FluidStack resultFluid) {
        return this.fluidTank.getFluid().is(resultFluid.getFluid()) || this.fluidTank.isEmpty();
    }

    private boolean canFitInTank (FluidStack resultFluid) {
        return this.fluidTank.getFluid().getAmount() + resultFluid.getAmount() <= this.fluidTank.getCapacity();
    }

    public TorqueFluidTank getFluidTank() {return fluidTank; }

    public ItemStackHandler getInputItems() {
        return inputItems;
    }

    public ItemStackHandler getOutputItems() {
        return outputItems;
    }

    public Lazy<IItemHandler> getItemHandler() {
        return itemHandler;
    }

    public Lazy<IItemHandler> getInputItemHandler() {
        return inputItemHandler;
    }

    public Lazy<IItemHandler> getOutputItemHandler() {
        return outputItemHandler;
    }

    @Nonnull
    private ItemStackHandler createItemHandler(int slots) {
        return new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        fluidTank.writeToNBT(provider,tag);
        tag.put(ITEMS_INPUT_TAG, inputItems.serializeNBT(provider));
        tag.put(ITEMS_OUTPUT_TAG, outputItems.serializeNBT(provider));
        tag.putInt("grinder.progress", progress);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag,provider);
        fluidTank.readFromNBT(provider,tag);
        if (tag.contains(ITEMS_INPUT_TAG)) {
            inputItems.deserializeNBT(provider, tag.getCompound(ITEMS_INPUT_TAG));
        }
        if (tag.contains(ITEMS_OUTPUT_TAG)) {
            outputItems.deserializeNBT(provider, tag.getCompound(ITEMS_OUTPUT_TAG));
        }
        tag.getInt("grinder.progress");
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag nbt = super.getUpdateTag(provider);
        saveAdditional(nbt, provider);
        return nbt;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.get().getSlots());
        for (int i = 0; i < itemHandler.get().getSlots(); i++) {
            inventory.setItem(i, itemHandler.get().getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
}
