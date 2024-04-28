package dev.shinyepo.torquecraft.recipes.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shinyepo.torquecraft.recipes.TorqueRecipes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;

public class GrinderRecipe extends SingleItemRecipe implements Recipe<Container> {
    public GrinderRecipe(String pGroup, Ingredient ingredient, ItemStack itemStack) {
        super(TorqueRecipes.Types.GRINDING, TorqueRecipes.Serializers.GRINDING_SERIALIZER.get(), pGroup, ingredient, itemStack);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public boolean matches(Container pContainer, Level pLevel) {
        if(pLevel.isClientSide()) return false;

        return getIngredients().get(0).test(pContainer.getItem(0));
    }

    public static class Serializer<T extends SingleItemRecipe> implements RecipeSerializer<GrinderRecipe> {
        final SingleItemRecipe.Factory<GrinderRecipe> factory;
        private final MapCodec<GrinderRecipe> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, GrinderRecipe> streamCodec;

        public Serializer(SingleItemRecipe.Factory<GrinderRecipe> pFactory) {
            this.factory = pFactory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p -> p.group),
                                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(p_301068_ -> p_301068_.ingredient),
                                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_302316_ -> p_302316_.result)
                            )
                            .apply(p_340781_, pFactory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    p_319737_ -> p_319737_.group,
                    Ingredient.CONTENTS_STREAM_CODEC,
                    p_319738_ -> p_319738_.ingredient,
                    ItemStack.STREAM_CODEC,
                    p_319736_ -> p_319736_.result,
                    pFactory::create
            );
        }

        @Override
        public MapCodec<GrinderRecipe> codec() {
            return this.codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, GrinderRecipe> streamCodec() {
            return this.streamCodec;
        }
    }
}
