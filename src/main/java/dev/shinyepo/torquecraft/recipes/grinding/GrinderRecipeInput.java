package dev.shinyepo.torquecraft.recipes.grinding;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record GrinderRecipeInput(ItemStack stack) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int slot) {
        if (slot != 0) throw new IllegalArgumentException("No item for slot " + slot);
        return this.stack;
    }

    @Override
    public int size() {
        return 1;
    }
}
