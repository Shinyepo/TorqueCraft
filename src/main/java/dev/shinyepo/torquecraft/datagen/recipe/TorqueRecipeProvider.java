package dev.shinyepo.torquecraft.datagen.recipe;

import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.fluid.TorqueFluids;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class TorqueRecipeProvider extends CustomRecipeProvider {
    public TorqueRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pHolder) {
        super(pOutput, pHolder);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput pRecipeOutput) {
        buildVanillaRecipes(pRecipeOutput);

        buildGrinderRecipes(pRecipeOutput);

        registerPackageRecipe(TorqueItems.HSLA_INGOT.get(), TorqueItems.HSLA_BLOCK_ITEM.get(), pRecipeOutput);
        registerPackageRecipe(TorqueItems.CAST_IRON_INGOT.get(), TorqueItems.CAST_IRON_BLOCK_ITEM.get(), pRecipeOutput);

        registerMaterialRecipes(pRecipeOutput);
        registerBlockRecipes(pRecipeOutput);
        registerComponents(pRecipeOutput);
        registerGears(pRecipeOutput);
        registerMachines(pRecipeOutput);

    }

    private void buildGrinderRecipes(RecipeOutput output) {
        registerDustRecipes(TorqueItems.COPPER_DUST.get(), output);
        registerDustRecipes(TorqueItems.IRON_DUST.get(), output);
        registerDustRecipes(TorqueItems.GOLD_DUST.get(), output);
        registerDustRecipes(TorqueItems.DIAMOND_DUST.get(), output);
        registerDustRecipes(TorqueItems.EMERALD_DUST.get(), output);
        registerDustRecipes(TorqueItems.NETHERITE_DUST.get(), output);
        registerDustRecipes(TorqueItems.OBSIDIAN_DUST.get(), output);

        grinding(Tags.Items.GEMS_QUARTZ, TorqueItems.QUARTZ_DUST.get(), output);
        grinding(Tags.Items.SEEDS, TorqueItems.CRUSHED_SEEDS.get(), new FluidStack(TorqueFluids.SOURCE_LUBRICANT.get(), 100), output);

    }

    private void registerGears(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_SHAFT_ITEM.get())
                .requires(TorqueItems.HSLA_STEEL_SHAFT.get())
                .requires(TorqueItems.HSLA_CASING.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_STEEL_SHAFT.get()), has(TorqueItems.HSLA_STEEL_SHAFT.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_CASING.get()), has(TorqueItems.HSLA_CASING.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.HSLA_THREE_WAY.get())
                .pattern(" R ")
                .pattern("RRR")
                .pattern(" C ")
                .define('R', TorqueItems.HSLA_GEAR.get())
                .define('C', TorqueItems.HSLA_CASING.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_GEAR.get()), has(TorqueItems.HSLA_GEAR.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_CASING.get()), has(TorqueItems.HSLA_CASING.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.HSLA_BEVEL_GEARS.get())
                .pattern(" R ")
                .pattern("RC ")
                .define('R', TorqueItems.HSLA_GEAR.get())
                .define('C', TorqueItems.HSLA_CASING.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_GEAR.get()), has(TorqueItems.HSLA_GEAR.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_CASING.get()), has(TorqueItems.HSLA_CASING.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.HSLA_GEARBOX1_2.get())
                .pattern(" G ")
                .pattern(" C ")
                .define('G', TorqueItems.HSLA_GEARS_2.get())
                .define('C', TorqueItems.HSLA_CASING.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_GEARS_2.get()), has(TorqueItems.HSLA_GEARS_2.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_CASING.get()), has(TorqueItems.HSLA_CASING.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.HSLA_GEARBOX1_4.get())
                .pattern(" G ")
                .pattern(" C ")
                .define('G', TorqueItems.HSLA_GEARS_4.get())
                .define('C', TorqueItems.HSLA_CASING.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_GEARS_4.get()), has(TorqueItems.HSLA_GEARS_4.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_CASING.get()), has(TorqueItems.HSLA_CASING.get()))
                .save(output);
    }

    private void registerMachines(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.GRINDER_ITEM.get())
                .pattern("STS")
                .pattern(" C ")
                .define('S', TorqueItems.SHARP_HSLA_GEAR.get())
                .define('T', TorqueItems.HSLA_TANK.get())
                .define('C', TorqueItems.HSLA_CASING.get())
                .unlockedBy(getHasName(TorqueItems.SHARP_HSLA_GEAR.get()), has(TorqueItems.SHARP_HSLA_GEAR.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_TANK.get()), has(TorqueItems.HSLA_TANK.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_CASING.get()), has(TorqueItems.HSLA_CASING.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.COOLING_RADIATOR_ITEM.get())
                .pattern("PPP")
                .pattern("PPP")
                .pattern("S S")
                .define('S', TorqueItems.HSLA_ROD.get())
                .define('P', TorqueItems.HSLA_PLATE.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_ROD.get()), has(TorqueItems.HSLA_ROD.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_PLATE.get()), has(TorqueItems.HSLA_PLATE.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.ROTARY_WRENCH.get())
                .pattern("  I")
                .pattern(" P ")
                .pattern("P  ")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .define('P', TorqueItems.HSLA_ROD.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_ROD.get()), has(TorqueItems.HSLA_ROD.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_INGOT.get()), has(TorqueItems.HSLA_INGOT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.FLUID_PIPE_ITEM.get())
                .pattern("PPP")
                .pattern("GGG")
                .pattern("PPP")
                .define('G', Items.GLASS)
                .define('P', TorqueItems.HSLA_PLATE.get())
                .unlockedBy(getHasName(Items.GLASS), has(Items.GLASS))
                .unlockedBy(getHasName(TorqueItems.HSLA_PLATE.get()), has(TorqueItems.HSLA_PLATE.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.FLUID_TANK_ITEM.get())
                .pattern("LPL")
                .pattern("LTL")
                .pattern("LPL")
                .define('L', TorqueItems.HSLA_PLATE.get())
                .define('T', TorqueItems.HSLA_TANK.get())
                .define('P', TorqueItems.FLUID_PIPE_ITEM.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_PLATE.get()), has(TorqueItems.HSLA_PLATE.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_TANK.get()), has(TorqueItems.HSLA_TANK.get()))
                .unlockedBy(getHasName(TorqueItems.FLUID_PIPE_ITEM.get()), has(TorqueItems.FLUID_PIPE_ITEM.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.MECHANICAL_FAN.get())
                .pattern("II ")
                .pattern("SSG")
                .pattern("II ")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .define('S', TorqueItems.HSLA_STEEL_SHAFT.get())
                .define('G', TorqueItems.HSLA_GEAR.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_GEAR.get()), has(TorqueItems.HSLA_GEAR.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_STEEL_SHAFT.get()), has(TorqueItems.HSLA_STEEL_SHAFT.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_INGOT.get()), has(TorqueItems.HSLA_INGOT.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.PUMP.get())
                .pattern("IPI")
                .pattern("GCS")
                .pattern("IPI")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .define('C', TorqueItems.HSLA_TANK.get())
                .define('S', TorqueItems.HSLA_STEEL_SHAFT.get())
                .define('G', TorqueItems.HSLA_GEAR.get())
                .define('P', TorqueItems.FLUID_PIPE_ITEM.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_INGOT.get()), has(TorqueItems.HSLA_INGOT.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_TANK.get()), has(TorqueItems.HSLA_TANK.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_STEEL_SHAFT.get()), has(TorqueItems.HSLA_STEEL_SHAFT.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_GEAR.get()), has(TorqueItems.HSLA_GEAR.get()))
                .unlockedBy(getHasName(TorqueItems.FLUID_PIPE_ITEM.get()), has(TorqueItems.FLUID_PIPE_ITEM.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.STEAM_ENGINE.get())
                .pattern("G G")
                .pattern("PTP")
                .pattern("IXI")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .define('T', TorqueItems.HSLA_TANK.get())
                .define('P', TorqueItems.HSLA_PISTON.get())
                .define('G', TorqueItems.HSLA_GEAR.get())
                .define('X', TorqueItems.FLUID_PIPE_ITEM.get())
                .unlockedBy(getHasName(TorqueItems.FLUID_PIPE_ITEM.get()), has(TorqueItems.FLUID_PIPE_ITEM.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_INGOT.get()), has(TorqueItems.HSLA_INGOT.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_TANK.get()), has(TorqueItems.HSLA_TANK.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_PISTON.get()), has(TorqueItems.HSLA_PISTON.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_GEAR.get()), has(TorqueItems.HSLA_GEAR.get()))
                .save(output);
    }

    private void registerBlockRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueBlocks.ALLOY_FURNACE.get())
                .pattern("SSS")
                .pattern("SBS")
                .pattern("SSS")
                .define('S', Items.BRICK)
                .define('B', Items.BLAST_FURNACE)
                .unlockedBy(getHasName(Items.BRICK), has(Items.BRICK))
                .unlockedBy(getHasName(Items.BLAST_FURNACE), has(Items.BLAST_FURNACE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.ROTARY_MONITOR.get())
                .pattern("III")
                .pattern("ICI")
                .pattern("IGI")
                //Change to HSLA ?
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', TorqueItems.CIRCUIT_MODULE.get())
                .define('G', TorqueItems.HSLA_GEAR.get())
                .unlockedBy(getHasName(TorqueItems.CIRCUIT_MODULE.get()), has(TorqueItems.CIRCUIT_MODULE.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_GEAR.get()), has(TorqueItems.HSLA_GEAR.get()))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);
    }

    private void registerComponents(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.CIRCUIT_MODULE.get())
                .pattern("IGI")
                .pattern("SRS")
                .pattern("ISI")
                .define('S', TorqueItems.SILICON.get())
                .define('G', Items.GOLD_INGOT)
                .define('R', Items.REDSTONE)
                //Replace with new ingot?
                .define('I', Items.IRON_INGOT)
                .unlockedBy(getHasName(TorqueItems.SILICON.get()), has(TorqueItems.SILICON.get()))
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.HSLA_CASING.get())
                .pattern("I I")
                .pattern("I I")
                .pattern("III")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_INGOT.get()), has(TorqueItems.HSLA_INGOT.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.HSLA_STEEL_SHAFT.get())
                .pattern("  I")
                .pattern(" I ")
                .pattern("I  ")
                .define('I', TorqueItems.HSLA_ROD.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_ROD.get()), has(TorqueItems.HSLA_ROD.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.HSLA_PISTON.get())
                .pattern(" P ")
                .pattern(" R ")
                .pattern(" G ")
                .define('P', TorqueItems.HSLA_PLATE.get())
                .define('R', TorqueItems.HSLA_ROD.get())
                .define('G', TorqueItems.HSLA_GEAR.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_PLATE.get()), has(TorqueItems.HSLA_PLATE.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_ROD.get()), has(TorqueItems.HSLA_ROD.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_GEAR.get()), has(TorqueItems.HSLA_GEAR.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.HSLA_TANK.get())
                .pattern("I I")
                .pattern("I I")
                .pattern("III")
                .define('I', TorqueItems.HSLA_PLATE.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_PLATE.get()), has(TorqueItems.HSLA_PLATE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.HSLA_PLATE.get())
                .pattern("III")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_INGOT.get()), has(TorqueItems.HSLA_INGOT.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.HSLA_GEAR.get())
                .pattern(" S ")
                .pattern("SIS")
                .pattern(" S ")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .define('S', TorqueItems.HSLA_ROD.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_ROD.get()), has(TorqueItems.HSLA_ROD.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_INGOT.get()), has(TorqueItems.HSLA_INGOT.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TorqueItems.SHARP_HSLA_GEAR.get())
                .pattern("S S")
                .pattern(" I ")
                .pattern("S S")
                .define('I', TorqueItems.HSLA_INGOT.get())
                .define('S', TorqueItems.HSLA_ROD.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_ROD.get()), has(TorqueItems.HSLA_ROD.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_INGOT.get()), has(TorqueItems.HSLA_INGOT.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_ROD.get())
                .requires(TorqueItems.HSLA_INGOT.get(), 2)
                .unlockedBy(getHasName(TorqueItems.HSLA_INGOT.get()), has(TorqueItems.HSLA_INGOT.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_GEARS_2.get())
                .requires(TorqueItems.HSLA_GEAR.get(), 2)
                .unlockedBy(getHasName(TorqueItems.HSLA_GEAR.get()), has(TorqueItems.HSLA_GEAR.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_GEARS_4.get())
                .requires(TorqueItems.HSLA_GEARS_2.get())
                .requires(TorqueItems.HSLA_GEAR.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_GEARS_2.get()), has(TorqueItems.HSLA_GEARS_2.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_GEAR.get()), has(TorqueItems.HSLA_GEAR.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_GEARS_8.get())
                .requires(TorqueItems.HSLA_GEARS_4.get())
                .requires(TorqueItems.HSLA_GEAR.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_GEARS_4.get()), has(TorqueItems.HSLA_GEARS_4.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_GEAR.get()), has(TorqueItems.HSLA_GEAR.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TorqueItems.HSLA_GEARS_16.get())
                .requires(TorqueItems.HSLA_GEARS_8.get())
                .requires(TorqueItems.HSLA_GEAR.get())
                .unlockedBy(getHasName(TorqueItems.HSLA_GEARS_8.get()), has(TorqueItems.HSLA_GEARS_8.get()))
                .unlockedBy(getHasName(TorqueItems.HSLA_GEAR.get()), has(TorqueItems.HSLA_GEAR.get()))
                .save(output);
    }

    private void registerMaterialRecipes(RecipeOutput output) {
        smeltingResultFromBase(output, TorqueItems.SILICON.get(), TorqueItems.QUARTZ_DUST.get());

        alloying(Ingredient.of(Items.IRON_INGOT), TorqueItems.CAST_IRON_INGOT.get())
                .addAddonIngredient(Items.COAL)
                .addAddonIngredient(Items.GUNPOWDER)
                .addAddonIngredient(TorqueItems.SILICON.get())
                .setTemp(1400)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Items.COAL), has(Items.COAL))
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Items.GUNPOWDER))
                .unlockedBy(getHasName(TorqueItems.SILICON.get()), has(TorqueItems.SILICON.get()))
                .save(output);

        alloying(Ingredient.of(Items.IRON_INGOT), TorqueItems.HSLA_INGOT.get())
                .addAddonIngredient(Items.COAL)
                .addAddonIngredient(Items.GUNPOWDER)
                .setTemp(1100)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Items.COAL), has(Items.COAL))
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Items.GUNPOWDER))
                .save(output);
    }
}
