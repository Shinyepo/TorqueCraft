package dev.shinyepo.torquecraft.integrations.emi.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.integrations.emi.EmiIntegration;
import dev.shinyepo.torquecraft.recipes.custom.AlloyFurnaceRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class EmiAlloyFurnaceRecipe implements EmiRecipe {
    private final ResourceLocation TEXTURE = fromNamespaceAndPath(TorqueCraft.MODID, "textures/gui/integrations/emi_sprite.png");
    private final ResourceLocation id;
    private final List<EmiIngredient> addons;
    private final EmiIngredient inputs;
    private final List<EmiStack> output;
    private final double temp;

    public EmiAlloyFurnaceRecipe(RecipeHolder<AlloyFurnaceRecipe> recipe) {
        this.id = recipe.id();
        var addon = recipe.value().getAddonIngredient();
        var addonList = new ArrayList<EmiIngredient>();
        for (Ingredient ingredient : addon) {
            addonList.add(EmiIngredient.of(ingredient));
        }
        this.addons = addonList;
        this.inputs = EmiIngredient.of(recipe.value().getIngotIngredient());
        this.output = List.of(EmiStack.of(recipe.value().getResultItem(null)));
        this.temp = recipe.value().getTemp();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiIntegration.ALLOY_SMELTING;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return addons;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        var joined = new ArrayList<>(addons);
        joined.add(inputs);

        return joined;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return 122;
    }

    @Override
    public int getDisplayHeight() {
        return 54;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 79, 18);
        widgets.addAnimatedTexture(EmiTexture.FULL_ARROW, 79, 18, 2000, true, false, false);
        widgets.addText(Component.literal("!"), 81, 14, -91518069, false);
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal("Temperature requirement"));
        tooltip.add(Component.literal(this.temp + "°C"));
        widgets.addTooltipText(tooltip, 79, 14, 25, 22);

        for (int i = 0; i < 3; i++) {
            if (i < addons.size()) {
                widgets.addSlot(addons.get(i), 0, 18 * i);
            } else {
                widgets.addSlot(0, 18 * i);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 0 && i == 0) {
                    widgets.addSlot(inputs, 25, 0);
                } else {
                    widgets.addSlot(25 + 18 * j, 18 * i);
                }
            }
        }
        widgets.addTexture(TEXTURE, 18, 9, 7, 38, 0, 53);

        widgets.addSlot(output.getFirst(), 104, 0).recipeContext(this);
        widgets.addSlot(104, 18);
        widgets.addSlot(104, 36);
    }
}
