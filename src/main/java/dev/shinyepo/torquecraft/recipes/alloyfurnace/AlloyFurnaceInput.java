package dev.shinyepo.torquecraft.recipes.alloyfurnace;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record AlloyFurnaceInput(List<ItemStack> stack) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int pIndex) {
        return this.stack.get(pIndex);
    }

    @Override
    public int size() {
        return 1;
    }
}
