package dev.shinyepo.torquecraft.integrations.emi.recipes;

import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.integrations.emi.EmiIntegration;
import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class EmiGrindingRecipe implements EmiRecipe {
    private final ResourceLocation TEXTURE = fromNamespaceAndPath(TorqueCraft.MODID, "textures/gui/integrations/emi_sprite.png");
    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;
    private final EmiStack resultFluid;

    public EmiGrindingRecipe(RecipeHolder<GrinderRecipe> recipe) {
        this.id = recipe.id();
        this.input = List.of(EmiIngredient.of(recipe.value().getIngredient()));
        this.output = List.of(EmiStack.of(recipe.value().getResultItem(null)));
        this.resultFluid = NeoForgeEmiStack.of(recipe.value().getResultFluid(null));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiIntegration.GRINDING;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return 96;
    }

    @Override
    public int getDisplayHeight() {
        return 54;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 18);
        widgets.addAnimatedTexture(EmiTexture.FULL_ARROW, 26, 18, 2000, true, false, false);

        widgets.addSlot(input.getFirst(), 0, 18);
        widgets.addSlot(output.getFirst(), 58, 18).recipeContext(this);

        widgets.addTexture(TEXTURE, 78, 0, 18, 53, 0, 0);
        widgets.addTank(resultFluid, 78, 0, 18, 53, 1).drawBack(false);
    }
}
