package dev.shinyepo.torquecraft.datagen.helpers;

import dev.shinyepo.torquecraft.recipes.TorqueRecipeBuilder;
import dev.shinyepo.torquecraft.recipes.alloyfurnace.AlloyFurnaceRecipeBuilder;
import dev.shinyepo.torquecraft.registries.recipe.TorqueRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

import static net.minecraft.resources.ResourceLocation.withDefaultNamespace;

public abstract class CustomRecipeProvider extends RecipeProvider {
    private HolderLookup.RegistryLookup<Item> items;
    public CustomRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    public void setRegistry(HolderLookup.RegistryLookup<Item> itemReg) {
        items = itemReg;
    }

    protected void grinding(TagKey<Item> ingredient, ItemLike result, int count, FluidStack resultFluid, RecipeOutput output) {
        TorqueRecipeBuilder.begin(this.items, RecipeCategory.MISC, result, count)
                .withIngredient(ingredient)
                .withResultFluid(resultFluid)
                .unlockedBy("has_" + formatTagName(ingredient), has(ingredient))
                .save(output);
    }

    protected void grinding(Item ingredient, ItemLike result, int count, FluidStack resultFluid, RecipeOutput output) {
        TorqueRecipeBuilder.begin(this.items, RecipeCategory.MISC, result, count)
                .withIngredient(ingredient)
                .withResultFluid(resultFluid)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(output);
    }

    protected void grinding(Item ingredient, ItemLike result, FluidStack resultFluid, RecipeOutput output) {
        TorqueRecipeBuilder.begin(this.items, RecipeCategory.MISC, result, 1)
                .withIngredient(ingredient)
                .withResultFluid(resultFluid)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(output);
    }

    protected void grinding(Item ingredient, ItemLike result, RecipeOutput output) {
        TorqueRecipeBuilder.begin(this.items, RecipeCategory.MISC, result, 1)
                .withIngredient(ingredient)
                .withResultFluid(new FluidStack(Fluids.WATER, 100))
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(output);
    }

    protected void grinding(Item ingredient, ItemLike result,int count, RecipeOutput output) {
        TorqueRecipeBuilder.begin(this.items, RecipeCategory.MISC, result, count)
                .withIngredient(ingredient)
                .withResultFluid(new FluidStack(Fluids.WATER, 100))
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(output);
    }


    protected void grinding(TagKey<Item> ingredient, ItemLike result, RecipeOutput output) {
        TorqueRecipeBuilder.begin(this.items, RecipeCategory.MISC, result, 1)
                .withIngredient(ingredient)
                .withResultFluid(new FluidStack(Fluids.WATER, 100))
                .unlockedBy("has_" + formatTagName(ingredient), has(ingredient))
                .save(output);
    }

    protected void grinding(TagKey<Item> ingredient, ItemLike result,int count, RecipeOutput output) {
        TorqueRecipeBuilder.begin(this.items, RecipeCategory.MISC, result, count)
                .withIngredient(ingredient)
                .withResultFluid(new FluidStack(Fluids.WATER, 100))
                .unlockedBy("has_" + formatTagName(ingredient), has(ingredient))
                .save(output);
    }

    protected static AlloyFurnaceRecipeBuilder alloying(Ingredient ingotIngredient, ItemLike result) {
        return new AlloyFurnaceRecipeBuilder(TorqueRecipes.Types.ALLOY_SMELTING.get(), RecipeCategory.MISC, ingotIngredient, new ItemStack(result, 1));
    }

    private static String formatTagName(TagKey<Item> tag) {
        return String.join("_", tag.location().toString().split(":")[1].split("/"));
    }

    private static String getMaterialName(ItemLike item) {
        return item.toString().split(":")[1].split("_")[0];
    }

    protected void registerPackageRecipe(Item ingot, ItemLike block) {
        shapeless(RecipeCategory.MISC, ingot, 9)
                .requires(block)
                .unlockedBy(getHasName(block), has(block))
                .save(output);

        shaped(RecipeCategory.MISC, block)
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ingot)
                .unlockedBy(getHasName(ingot), has(ingot))
                .save(output);
    }

    protected void registerDustRecipes(ItemLike dustItem, RecipeOutput output) {
        String dustName = getMaterialName(dustItem);
        var ingot = BuiltInRegistries.ITEM.getOptional(withDefaultNamespace(dustName + "_ingot"));
        var gem = BuiltInRegistries.ITEM.getOptional(withDefaultNamespace(dustName));
        var ore = BuiltInRegistries.ITEM.getOptional(withDefaultNamespace(dustName + "_ore"));
        var deepslateOre = BuiltInRegistries.ITEM.getOptional(withDefaultNamespace("deepslate_" + dustName + "_ore"));
        var raw = BuiltInRegistries.ITEM.getOptional(withDefaultNamespace("raw_" + dustName));
        var rawBlock = BuiltInRegistries.ITEM.getOptional(withDefaultNamespace("raw_" + dustName + "_block"));


        ingot.ifPresent(item -> {
            grinding(item, dustItem, output);
            smeltingResultFromBase(item, dustItem);
            oreBlasting(List.of(dustItem), RecipeCategory.MISC, item, 0.2F, 100, dustName + "_ingot");
        });
        gem.ifPresent(item -> {
            grinding(item, dustItem, output);
            smeltingResultFromBase(item, dustItem);
            oreBlasting(List.of(dustItem), RecipeCategory.MISC, item, 0.2F, 100, dustName);
        });
        ore.ifPresent(item -> grinding(item, dustItem, 2, output));
        deepslateOre.ifPresent(item -> grinding(item, dustItem, 2, output));
        raw.ifPresent(item -> grinding(item, dustItem, output));
        rawBlock.ifPresent(item -> grinding(item, dustItem, 9, output));
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
