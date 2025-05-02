package dev.shinyepo.torquecraft.recipes;

import dev.shinyepo.torquecraft.recipes.grinding.GrinderRecipe;
import dev.shinyepo.torquecraft.registries.recipe.TorqueRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class TorqueRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final ItemStack resultItem;
    private FluidStack resultFluid;
    private GrinderRecipe.InputItem ingredient;
    private final HolderLookup.RegistryLookup<Item> items;
    private ResourceKey<Recipe<?>> recipeResourceKey;


    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    public static TorqueRecipeBuilder begin(HolderLookup.RegistryLookup<Item> items, RecipeCategory cat, ItemLike result, int count) {
        return new TorqueRecipeBuilder(items, cat, new ItemStack(result, count));
    }

    public TorqueRecipeBuilder(HolderLookup.RegistryLookup<Item> items, RecipeCategory category, ItemStack resultItem) {
        this.items = items;
        this.category = category;
        this.resultItem = resultItem;
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String pName, @NotNull Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);
        return this;
    }

    public TorqueRecipeBuilder withIngredient(Ingredient ingredient) {
        this.ingredient = GrinderRecipe.InputItem.of(ingredient,1);
        withRecipeResourcePath(TorqueRecipes.Types.GRINDING.get(),getItemPath(ingredient.getValues().get(0).value()));
        return this;
    }

    public TorqueRecipeBuilder withIngredient(Item item) {
        return withIngredient(Ingredient.of(item));
    }

    public TorqueRecipeBuilder withIngredient(TagKey<Item> tag) {
        withRecipeResourcePath(TorqueRecipes.Types.GRINDING.get(), tag.location().getPath());
        this.ingredient = GrinderRecipe.InputItem.of(Ingredient.of(items.getOrThrow(tag)),1);
        return this;
    }

    public TorqueRecipeBuilder withResultFluid(FluidStack fluid) {
        this.resultFluid = fluid;
        return this;
    }

    public void withRecipeResourcePath(RecipeType<GrinderRecipe> recipeType, String path) {
        this.recipeResourceKey = ResourceKey.create(Registries.RECIPE, ResourceLocation.parse(recipeType + "/" + getItemPath(resultItem.getItem()) + "_from_" + path));
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


    private String getItemPath(Item item) {

        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    @Override
    public void save(@NotNull RecipeOutput pRecipeOutput) {
        this.save(pRecipeOutput, this.recipeResourceKey);
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceKey<Recipe<?>> pId) {
        this.ensureValid(pId.location());
        Advancement.Builder advancement = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);

        GrinderRecipe grinderRecipe = new GrinderRecipe(ingredient, resultItem, resultFluid);
        pRecipeOutput.accept(pId, grinderRecipe, advancement.build(pId.location().withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }
}
