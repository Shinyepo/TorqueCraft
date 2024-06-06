package dev.shinyepo.torquecraft.datagen.recipe;

import dev.shinyepo.torquecraft.recipes.TorqueRecipeBuilder;
import dev.shinyepo.torquecraft.recipes.custom.AlloyFurnaceRecipe;
import dev.shinyepo.torquecraft.recipes.custom.AlloyFurnaceRecipeBuilder;
import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.fluid.TorqueFluids;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import dev.shinyepo.torquecraft.registries.recipe.TorqueRecipes;
import dev.shinyepo.torquecraft.registries.tag.TorqueTags;
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

public class TorqueRecipeProvider extends RecipeProvider {
    private static final List<ItemLike> SMELTABLES = List.of(TorqueItems.TUNGSTEN_INGOT.get());

    public TorqueRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pHolder) {
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
        //Materials
        registerMaterialRecipes(pRecipeOutput);
        registerBlockRecipes(pRecipeOutput);
        registerComponents(pRecipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.TUNGSTEN_INGOT.get(),9)
                .requires(TorqueItems.TUNGSTEN_BLOCK_ITEM.get())
                .unlockedBy("criteria", has(TorqueBlocks.TUNGSTEN_BLOCK.get()))
                .save(pRecipeOutput);

        grinding(Ingredient.of(Tags.Items.SEEDS), TorqueItems.CRUSHED_SEEDS.get(), new FluidStack(TorqueFluids.SOURCE_LUBRICANT.get(),100))
                .unlockedBy("criteria", has(Tags.Items.SEEDS))
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

    private void registerBlockRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.ALLOY_FURNACE.get())
                .pattern("SSS")
                .pattern("SBS")
                .pattern("SSS")
                .define('S', Items.BRICK)
                .define('B', Items.BLAST_FURNACE)
                .unlockedBy("has_blast_furnace", has(Items.BLAST_FURNACE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.ROTARY_MONITOR.get())
                .pattern("III")
                .pattern("ICI")
                .pattern("IGI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', TorqueItems.CIRCUIT_MODULE.get())
                //Add gear
                .define('G', TorqueItems.TUNGSTEN_INGOT.get())
                .unlockedBy("has_circuit_module", has(TorqueItems.CIRCUIT_MODULE.get()))
                .save(output);
    }

    private void registerComponents(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.CIRCUIT_MODULE.get())
                .pattern("IGI")
                .pattern("SRS")
                .pattern("III")
                .define('S', TorqueItems.SILICON.get())
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                //Replace with new ingot?
                .define('I', TorqueItems.TUNGSTEN_INGOT.get())
                .unlockedBy("has_silicon", has(TorqueItems.SILICON.get()))
                .save(output);
    }

    private void registerMaterialRecipes(RecipeOutput output) {
        grinding(Ingredient.of(Tags.Items.GEMS_QUARTZ), TorqueItems.NETHER_QUARTZ_DUST.get(), FluidStack.EMPTY)
                .unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
                .save(output);

        smeltingResultFromBase(output, TorqueItems.SILICON.get(), TorqueItems.NETHER_QUARTZ_DUST.get());

        alloying(Ingredient.of(Items.IRON_INGOT), TorqueItems.CAST_IRON_INGOT.get())
                .addAddonIngredient(ItemTags.COALS)
                .addAddonIngredient(Tags.Items.GUNPOWDERS)
                .addAddonIngredient(TorqueTags.SILICON)
                .unlockedBy("has_alloy_furnace", has(TorqueItems.ALLOY_FURNACE_ITEM.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.CAST_IRON_INGOT.get(), 9)
                .requires(TorqueBlocks.CAST_IRON_BLOCK.get())
                .unlockedBy("has_cast_iron_block", has(TorqueBlocks.CAST_IRON_BLOCK.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.CAST_IRON_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', TorqueItems.CAST_IRON_INGOT.get())
                .unlockedBy("has_cast_iron_ingot", has(TorqueItems.CAST_IRON_INGOT.get()))
                .save(output);
    }
}
