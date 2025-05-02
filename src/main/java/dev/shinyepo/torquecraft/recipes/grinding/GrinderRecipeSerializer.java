package dev.shinyepo.torquecraft.recipes.grinding;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class GrinderRecipeSerializer implements RecipeSerializer<GrinderRecipe> {
    public static final MapCodec<GrinderRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            GrinderRecipe.InputItem.CODEC.fieldOf("ingredient").forGetter(GrinderRecipe::getInputItem),
            ItemStack.CODEC.fieldOf("result").forGetter(GrinderRecipe::getResult),
            FluidStack.CODEC.fieldOf("resultFluid").forGetter(GrinderRecipe::getResultFluid)
    ).apply(inst, GrinderRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GrinderRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    GrinderRecipe.InputItem.STREAM_CODEC, GrinderRecipe::getInputItem,
                    ItemStack.STREAM_CODEC, GrinderRecipe::getResult,
                    FluidStack.STREAM_CODEC, GrinderRecipe::getResultFluid,
                    GrinderRecipe::new
            );

    @Override
    public @NotNull MapCodec<GrinderRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, GrinderRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
