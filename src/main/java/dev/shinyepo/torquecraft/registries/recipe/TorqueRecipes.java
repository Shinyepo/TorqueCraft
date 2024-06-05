package dev.shinyepo.torquecraft.registries.recipe;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.recipes.custom.AlloyFurnaceRecipe;
import dev.shinyepo.torquecraft.recipes.custom.GrinderRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueRecipes {
    public static class Types {
        public static final DeferredRegister<RecipeType<?>> RECIPE_WRITING = DeferredRegister.create(Registries.RECIPE_TYPE, TorqueCraft.MODID);

        public static final RecipeType<GrinderRecipe> GRINDING = RecipeType.simple(new ResourceLocation(TorqueCraft.MODID, "grinding"));
        public static final RecipeType<AlloyFurnaceRecipe> ALLOY_SMELTING = RecipeType.simple(new ResourceLocation(TorqueCraft.MODID, "alloy_smelting"));
    }

    public static class Serializers {
        public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER,  TorqueCraft.MODID);

        public static final Supplier<RecipeSerializer<?>> GRINDING_SERIALIZER = RECIPE_SERIALIZERS.register("grinding", () -> new GrinderRecipe.Serializer<>(GrinderRecipe::new));
        public static final Supplier<RecipeSerializer<?>> ALLOY_SMELTING_SERIALIZER = RECIPE_SERIALIZERS.register("alloy_smelting", () -> new AlloyFurnaceRecipe.Serializer(AlloyFurnaceRecipe::new));
    }
}
