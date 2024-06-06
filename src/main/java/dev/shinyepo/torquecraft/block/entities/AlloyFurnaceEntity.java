package dev.shinyepo.torquecraft.block.entities;

import dev.shinyepo.torquecraft.capabilities.handlers.AdaptedItemHandler;
import dev.shinyepo.torquecraft.constants.TorqueNBT;
import dev.shinyepo.torquecraft.factory.StandaloneMachineFactory;
import dev.shinyepo.torquecraft.recipes.custom.AlloyFurnaceRecipe;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import dev.shinyepo.torquecraft.registries.recipe.TorqueRecipes;
import dev.shinyepo.torquecraft.registries.tag.TorqueTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AlloyFurnaceEntity extends StandaloneMachineFactory {
    public static final int ADDON_SLOT_COUNT = 3;
    public static final int INPUT_SLOT_COUNT = 9;
    public static final int OUTPUT_SLOT_COUNT = 3;
    public static final int SLOT_COUNT = ADDON_SLOT_COUNT + INPUT_SLOT_COUNT + OUTPUT_SLOT_COUNT + 1;
    private static final List<TagKey<Item>> validAddonsInputs = List.of(ItemTags.COALS, Tags.Items.GUNPOWDERS, Tags.Items.SANDS, Tags.Items.SANDSTONE_BLOCKS, TorqueTags.SILICON);
    private static final List<TagKey<Item>> validInputs = List.of(Tags.Items.INGOTS);
    private final Lazy<ItemStackHandler> addonItemHandler = createInputItemHandler(ADDON_SLOT_COUNT, validAddonsInputs);
    private final Lazy<ItemStackHandler> inputItemHandler = createInputItemHandler(INPUT_SLOT_COUNT, validInputs);
    private final Lazy<ItemStackHandler> outputItemHandler = createOutputItemHandler(OUTPUT_SLOT_COUNT);
    private final Lazy<ItemStackHandler> fuelItemHandler = createInputItemHandler(1, List.of(ItemTags.COALS));

    private final Lazy<AdaptedItemHandler> addonInput = createAutoInputHandler(addonItemHandler.get());
    private final Lazy<AdaptedItemHandler> craftInput = createAutoInputHandler(inputItemHandler.get());
    private final Lazy<AdaptedItemHandler> resultOutput = createAutoOutputHandler(outputItemHandler.get());

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> burnTime;
                case 1 -> maxBurnTime;
                case 2 -> progress;
                case 3 -> maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> burnTime = pValue;
                case 1 -> maxBurnTime = pValue;
                case 2 -> progress = pValue;
                case 3 -> maxProgress = pValue;
            }
            ;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

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

    public ContainerData getGuiData() {
        return data;
    }
    //GUI end

    private int getBurnTime(ItemStack fuel) {
        return fuel.getBurnTime(RecipeType.SMELTING);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (this.burnTime > 0) {
            this.burnTime--;
        } else {
            ItemStack fuel = fuelItemHandler.get().getStackInSlot(0);
            int burnTime = getBurnTime(fuel);
            this.maxBurnTime = burnTime;
            if (!fuel.isEmpty() && getBurnTime(fuel) > 0 && hasRecipe()) {
                this.burnTime = burnTime;
                fuel.setCount(fuel.getCount() - 1);
                fuelItemHandler.get().setStackInSlot(0, fuel);
                pLevel.setBlockAndUpdate(pPos, pState.setValue(BlockStateProperties.LIT, true));
            } else {
                this.maxBurnTime = 0;
                pLevel.setBlockAndUpdate(pPos, pState.setValue(BlockStateProperties.LIT, false));
            }
        }
        if (hasRecipe() && burnTime > 0) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);
            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
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

    private void craftItem() {
        Optional<RecipeHolder<AlloyFurnaceRecipe>> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().value().getResultItem(null);
        ItemStack ingot = recipe.get().value().getIngotIngredient().getItems()[0];
        for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
            if (inputItemHandler.get().getStackInSlot(i).is(ingot.getItem())) {
                int outputSlot = canFitInOutput(1, result.getItem());
                if (outputSlot != -1) {
                    inputItemHandler.get().extractItem(i, 1, false);
                    outputItemHandler.get().setStackInSlot(outputSlot, new ItemStack(result.getItem(), this.outputItemHandler.get().getStackInSlot(outputSlot).getCount() + 1));
                }
            }
        }
        useAddonItems();
    }

    private void useAddonItems() {
        for (int i = 0; i < ADDON_SLOT_COUNT; i++) {
            var stack = addonItemHandler.get().getStackInSlot(i);
            addonItemHandler.get().setStackInSlot(i, new ItemStack(stack.getItem(), stack.getCount() - 1));
        }
    }

    //
//
//
    private boolean hasRecipe() {
        Optional<RecipeHolder<AlloyFurnaceRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) return false;

        ItemStack result = recipe.get().value().getResultItem(null);
        return canOutputItem(result.getItem()) && canFitInOutput(1, result.getItem()) != -1;
    }

    private Optional<RecipeHolder<AlloyFurnaceRecipe>> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(4);
        for (int i = 0; i < ADDON_SLOT_COUNT; i++) {
            inventory.setItem(i, addonItemHandler.get().getStackInSlot(i));
        }
        for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
            var ingot = inputItemHandler.get().getStackInSlot(i);
            if (!ingot.isEmpty()) {
                inventory.setItem(3, ingot);
                break;
            }
        }

        return this.level.getRecipeManager().getRecipeFor(TorqueRecipes.Types.ALLOY_SMELTING, inventory, this.level);
    }

    private boolean canOutputItem(Item item) {
        for (int i = 0; i < OUTPUT_SLOT_COUNT; i++) {
            if (this.outputItemHandler.get().getStackInSlot(i).isEmpty() || this.outputItemHandler.get().getStackInSlot(i).is(item)) {
                return true;
            }
        }
        return false;
    }

    private int canFitInOutput(int count, Item item) {
        for (int i = 0; i < OUTPUT_SLOT_COUNT; i++) {
            if (outputItemHandler.get().getStackInSlot(i).is(item) || outputItemHandler.get().getStackInSlot(i).isEmpty()) {
                if (outputItemHandler.get().getStackInSlot(i).getCount() + count <= this.outputItemHandler.get().getStackInSlot(i).getMaxStackSize()) {
                    return i;
                }
            }
        }
        return -1;
    }


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
