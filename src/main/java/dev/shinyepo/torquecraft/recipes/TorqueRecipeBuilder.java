package dev.shinyepo.torquecraft.recipes;

import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class TorqueRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item resultItem;
    private final int count;
    private final FluidStack resultFluid;
    private final int fluidAmount;
    private final Ingredient ingredient;


    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;
    private final GrinderRecipe.Factory<?> factory;

    public TorqueRecipeBuilder(RecipeCategory category, GrinderRecipe.Factory<?> factory, Ingredient ingredient, ItemLike resultItem, int count, FluidStack resultFluid, int amount) {
        this.category = category;
        this.factory = factory;
        this.ingredient = ingredient;
        this.resultItem = resultItem.asItem();
        this.count = count;
        this.resultFluid = resultFluid;
        this.fluidAmount = amount;
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
        return this.resultItem;
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
                .create(Objects.requireNonNullElse(this.group, ""), this.ingredient, new ItemStack(this.resultItem, this.count), new FluidStack(this.resultFluid.getFluidHolder(), this.fluidAmount));
        pRecipeOutput.accept(pId, grinderRecipe, advancement$builder.build(pId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }
}
