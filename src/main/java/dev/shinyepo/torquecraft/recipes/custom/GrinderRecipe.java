package dev.shinyepo.torquecraft.recipes.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shinyepo.torquecraft.recipes.TorqueRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class GrinderRecipe implements Recipe<Container> {
    protected final Ingredient ingredient;
    protected final ItemStack result;
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;
    protected final String group;

    public GrinderRecipe(String pGroup, Ingredient ingredient, ItemStack itemStack) {
        this.type = TorqueRecipes.Types.GRINDING;
        this.serializer = TorqueRecipes.Serializers.GRINDING_SERIALIZER.get();
        this.group = pGroup;
        this.ingredient = ingredient;
        this.result = itemStack;
    }


    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public ItemStack assemble(Container container, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    public boolean matches(Container container, Level level) {
        if(level.isClientSide()) return false;

        return getIngredient().get(0).test(container.getItem(0));
    }

    public NonNullList<Ingredient> getIngredient() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return serializer;
    }

    @Override
    public RecipeType<?> getType() {
        return type;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public static class Serializer<T extends GrinderRecipe> implements RecipeSerializer<GrinderRecipe> {
        final GrinderRecipe.Factory<GrinderRecipe> factory;
        private final MapCodec<GrinderRecipe> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, GrinderRecipe> streamCodec;

        public Serializer(GrinderRecipe.Factory<GrinderRecipe> pFactory) {
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

    public interface Factory<T extends GrinderRecipe> {
        T create(String pGroup, Ingredient pIngredient, ItemStack pResult);
    }
}
