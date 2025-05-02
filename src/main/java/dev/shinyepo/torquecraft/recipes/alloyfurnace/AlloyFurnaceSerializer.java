package dev.shinyepo.torquecraft.recipes.alloyfurnace;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AlloyFurnaceSerializer implements RecipeSerializer<AlloyFurnaceRecipe> {
    public static final MapCodec<AlloyFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.listOf().fieldOf("addonIngredient").forGetter(AlloyFurnaceRecipe::getAddonIngredient),
            Ingredient.CODEC.fieldOf("ingotIngredient").forGetter(AlloyFurnaceRecipe::getIngotIngredient),
            Codec.DOUBLE.fieldOf("temperature").forGetter(AlloyFurnaceRecipe::getTemp),
            ItemStack.STRICT_CODEC.fieldOf("resultItem").forGetter(AlloyFurnaceRecipe::getResultItem)
    ).apply(inst, AlloyFurnaceRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlloyFurnaceRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.collection(ArrayList::new, Ingredient.CONTENTS_STREAM_CODEC), AlloyFurnaceRecipe::getAddonIngredient,
                    Ingredient.CONTENTS_STREAM_CODEC, AlloyFurnaceRecipe::getIngotIngredient,
                    ByteBufCodecs.DOUBLE, AlloyFurnaceRecipe::getTemp,
                    ItemStack.STREAM_CODEC, AlloyFurnaceRecipe::getResultItem,
                    AlloyFurnaceRecipe::new
            );

    @Override
    public @NotNull MapCodec<AlloyFurnaceRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, AlloyFurnaceRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
