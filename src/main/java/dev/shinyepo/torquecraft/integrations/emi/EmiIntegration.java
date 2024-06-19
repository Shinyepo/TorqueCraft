package dev.shinyepo.torquecraft.integrations.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.integrations.emi.recipes.EmiAlloyFurnaceRecipe;
import dev.shinyepo.torquecraft.integrations.emi.recipes.EmiGrindingRecipe;
import dev.shinyepo.torquecraft.recipes.custom.AlloyFurnaceRecipe;
import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import dev.shinyepo.torquecraft.registries.recipe.TorqueRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

@EmiEntrypoint
public class EmiIntegration implements EmiPlugin {
    public static final ResourceLocation sprite = fromNamespaceAndPath(TorqueCraft.MODID, "textures/gui/integrations/emi_simplified_icons.png");
    public static final EmiStack GRINDER = EmiStack.of(TorqueItems.GRINDER_ITEM.get());
    public static final EmiStack ALLOY_FURNACE = EmiStack.of(TorqueItems.ALLOY_FURNACE_ITEM.get());
    public static final EmiRecipeCategory GRINDING = new EmiRecipeCategory(fromNamespaceAndPath(TorqueCraft.MODID, "grinding"), GRINDER, new EmiTexture(sprite, 0, 0, 18, 18));
    public static final EmiRecipeCategory ALLOY_SMELTING = new EmiRecipeCategory(fromNamespaceAndPath(TorqueCraft.MODID, "alloying"), ALLOY_FURNACE, new EmiTexture(sprite, 0, 18, 18, 18));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(GRINDING);
        registry.addCategory(ALLOY_SMELTING);

        registry.addWorkstation(GRINDING, GRINDER);
        registry.addWorkstation(ALLOY_SMELTING, ALLOY_FURNACE);

        RecipeManager manager = registry.getRecipeManager();

        for (RecipeHolder<GrinderRecipe> recipe : manager.getAllRecipesFor(TorqueRecipes.Types.GRINDING)) {
            registry.addRecipe(new EmiGrindingRecipe(recipe));
        }

        for (RecipeHolder<AlloyFurnaceRecipe> recipe : manager.getAllRecipesFor(TorqueRecipes.Types.ALLOY_SMELTING)) {
            registry.addRecipe(new EmiAlloyFurnaceRecipe(recipe));
        }
    }
}
