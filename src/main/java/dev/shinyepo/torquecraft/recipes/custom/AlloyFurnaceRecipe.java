package dev.shinyepo.torquecraft.recipes.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shinyepo.torquecraft.registries.recipe.TorqueRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class AlloyFurnaceRecipe implements Recipe<RecipeInput> {
    protected final String group;
    protected final List<Ingredient> addonIngredient;
    protected final Ingredient ingotIngredient;
    protected final double temp;
    protected final ItemStack resultItem;

    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;

    public AlloyFurnaceRecipe(String pGroup, List<Ingredient> addonIngredient, Ingredient ingotIngredient, double temp, ItemStack itemStack) {
        this.temp = temp;
        this.type = TorqueRecipes.Types.ALLOY_SMELTING;
        this.serializer = TorqueRecipes.Serializers.ALLOY_SMELTING_SERIALIZER.get();
        this.group = pGroup;
        this.addonIngredient = addonIngredient;
        this.ingotIngredient = ingotIngredient;
        this.resultItem = itemStack;
    }


    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.resultItem;
    }

    @Override
    public ItemStack assemble(RecipeInput container, HolderLookup.Provider provider) {
        return this.resultItem.copy();
    }

    public double getTemp() {
        return temp;
    }

    public boolean matches(RecipeInput container, Level level) {
        if (level.isClientSide()) return false;
        var ingots = getIngotIngredient();
        var addonItems = new ArrayList<ItemStack>();
        for (int i = 0; i < container.size() - 1; i++) {
            if (container.getItem(i).is(ItemStack.EMPTY.getItem())) continue;
            var item = container.getItem(i);
            if (addonItems.stream().anyMatch(x -> x.getItem() == item.getItem())) continue;
            addonItems.add(container.getItem(i));
        }
        if (addonItems.size() != addonIngredient.size()) return false;
        var matchingAddons = false;
        for (int i = 0; i < addonIngredient.size(); i++) {
            for (var item : addonItems) {
                matchingAddons = addonIngredient.get(i).test(item);
                if (matchingAddons) break;
            }
            if (!matchingAddons) return false;
        }
        var matches = ingots.test(container.getItem(container.size() - 1));

        return matches && matchingAddons;
    }

    public Ingredient getIngotIngredient() {
        return ingotIngredient;
    }

    public List<Ingredient> getAddonIngredient() {
        return addonIngredient;
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

    public interface Factory<T extends AlloyFurnaceRecipe> {
        T create(String pGroup, List<Ingredient> addonIngredient, Ingredient ingotIngredient, double temp, ItemStack pResultItem);
    }

    public static class Serializer implements RecipeSerializer<AlloyFurnaceRecipe> {
        final Factory<AlloyFurnaceRecipe> factory;
        private final MapCodec<AlloyFurnaceRecipe> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, AlloyFurnaceRecipe> streamCodec;

        public Serializer(Factory<AlloyFurnaceRecipe> pFactory) {
            this.factory = pFactory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p -> p.group),
                                    Ingredient.LIST_CODEC.fieldOf("addonIngredient").forGetter(i -> i.addonIngredient),
                                    Ingredient.CODEC_NONEMPTY.fieldOf("ingotIngredient").forGetter(p_301068_ -> p_301068_.ingotIngredient),
                                    Codec.DOUBLE.fieldOf("temperature").forGetter(p_301068_ -> p_301068_.temp),
                                    ItemStack.STRICT_CODEC.fieldOf("resultItem").forGetter(p_302316_ -> p_302316_.resultItem)
                            )
                            .apply(p_340781_, pFactory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    p_319737_ -> p_319737_.group,
                    ByteBufCodecs.collection(ArrayList::new, Ingredient.CONTENTS_STREAM_CODEC),
                    p_319738_ -> p_319738_.addonIngredient,
                    Ingredient.CONTENTS_STREAM_CODEC,
                    p -> p.ingotIngredient,
                    ByteBufCodecs.DOUBLE,
                    p -> p.temp,
                    ItemStack.STREAM_CODEC,
                    p -> p.resultItem,
                    pFactory::create
            );
        }

        @Override
        public MapCodec<AlloyFurnaceRecipe> codec() {
            return this.codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AlloyFurnaceRecipe> streamCodec() {
            return this.streamCodec;
        }
    }
}
