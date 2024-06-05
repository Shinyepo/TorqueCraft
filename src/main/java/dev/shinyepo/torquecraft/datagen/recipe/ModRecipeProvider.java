package dev.shinyepo.torquecraft.datagen.recipe;

import dev.shinyepo.torquecraft.recipes.TorqueRecipeBuilder;
import dev.shinyepo.torquecraft.recipes.custom.AlloyFurnaceRecipe;
import dev.shinyepo.torquecraft.recipes.custom.AlloyFurnaceRecipeBuilder;
import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.fluid.TorqueFluids;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import dev.shinyepo.torquecraft.registries.recipe.TorqueRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    private static final List<ItemLike> SMELTABLES = List.of(TorqueItems.TUNGSTEN_INGOT.get());

    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pHolder) {
        super(pOutput, pHolder);
    }


    public static TorqueRecipeBuilder grinding(Ingredient ingredient, ItemLike result, FluidStack resultFluid) {
        return new TorqueRecipeBuilder(TorqueRecipes.Types.GRINDING, RecipeCategory.MISC, GrinderRecipe::new, ingredient, new ItemStack(result, 1), resultFluid);
    }

    private static AlloyFurnaceRecipeBuilder alloying(Ingredient ingotIngredient, ItemLike result) {
        return new AlloyFurnaceRecipeBuilder(TorqueRecipes.Types.ALLOY_SMELTING, RecipeCategory.MISC, AlloyFurnaceRecipe::new, ingotIngredient, new ItemStack(result, 1));
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput pRecipeOutput) {
        oreSmelting(pRecipeOutput, SMELTABLES, RecipeCategory.MISC, TorqueBlocks.TUNGSTEN_BLOCK.get(), 0.25f, 100, "tungsten");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.TUNGSTEN_INGOT.get(),9)
                .requires(TorqueItems.TUNGSTEN_BLOCK_ITEM.get())
                .unlockedBy("criteria", has(TorqueBlocks.TUNGSTEN_BLOCK.get()))
                .save(pRecipeOutput);

        grinding(Ingredient.of(Tags.Items.SEEDS), TorqueItems.CRUSHED_SEEDS.get(), new FluidStack(TorqueFluids.SOURCE_LUBRICANT.get(),100))
                .unlockedBy("criteria", has(Tags.Items.SEEDS))
                .save(pRecipeOutput);

        alloying(Ingredient.of(Items.IRON_INGOT), TorqueItems.TUNGSTEN_INGOT.get())
                .addAddonIngredient(ItemTags.COALS)
                .addAddonIngredient(Tags.Items.GUNPOWDERS)
                .addAddonIngredient(Tags.Items.SANDSTONE_BLOCKS)
                .unlockedBy("criteria", has(TorqueItems.ALLOY_FURNACE_ITEM.get()))
                .save(pRecipeOutput);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.TUNGSTEN_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', TorqueItems.TUNGSTEN_INGOT.get())
                .unlockedBy("criteria", has(TorqueItems.TUNGSTEN_INGOT.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.STEAM_ENGINE_ITEM.get())
                .pattern("AAA")
                .pattern("BBB")
                .define('A', TorqueItems.TUNGSTEN_INGOT.get())
                .define('B', TorqueItems.TUNGSTEN_BLOCK_ITEM.get())
                .unlockedBy("criteria", has(TorqueItems.TUNGSTEN_BLOCK_ITEM.get()))
                .unlockedBy("criteria", has(TorqueItems.TUNGSTEN_INGOT.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.MECHANICAL_FAN_ITEM.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TorqueItems.TUNGSTEN_INGOT.get())
                .define('B', TorqueItems.TUNGSTEN_BLOCK_ITEM.get())
                .unlockedBy("criteria", has(TorqueItems.TUNGSTEN_BLOCK_ITEM.get()))
                .unlockedBy("criteria", has(TorqueItems.TUNGSTEN_INGOT.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.GRINDER_ITEM.get())
                .pattern(" A ")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', TorqueItems.TUNGSTEN_INGOT.get())
                .define('B', TorqueItems.TUNGSTEN_BLOCK_ITEM.get())
                .unlockedBy("criteria", has(TorqueItems.TUNGSTEN_BLOCK_ITEM.get()))
                .unlockedBy("criteria", has(TorqueItems.TUNGSTEN_INGOT.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.PUMP_ITEM.get())
                .pattern(" A ")
                .pattern(" BA")
                .pattern(" A ")
                .define('B', TorqueItems.TUNGSTEN_INGOT.get())
                .define('A', TorqueItems.TUNGSTEN_BLOCK_ITEM.get())
                .unlockedBy("criteria", has(TorqueItems.TUNGSTEN_BLOCK_ITEM.get()))
                .unlockedBy("criteria", has(TorqueItems.TUNGSTEN_INGOT.get()))
                .save(pRecipeOutput);
    }
}
