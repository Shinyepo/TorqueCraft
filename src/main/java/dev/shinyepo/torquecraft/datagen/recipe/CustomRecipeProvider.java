package dev.shinyepo.torquecraft.datagen.recipe;

import dev.shinyepo.torquecraft.recipes.TorqueRecipeBuilder;
import dev.shinyepo.torquecraft.recipes.custom.AlloyFurnaceRecipe;
import dev.shinyepo.torquecraft.recipes.custom.AlloyFurnaceRecipeBuilder;
import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
import dev.shinyepo.torquecraft.registries.recipe.TorqueRecipes;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.resources.ResourceLocation.withDefaultNamespace;

public abstract class CustomRecipeProvider extends RecipeProvider {
    public CustomRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    protected static void grinding(ItemLike ingredient, ItemLike result, FluidStack resultFluid, RecipeOutput output) {
        grinding(Ingredient.of(ingredient), getHasName(ingredient), has(ingredient), result, 1, resultFluid, output);
    }

    protected static void grinding(ItemLike ingredient, ItemLike result, int count, FluidStack resultFluid, RecipeOutput output) {
        grinding(Ingredient.of(ingredient), getHasName(ingredient), has(ingredient), result, count, resultFluid, output);

    }

    protected static void grinding(ItemLike ingredient, ItemLike result, int count, RecipeOutput output) {
        grinding(Ingredient.of(ingredient), getHasName(ingredient), has(ingredient), result, count, FluidStack.EMPTY, output);
    }

    protected static void grinding(ItemLike ingredient, ItemLike result, RecipeOutput output) {
        grinding(Ingredient.of(ingredient), getHasName(ingredient), has(ingredient), result, 1, FluidStack.EMPTY, output);
    }

    protected static void grinding(TagKey<Item> ingredient, ItemLike result, RecipeOutput output) {
        grinding(Ingredient.of(ingredient), "has_" + formatTagName(ingredient), has(ingredient), result, 1, FluidStack.EMPTY, output);
    }

    protected static void grinding(TagKey<Item> ingredient, ItemLike result, int count, RecipeOutput output) {
        grinding(Ingredient.of(ingredient), "has_" + formatTagName(ingredient), has(ingredient), result, count, FluidStack.EMPTY, output);
    }

    protected static void grinding(TagKey<Item> ingredient, ItemLike result, FluidStack resultFluid, RecipeOutput output) {
        grinding(Ingredient.of(ingredient), "has_" + formatTagName(ingredient), has(ingredient), result, 1, resultFluid, output);
    }

    protected static void grinding(TagKey<Item> ingredient, ItemLike result, int count, FluidStack resultFluid, RecipeOutput output) {
        grinding(Ingredient.of(ingredient), "has_" + formatTagName(ingredient), has(ingredient), result, count, resultFluid, output);
    }

    protected static void grinding(Ingredient ingredient, String criterionName, Criterion<?> criterion, ItemLike result, int count, FluidStack resultFluid, RecipeOutput output) {
        new TorqueRecipeBuilder(TorqueRecipes.Types.GRINDING, RecipeCategory.MISC, GrinderRecipe::new, ingredient, new ItemStack(result, count), resultFluid)
                .unlockedBy(criterionName, criterion)
                .save(output);
    }

    protected static AlloyFurnaceRecipeBuilder alloying(Ingredient ingotIngredient, ItemLike result) {
        return new AlloyFurnaceRecipeBuilder(TorqueRecipes.Types.ALLOY_SMELTING, RecipeCategory.MISC, AlloyFurnaceRecipe::new, ingotIngredient, new ItemStack(result, 1));
    }

    private static String formatTagName(TagKey<Item> tag) {
        return String.join("_", tag.location().toString().split(":")[1].split("/"));
    }

    private static String getMaterialName(ItemLike item) {
        return item.toString().split(":")[1].split("_")[0];
    }

    protected void registerDustRecipes(ItemLike dustItem, RecipeOutput output) {
        String dustName = getMaterialName(dustItem);
        var ingot = BuiltInRegistries.ITEM.getOptional(withDefaultNamespace(dustName + "_ingot"));
        var ore = BuiltInRegistries.ITEM.getOptional(withDefaultNamespace(dustName + "_ore"));
        var deepslateOre = BuiltInRegistries.ITEM.getOptional(withDefaultNamespace("deepslate_" + dustName + "_ore"));
        var raw = BuiltInRegistries.ITEM.getOptional(withDefaultNamespace("raw_" + dustName));
        var rawBlock = BuiltInRegistries.ITEM.getOptional(withDefaultNamespace("raw_" + dustName + "_block"));


        ingot.ifPresent(item -> {
            grinding(item, dustItem, FluidStack.EMPTY, output);
            smeltingResultFromBase(output, item, dustItem);
            oreBlasting(output, List.of(dustItem), RecipeCategory.MISC, item, 0.7F, 100, dustName + "_ingot");
        });
        ore.ifPresent(item -> grinding(item, dustItem, 2, FluidStack.EMPTY, output));
        deepslateOre.ifPresent(item -> grinding(item, dustItem, 2, FluidStack.EMPTY, output));
        raw.ifPresent(item -> grinding(item, dustItem, FluidStack.EMPTY, output));
        rawBlock.ifPresent(item -> grinding(item, dustItem, 9, FluidStack.EMPTY, output));
    }

    protected void buildVanillaRecipes(RecipeOutput output) {
        grinding(Items.STONE, Items.COBBLESTONE, output);
        grinding(Items.COBBLESTONE, Items.GRAVEL, output);
        grinding(Items.GRAVEL, Items.SAND, output);


        grinding(ItemTags.REDSTONE_ORES, Items.REDSTONE, 12, output);
        grinding(Items.ANCIENT_DEBRIS, Items.NETHERITE_SCRAP, 2, output);
        grinding(Items.CLAY, Items.CLAY_BALL, 4, output);
    }
}
