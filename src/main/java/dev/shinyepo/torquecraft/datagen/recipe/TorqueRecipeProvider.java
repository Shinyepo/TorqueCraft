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
        registerGears(pRecipeOutput);
        registerMachines(pRecipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.TUNGSTEN_INGOT.get(), 9)
                .requires(TorqueItems.TUNGSTEN_BLOCK_ITEM.get())
                .unlockedBy("criteria", has(TorqueBlocks.TUNGSTEN_BLOCK.get()))
                .save(pRecipeOutput);

        grinding(Ingredient.of(Tags.Items.SEEDS), TorqueItems.CRUSHED_SEEDS.get(), new FluidStack(TorqueFluids.SOURCE_LUBRICANT.get(), 100))
                .unlockedBy("criteria", has(Tags.Items.SEEDS))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.TUNGSTEN_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', TorqueItems.TUNGSTEN_INGOT.get())
                .unlockedBy("criteria", has(TorqueItems.TUNGSTEN_INGOT.get()))
                .save(pRecipeOutput);
    }

    private void registerGears(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_SHAFT_ITEM.get())
                .requires(TorqueItems.HSLA_STEEL_SHAFT.get())
                .requires(TorqueItems.HSLA_CASING.get())
                .unlockedBy("has_hsla_steel_shaft", has(TorqueItems.HSLA_STEEL_SHAFT.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.HSLA_THREE_WAY.get())
                .pattern(" R ")
                .pattern("RRR")
                .pattern(" C ")
                .define('R', TorqueItems.HSLA_GEAR.get())
                .define('C', TorqueItems.HSLA_CASING.get())
                .unlockedBy("has_hsla_gear", has(TorqueItems.HSLA_GEAR.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.HSLA_BEVEL_GEARS.get())
                .pattern("   ")
                .pattern(" R ")
                .pattern("RC ")
                .define('R', TorqueItems.HSLA_GEAR.get())
                .define('C', TorqueItems.HSLA_CASING.get())
                .unlockedBy("has_hsla_gear", has(TorqueItems.HSLA_GEAR.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.HSLA_GEARBOX1_2.get())
                .pattern("   ")
                .pattern(" G ")
                .pattern(" C ")
                .define('G', TorqueItems.HSLA_GEARS_2.get())
                .define('C', TorqueItems.HSLA_CASING.get())
                .unlockedBy("has_hsla_gear", has(TorqueItems.HSLA_GEAR.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.HSLA_GEARBOX1_4.get())
                .pattern("   ")
                .pattern(" G ")
                .pattern(" C ")
                .define('G', TorqueItems.HSLA_GEARS_4.get())
                .define('C', TorqueItems.HSLA_CASING.get())
                .unlockedBy("has_hsla_gear", has(TorqueItems.HSLA_GEAR.get()))
                .save(output);
    }

    private void registerMachines(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.GRINDER.get())
                .pattern("   ")
                .pattern("STS")
                .pattern(" C ")
                .define('S', TorqueItems.SHARP_HSLA_GEAR.get())
                .define('T', TorqueItems.HSLA_TANK.get())
                .define('C', TorqueItems.HSLA_CASING.get())
                .unlockedBy("has_sharp_hsla_gear", has(TorqueItems.SHARP_HSLA_GEAR.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.MECHANICAL_FAN.get())
                .pattern("II ")
                .pattern("SSG")
                .pattern("II ")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .define('S', TorqueItems.HSLA_STEEL_SHAFT.get())
                .define('G', TorqueItems.HSLA_GEAR.get())
                .unlockedBy("has_hsla_gear", has(TorqueItems.HSLA_GEAR.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.PUMP.get())
                .pattern("III")
                .pattern("ICS")
                .pattern("III")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .define('C', TorqueItems.HSLA_TANK.get())
                .define('S', TorqueItems.HSLA_STEEL_SHAFT.get())
                .unlockedBy("has_hsla_tank", has(TorqueItems.HSLA_TANK.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.STEAM_ENGINE.get())
                .pattern("G G")
                .pattern("PTP")
                .pattern("III")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .define('T', TorqueItems.HSLA_TANK.get())
                .define('P', TorqueItems.HSLA_PISTON.get())
                .define('G', TorqueItems.HSLA_GEAR.get())
                .unlockedBy("has_hsla_tank", has(TorqueItems.HSLA_TANK.get()))
                .save(output);
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
                //Change to HSLA ?
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', TorqueItems.CIRCUIT_MODULE.get())
                .define('G', TorqueItems.HSLA_GEAR.get())
                .unlockedBy("has_circuit_module", has(TorqueItems.CIRCUIT_MODULE.get()))
                .save(output);
    }

    private void registerComponents(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.CIRCUIT_MODULE.get())
                .pattern("IGI")
                .pattern("SRS")
                .pattern("ISI")
                .define('S', TorqueItems.SILICON.get())
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                //Replace with new ingot?
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_silicon", has(TorqueItems.SILICON.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.HSLA_CASING.get())
                .pattern("I I")
                .pattern("I I")
                .pattern("III")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .unlockedBy("has_hsla_ingot", has(TorqueItems.HSLA_INGOT.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.HSLA_STEEL_SHAFT.get())
                .pattern("  I")
                .pattern(" I ")
                .pattern("I  ")
                .define('I', TorqueItems.HSLA_ROD.get())
                .unlockedBy("has_hsla_rod", has(TorqueItems.HSLA_ROD.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.HSLA_PISTON.get())
                .pattern(" P ")
                .pattern(" R ")
                .pattern(" G ")
                .define('P', TorqueItems.HSLA_PLATE.get())
                .define('R', TorqueItems.HSLA_ROD.get())
                .define('G', TorqueItems.HSLA_GEAR.get())
                .unlockedBy("has_hsla_gear", has(TorqueItems.HSLA_GEAR.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.HSLA_TANK.get())
                .pattern("I I")
                .pattern("I I")
                .pattern("III")
                .define('I', TorqueItems.HSLA_PLATE.get())
                .unlockedBy("has_hsla_plate", has(TorqueItems.HSLA_PLATE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.HSLA_PLATE.get())
                .pattern("   ")
                .pattern("   ")
                .pattern("III")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .unlockedBy("has_hsla_ingot", has(TorqueItems.HSLA_INGOT.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.HSLA_GEAR.get())
                .pattern(" S ")
                .pattern("SIS")
                .pattern(" S ")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .define('S', TorqueItems.HSLA_ROD.get())
                .unlockedBy("has_hsla_rod", has(TorqueItems.HSLA_ROD.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.SHARP_HSLA_GEAR.get())
                .pattern("S S")
                .pattern(" I ")
                .pattern("S S")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .define('S', TorqueItems.HSLA_ROD.get())
                .unlockedBy("has_hsla_rod", has(TorqueItems.HSLA_ROD.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_ROD.get())
                .requires(TorqueItems.HSLA_INGOT.get(), 2)
                .unlockedBy("has_hsla_ingot", has(TorqueItems.HSLA_INGOT.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_GEARS_2.get())
                .requires(TorqueItems.HSLA_GEAR.get(), 2)
                .unlockedBy("has_hsla_gear", has(TorqueItems.HSLA_GEAR.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_GEARS_4.get())
                .requires(TorqueItems.HSLA_GEARS_2.get())
                .requires(TorqueItems.HSLA_GEAR.get())
                .unlockedBy("has_hsla_gear", has(TorqueItems.HSLA_GEAR.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_GEARS_8.get())
                .requires(TorqueItems.HSLA_GEARS_4.get())
                .requires(TorqueItems.HSLA_GEAR.get())
                .unlockedBy("has_hsla_gear", has(TorqueItems.HSLA_GEAR.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_GEARS_16.get())
                .requires(TorqueItems.HSLA_GEARS_8.get())
                .requires(TorqueItems.HSLA_GEAR.get())
                .unlockedBy("has_hsla_gear", has(TorqueItems.HSLA_GEAR.get()))
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
                .setTemp(1400)
                .unlockedBy("has_alloy_furnace", has(TorqueItems.ALLOY_FURNACE_ITEM.get()))
                .save(output);

        alloying(Ingredient.of(Items.IRON_INGOT), TorqueItems.HSLA_INGOT.get())
                .addAddonIngredient(ItemTags.COALS)
                .addAddonIngredient(Tags.Items.GUNPOWDERS)
                .setTemp(1100)
                .unlockedBy("has_alloy_furnace", has(TorqueItems.ALLOY_FURNACE_ITEM.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_INGOT.get(), 9)
                .requires(TorqueBlocks.HSLA_BLOCK.get())
                .unlockedBy("has_hsla_block", has(TorqueBlocks.HSLA_BLOCK.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.HSLA_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', TorqueItems.HSLA_INGOT.get())
                .unlockedBy("has_hsla_ingot", has(TorqueItems.HSLA_INGOT.get()))
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
