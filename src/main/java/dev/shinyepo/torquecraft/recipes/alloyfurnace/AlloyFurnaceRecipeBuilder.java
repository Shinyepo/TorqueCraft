package dev.shinyepo.torquecraft.recipes.alloyfurnace;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AlloyFurnaceRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final RecipeType<?> recipeType;
    private final List<Ingredient> addonIngredient = new ArrayList<>();
    private final Ingredient ingotIngredient;
    private double temp = 0;
    private final ItemStack resultItem;


    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    public AlloyFurnaceRecipeBuilder(RecipeType<?> recipeType, RecipeCategory category, Ingredient ingotIngredient, ItemStack resultItem) {
        this.category = category;
        this.recipeType = recipeType;
        this.ingotIngredient = ingotIngredient;
        this.resultItem = resultItem;
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String pName, @NotNull Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return this.resultItem.getItem();
    }

    public AlloyFurnaceRecipeBuilder addAddonIngredient(ItemLike item) {
        addonIngredient.add(Ingredient.of(item));
        return this;
    }

    public AlloyFurnaceRecipeBuilder setTemp(double temp) {
        this.temp = temp;
        return this;
    }

    private String getResultPath() {
        return BuiltInRegistries.ITEM.getKey(getResult().asItem()).getPath();
    }

    @Override
    public void save(@NotNull RecipeOutput pRecipeOutput) {
        this.save(pRecipeOutput, ResourceKey.create(Registries.RECIPE,ResourceLocation.parse(this.recipeType + "/" + getResultPath())));
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceKey<Recipe<?>> pId) {
        this.ensureValid(pId.location());
        Advancement.Builder advancement = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);

        AlloyFurnaceRecipe alloyFurnaceRecipe = new AlloyFurnaceRecipe(addonIngredient, ingotIngredient, temp, resultItem);
        pRecipeOutput.accept(pId, alloyFurnaceRecipe, advancement.build(pId.location().withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }
}
