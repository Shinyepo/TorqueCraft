package dev.shinyepo.torquecraft.recipes;

import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class TorqueRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final RecipeType<?> recipeType;
    private final ItemStack resultItem;
    private final FluidStack resultFluid;
    private final Ingredient ingredient;


    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;
    private final GrinderRecipe.Factory<?> factory;

    public TorqueRecipeBuilder(RecipeType<?> recipeType, RecipeCategory category, GrinderRecipe.Factory<?> factory, Ingredient ingredient, ItemStack resultItem, FluidStack resultFluid) {
        this.category = category;
        this.recipeType = recipeType;
        this.factory = factory;
        this.ingredient = ingredient;
        this.resultItem = resultItem;
        this.resultFluid = resultFluid;
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

    private String getResultPath() {
        return BuiltInRegistries.ITEM.getKey(getResult().asItem()).getPath();
    }

    @Override
    public void save(RecipeOutput pRecipeOutput) {
        this.save(pRecipeOutput, ResourceLocation.parse(this.recipeType + "/" + getResultPath()));
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId) {
        this.ensureValid(pId);
        Advancement.Builder advancement$builder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        GrinderRecipe grinderRecipe = this.factory
                .create(Objects.requireNonNullElse(this.group, ""), ingredient, resultItem, resultFluid);
        pRecipeOutput.accept(pId, grinderRecipe, advancement$builder.build(pId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }
}
