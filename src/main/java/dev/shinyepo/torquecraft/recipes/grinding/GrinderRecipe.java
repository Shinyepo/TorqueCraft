package dev.shinyepo.torquecraft.recipes.grinding;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shinyepo.torquecraft.registries.recipe.TorqueRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class GrinderRecipe implements Recipe<GrinderRecipeInput> {
    private final InputItem inputItem;
    private final ItemStack result;
    private final FluidStack resultFluid;

    public GrinderRecipe(InputItem inputItem, ItemStack result, FluidStack resultFluid) {
        this.inputItem = inputItem;
        this.result = result;
        this.resultFluid = resultFluid;
    }

    @Override
    public boolean matches(GrinderRecipeInput input, @NotNull Level level) {
        return this.inputItem.test(input.stack());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull GrinderRecipeInput pInput, HolderLookup.@NotNull Provider pRegistries) {
        return this.result.copy();
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<GrinderRecipeInput>> getSerializer() {
        return TorqueRecipes.Serializers.GRINDING_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<GrinderRecipeInput>> getType() {
        return TorqueRecipes.Types.GRINDING.get();
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.FURNACE_BLOCKS;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public InputItem getInputItem() {
        return this.inputItem;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public FluidStack getResultFluid() {
        return this.resultFluid;
    }

    public record InputItem(Ingredient ingredient, int count) implements Predicate<ItemStack>
    {
        public static final Codec<InputItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(InputItem::ingredient),
                Codec.INT.fieldOf("count").forGetter(InputItem::count)
        ).apply(instance, InputItem::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, InputItem> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, InputItem::ingredient,
                ByteBufCodecs.INT, InputItem::count,
                InputItem::new
        );

        public static InputItem of(Ingredient ingredient, int count)
        {
            return new InputItem(ingredient, count);
        }

        @Override
        public boolean test(ItemStack itemStack)
        {
            return ingredient.test(itemStack) && itemStack.getCount() >= count;
        }
    }
}
