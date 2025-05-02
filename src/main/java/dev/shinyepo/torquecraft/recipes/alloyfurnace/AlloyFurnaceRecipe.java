package dev.shinyepo.torquecraft.recipes.alloyfurnace;

import dev.shinyepo.torquecraft.registries.recipe.TorqueRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AlloyFurnaceRecipe implements Recipe<AlloyFurnaceInput> {
    protected final List<Ingredient> addonIngredient;
    protected final Ingredient ingotIngredient;
    protected final double temp;
    protected final ItemStack resultItem;

    public AlloyFurnaceRecipe(List<Ingredient> addonIngredient, Ingredient ingotIngredient, double temp, ItemStack resultItem) {
        this.addonIngredient = addonIngredient;
        this.ingotIngredient = ingotIngredient;
        this.temp = temp;
        this.resultItem = resultItem;
    }

    @Override
    public boolean matches(AlloyFurnaceInput pInput, @NotNull Level pLevel) {
        var ingots = getIngotIngredient();
        var addonItems = new ArrayList<ItemStack>();
        for (int i = 0; i < pInput.size() - 1; i++) {
            if (pInput.getItem(i).is(ItemStack.EMPTY.getItem())) continue;
            var item = pInput.getItem(i);
            if (addonItems.stream().anyMatch(x -> x.getItem() == item.getItem())) continue;
            addonItems.add(pInput.getItem(i));
        }
        if (addonItems.size() != addonIngredient.size()) return false;
        var matchingAddons = false;
        for (Ingredient ingredient : addonIngredient) {
            for (var item : addonItems) {
                matchingAddons = ingredient.test(item);
                if (matchingAddons) break;
            }
            if (!matchingAddons) return false;
        }
        var matches = ingots.test(pInput.getItem(pInput.size() - 1));

        return matches && matchingAddons;
    }

    public Ingredient getIngotIngredient() {
        return ingotIngredient;
    }

    public List<Ingredient> getAddonIngredient() {
        return addonIngredient;
    }

    public double getTemp() {
        return temp;
    }

    public ItemStack getResultItem() {
        return resultItem;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull AlloyFurnaceInput pInput, HolderLookup.@NotNull Provider pRegistries) {
        return this.resultItem.copy();
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<AlloyFurnaceInput>> getSerializer() {
        return TorqueRecipes.Serializers.ALLOY_SMELTING_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<AlloyFurnaceInput>> getType() {
        return TorqueRecipes.Types.ALLOY_SMELTING.get();
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.FURNACE_BLOCKS;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
