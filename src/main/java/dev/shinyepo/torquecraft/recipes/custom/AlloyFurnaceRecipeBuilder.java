package dev.shinyepo.torquecraft.recipes.custom;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AlloyFurnaceRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final RecipeType<?> recipeType;
    private final List<Ingredient> addonIngredient = new ArrayList<>();
    private final Ingredient ingotIngredient;
    private final ItemStack resultItem;


    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private final AlloyFurnaceRecipe.Factory<?> factory;
    @Nullable
    private String group;

    public AlloyFurnaceRecipeBuilder(RecipeType<?> recipeType, RecipeCategory category, AlloyFurnaceRecipe.Factory<?> factory, Ingredient ingotIngredient, ItemStack resultItem) {
        this.category = category;
        this.recipeType = recipeType;
        this.ingotIngredient = ingotIngredient;
        this.factory = factory;
        this.resultItem = resultItem;
    }

    @Override
    public RecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    @Override
    public Item getResult() {
        return this.resultItem.getItem();
    }

    public AlloyFurnaceRecipeBuilder addAddonIngredient(TagKey<Item> tag) {
        addonIngredient.add(Ingredient.of(tag));
        return this;
    }

    public AlloyFurnaceRecipeBuilder addAddonIngredient(ItemLike item) {
        addonIngredient.add(Ingredient.of(item));
        return this;
    }

    private String getResultPath() {
        return BuiltInRegistries.ITEM.getKey(getResult().asItem()).getPath();
    }

    @Override
    public void save(RecipeOutput pRecipeOutput) {
        this.save(pRecipeOutput, new ResourceLocation(this.recipeType + "/" + getResultPath()));
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId) {
        this.ensureValid(pId);
        Advancement.Builder advancement$builder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        AlloyFurnaceRecipe alloyFurnaceRecipe = this.factory
                .create(Objects.requireNonNullElse(this.group, ""), addonIngredient, ingotIngredient, resultItem);
        pRecipeOutput.accept(pId, alloyFurnaceRecipe, advancement$builder.build(pId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }
}
