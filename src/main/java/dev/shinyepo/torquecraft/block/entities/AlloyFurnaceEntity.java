package dev.shinyepo.torquecraft.block.entities;

import dev.shinyepo.torquecraft.capabilities.handlers.AdaptedItemHandler;
import dev.shinyepo.torquecraft.constants.TorqueNBT;
import dev.shinyepo.torquecraft.factory.StandaloneMachineFactory;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlloyFurnaceEntity extends StandaloneMachineFactory {
    public static final int ADDON_SLOT_COUNT = 3;
    public static final int INPUT_SLOT_COUNT = 9;
    public static final int OUTPUT_SLOT_COUNT = 3;
    public static final int SLOT_COUNT = ADDON_SLOT_COUNT + INPUT_SLOT_COUNT + OUTPUT_SLOT_COUNT;
    private static final List<TagKey<Item>> validAddonsInputs = List.of(ItemTags.COALS, Tags.Items.GUNPOWDERS, Tags.Items.SANDS, Tags.Items.SANDSTONE_BLOCKS);
    private static final List<TagKey<Item>> validInputs = List.of(Tags.Items.INGOTS);
    private final Lazy<ItemStackHandler> addonItemHandler = createInputItemHandler(ADDON_SLOT_COUNT, validAddonsInputs);
    private final Lazy<ItemStackHandler> inputItemHandler = createInputItemHandler(INPUT_SLOT_COUNT, validInputs);
    private final Lazy<ItemStackHandler> outputItemHandler = createOutputItemHandler(OUTPUT_SLOT_COUNT);
    private final Lazy<ItemStackHandler> fuelItemHandler = createInputItemHandler(1, List.of(ItemTags.COALS));

    private final Lazy<AdaptedItemHandler> addonInput = createAutoInputHandler(addonItemHandler.get());
    private final Lazy<AdaptedItemHandler> craftInput = createAutoInputHandler(inputItemHandler.get());
    private final Lazy<AdaptedItemHandler> resultOutput = createAutoOutputHandler(outputItemHandler.get());

    private final Lazy<CombinedInvWrapper> combinedItemHandlers = createCombinedHandler(addonItemHandler.get(), inputItemHandler.get(), outputItemHandler.get());

    public AlloyFurnaceEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.ALLOY_FURNACE_ENTITY.get(), pPos, pBlockState);
        registerHandlers();
    }

    private void registerHandlers() {
        Map<String, ItemStackHandler> toRegister = new HashMap<>();
        toRegister.put(TorqueNBT.INPUT, inputItemHandler.get());
        toRegister.put(TorqueNBT.OUTPUT, outputItemHandler.get());
        toRegister.put(TorqueNBT.ADDON, addonItemHandler.get());
        toRegister.put(TorqueNBT.FUEL, fuelItemHandler.get());
        super.registerHandlers(toRegister);
    }

    //Capability stuff
    public IItemHandler getItemHandler(Direction dir) {
        if (dir == Direction.UP)
            return craftInput.get();
        if (dir == Direction.DOWN)
            return resultOutput.get();
        return addonInput.get();
    }

    //GUI
    public IItemHandler getInput() {
        return inputItemHandler.get();
    }

    public IItemHandler getOutput() {
        return outputItemHandler.get();
    }

    public IItemHandler getFuelHandler() {
        return fuelItemHandler.get();
    }

    public IItemHandler getAddon() {
        return addonItemHandler.get();
    }
    //GUI end

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
//        if (hasRecipe()) {
//            increaseCraftingProgress();
//            setChanged(pLevel,pPos,pState);
//
//            if(hasProgressFinished()) {
//                craftItem();
//                resetProgress();
//            }
//        } else {
//            resetProgress();
//        }
    }

//    private void craftItem() {
//        Optional<RecipeHolder<GrinderRecipe>> recipe = getCurrentRecipe();
//        ItemStack result = recipe.get().value().getResultItem(null);
//        FluidStack resultFluid = recipe.get().value().getResultFluid(null);
//        int fluidAmount = resultFluid.getAmount();
//        if (this.itemHandler.get().getStackInSlot(SLOT_INPUT).is(TorqueItems.CANOLA_SEEDS.get())) {
//            fluidAmount = fluidAmount * 2;
//        }
//        this.itemHandler.get().extractItem(SLOT_INPUT,1, false);
//        this.itemHandler.get().setStackInSlot(SLOT_OUTPUT, new ItemStack(result.getItem(),
//                this.itemHandler.get().getStackInSlot(SLOT_OUTPUT).getCount() + result.getCount()));
//    }
//
//
//
//    private boolean hasRecipe() {
//        Optional<RecipeHolder<GrinderRecipe>> recipe = getCurrentRecipe();
//
//        if (recipe.isEmpty()) return false;
//
//        ItemStack result = recipe.get().value().getResultItem(null);
//        return canFitInOutput(result.getCount()) && canOutputItem(result.getItem());
//    }
//
//    private Optional<RecipeHolder<GrinderRecipe>> getCurrentRecipe() {
//        SimpleContainer inventory = new SimpleContainer(1);
//        for (int i = 0; i < 1; i++) {
//            inventory.setItem(i, this.itemHandler.get().getStackInSlot(0));
//        }
//        return this.level.getRecipeManager().getRecipeFor(TorqueRecipes.Types.GRINDING, inventory, this.level);
//    }
//
//    private boolean canOutputItem(Item item) {
//            return this.itemHandler.get().getStackInSlot(SLOT_OUTPUT).isEmpty() || this.itemHandler.get().getStackInSlot(1).is(item);
//    }
//
//    private boolean canFitInOutput (int count) {
//        return this.itemHandler.get().getStackInSlot(SLOT_OUTPUT).getCount() + count <= this.itemHandler.get().getStackInSlot(1).getMaxStackSize();
//    }


    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains(TorqueNBT.ADDON)) {
            addonItemHandler.get().deserializeNBT(provider, tag.getCompound(TorqueNBT.ADDON));
        }
        if (tag.contains(TorqueNBT.INPUT)) {
            inputItemHandler.get().deserializeNBT(provider, tag.getCompound(TorqueNBT.INPUT));
        }
        if (tag.contains(TorqueNBT.OUTPUT)) {
            outputItemHandler.get().deserializeNBT(provider, tag.getCompound(TorqueNBT.OUTPUT));
        }
        if (tag.contains(TorqueNBT.FUEL)) {
            fuelItemHandler.get().deserializeNBT(provider, tag.getCompound(TorqueNBT.FUEL));
        }
    }
}
