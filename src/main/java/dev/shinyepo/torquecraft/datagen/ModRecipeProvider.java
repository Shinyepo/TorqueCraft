package dev.shinyepo.torquecraft.datagen;

import dev.shinyepo.torquecraft.fluid.TorqueFluids;
import dev.shinyepo.torquecraft.recipes.TorqueRecipeBuilder;
import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
import dev.shinyepo.torquecraft.registries.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.TorqueItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    private static final List<ItemLike> SMELTABLES = List.of(TorqueItems.TUNGSTEN_INGOT.get());

    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pHolder) {
        super(pOutput, pHolder);
    }


    @Override
    protected void buildRecipes(@NotNull RecipeOutput pRecipeOutput) {
        oreSmelting(pRecipeOutput, SMELTABLES, RecipeCategory.MISC, TorqueBlocks.TUNGSTEN_BLOCK.get(), 0.25f, 100, "tungsten");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.TUNGSTEN_INGOT.get(),9)
                .requires(TorqueItems.TUNGSTEN_BLOCK_ITEM.get())
                .unlockedBy("criteria", has(TorqueBlocks.TUNGSTEN_BLOCK.get()))
                .save(pRecipeOutput);

        grinding(Ingredient.of(TorqueItems.CANOLA_SEEDS.get()), TorqueItems.CANOLA_MEAL.get(), 1, new FluidStack(TorqueFluids.SOURCE_LUBRICANT.get(),200))
                .unlockedBy("criteria", has(TorqueItems.CANOLA_SEEDS.get()))
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
    }

    public static TorqueRecipeBuilder grinding(Ingredient ingredient, ItemLike result, int count, FluidStack resultFluid) {
        return new TorqueRecipeBuilder(RecipeCategory.MISC, GrinderRecipe::new, ingredient, result, count, resultFluid, resultFluid.getAmount());
    }
}
