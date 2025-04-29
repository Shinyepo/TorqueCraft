package dev.shinyepo.torquecraft.block.entities;

import dev.shinyepo.torquecraft.capabilities.handlers.AdaptedItemHandler;
import dev.shinyepo.torquecraft.constants.TorqueNBT;
import dev.shinyepo.torquecraft.factory.StandaloneMachineFactory;
import dev.shinyepo.torquecraft.recipes.custom.AlloyFurnaceRecipe;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import dev.shinyepo.torquecraft.registries.recipe.TorqueRecipes;
import dev.shinyepo.torquecraft.registries.tag.TorqueTags;
import dev.shinyepo.torquecraft.utils.IHeatedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.*;

import static dev.shinyepo.torquecraft.utils.HeatSource.adjustTemp;

public class AlloyFurnaceEntity extends StandaloneMachineFactory implements IHeatedEntity {
    private static final int ADDON_SLOT_COUNT = 3;
    private static final int INPUT_SLOT_COUNT = 9;
    private static final int OUTPUT_SLOT_COUNT = 3;
    public static final int SLOT_COUNT = ADDON_SLOT_COUNT + INPUT_SLOT_COUNT + OUTPUT_SLOT_COUNT;
    private static final List<TagKey<Item>> validAddonsInputs = List.of(ItemTags.COALS, Tags.Items.GUNPOWDERS, Tags.Items.SANDS, Tags.Items.SANDSTONE_BLOCKS, TorqueTags.SILICON);
    private static final List<TagKey<Item>> validInputs = List.of(Tags.Items.INGOTS);
    private final Lazy<ItemStackHandler> addonItemHandler = createInputItemHandler(ADDON_SLOT_COUNT, validAddonsInputs);
    private final Lazy<ItemStackHandler> inputItemHandler = createInputItemHandler(INPUT_SLOT_COUNT, validInputs);
    private final Lazy<ItemStackHandler> outputItemHandler = createOutputItemHandler(OUTPUT_SLOT_COUNT);

    private final Lazy<AdaptedItemHandler> addonInput = createAutoInputHandler(addonItemHandler.get());
    private final Lazy<AdaptedItemHandler> craftInput = createAutoInputHandler(inputItemHandler.get());
    private final Lazy<AdaptedItemHandler> resultOutput = createAutoOutputHandler(outputItemHandler.get());

    protected double temp = 0;
    protected float maxTemp = 2100;
    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> (int) temp;
                case 1 -> (int) maxTemp;
                case 2 -> progress;
                case 3 -> maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> temp = pValue;
                case 1 -> maxTemp = pValue;
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
    private AlloyFurnaceRecipe recipe;

    public AlloyFurnaceEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.ALLOY_FURNACE_ENTITY.get(), pPos, pBlockState);
        registerHandlers();
    }

    private void registerHandlers() {
        Map<String, ItemStackHandler> toRegister = new HashMap<>();
        toRegister.put(TorqueNBT.INPUT, inputItemHandler.get());
        toRegister.put(TorqueNBT.OUTPUT, outputItemHandler.get());
        toRegister.put(TorqueNBT.ADDON, addonItemHandler.get());
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

    public IItemHandler getAddon() {
        return addonItemHandler.get();
    }

    public ContainerData getGuiData() {
        return data;
    }
    //GUI end

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (this.level.getGameTime() % 20 == 0)
            adjustTemp(this);

        if (hasRecipe()) {
            if (temp > this.recipe.getTemp()) {
                increaseCraftingProgress();
                setChanged(pLevel, pPos, pState);
                if (hasProgressFinished()) {
                    craftItem();
                    resetProgress();
                }
            }
        } else {
            resetProgress();
        }
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    private void craftItem() {
        Optional<RecipeHolder<AlloyFurnaceRecipe>> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().value().getResultItem(null);
        ItemStack ingot = recipe.get().value().getIngotIngredient().getItems()[0];
        List<Ingredient> addons = recipe.get().value().getAddonIngredient();
        for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
            if (inputItemHandler.get().getStackInSlot(i).is(ingot.getItem())) {
                int outputSlot = canFitInOutput(1, result.getItem());
                if (outputSlot != -1) {
                    inputItemHandler.get().extractItem(i, 1, false);
                    outputItemHandler.get().setStackInSlot(outputSlot, new ItemStack(result.getItem(), this.outputItemHandler.get().getStackInSlot(outputSlot).getCount() + 1));
                }
            }
        }
        useAddonItems(addons);
    }

    private void useAddonItems(List<Ingredient> addons) {
        List<Item> removed = new ArrayList<>();
        for (Ingredient addon : addons) {
            for (int j = 0; j < 3; j++) {
                var item = addonItemHandler.get().getStackInSlot(j);
                var matches = addon.test(item);
                if (matches && !removed.contains(item.getItem())) {
                    addonItemHandler.get().setStackInSlot(j, new ItemStack(item.getItem(), item.getCount() - 1));
                    removed.add(item.getItem());
                }
            }
        }
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<AlloyFurnaceRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) return false;

        ItemStack result = recipe.get().value().getResultItem(null);
        if (this.recipe != recipe.get().value()) resetProgress();
        this.recipe = recipe.get().value();
        return canOutputItem(result.getItem()) && canFitInOutput(1, result.getItem()) != -1;
    }

    private Optional<RecipeHolder<AlloyFurnaceRecipe>> getCurrentRecipe() {
        CraftingInput inventory = getItemsInSlots();

        return this.level.getRecipeManager().getRecipeFor(TorqueRecipes.Types.ALLOY_SMELTING, inventory, this.level);
    }

    private CraftingInput getItemsInSlots() {

        List<ItemStack> inputs = new ArrayList<>(4);
        for (int i = 0; i < ADDON_SLOT_COUNT; i++) {
            inputs.add(addonItemHandler.get().getStackInSlot(i));
        }
        for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
            var ingot = inputItemHandler.get().getStackInSlot(i);
            if (i == 8) {
                inputs.add(3, ingot);
                break;
            }
            if (!ingot.isEmpty()) {
                inputs.add(ingot);
                break;
            }
        }
        return CraftingInput.of(4, 1, inputs);
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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putDouble(TorqueNBT.TEMP, (double) Math.round(getTemp() * 100) / 100);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains(TorqueNBT.ADDON)) {
            addonItemHandler.get().deserializeNBT(provider, tag.getCompound(TorqueNBT.ADDON).get());
        }
        if (tag.contains(TorqueNBT.INPUT)) {
            inputItemHandler.get().deserializeNBT(provider, tag.getCompound(TorqueNBT.INPUT).get());
        }
        if (tag.contains(TorqueNBT.OUTPUT)) {
            outputItemHandler.get().deserializeNBT(provider, tag.getCompound(TorqueNBT.OUTPUT).get());
        }
        if (tag.contains(TorqueNBT.TEMP)) {
            temp = tag.getDouble(TorqueNBT.TEMP).get();
        }
    }
}
