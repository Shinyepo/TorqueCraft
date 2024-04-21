package dev.shinyepo.torquecraft.datagen;

import dev.shinyepo.torquecraft.block.TCBlocks;
import dev.shinyepo.torquecraft.item.TCItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class ModRecipeProvider extends RecipeProvider {
    private static final List<ItemLike> SMELTABLES = List.of(TCItems.TUNGSTEN_INGOT.get());

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        oreSmelting(pRecipeOutput, SMELTABLES, RecipeCategory.MISC, TCBlocks.TUNGSTEN_BLOCK.get(), 0.25f, 100, "tungsten");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TCItems.TUNGSTEN_INGOT.get(),9)
                .requires(TCItems.TUNGSTEN_BLOCK_ITEM.get())
                .unlockedBy("criteria", has(TCBlocks.TUNGSTEN_BLOCK.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,TCBlocks.TUNGSTEN_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', TCItems.TUNGSTEN_INGOT.get())
                .unlockedBy("criteria", has(TCItems.TUNGSTEN_INGOT.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,TCItems.STEAM_ENGINE_ITEM.get())
                .pattern("AAA")
                .pattern("BBB")
                .define('A', TCItems.TUNGSTEN_INGOT.get())
                .define('B', TCItems.TUNGSTEN_BLOCK_ITEM.get())
                .unlockedBy("criteria", has(TCItems.TUNGSTEN_BLOCK_ITEM.get()))
                .unlockedBy("criteria", has(TCItems.TUNGSTEN_INGOT.get()))
                .save(pRecipeOutput);
    }
}
