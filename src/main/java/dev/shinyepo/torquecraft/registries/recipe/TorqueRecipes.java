package dev.shinyepo.torquecraft.registries.recipe;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.recipes.alloyfurnace.AlloyFurnaceRecipe;
import dev.shinyepo.torquecraft.recipes.alloyfurnace.AlloyFurnaceSerializer;
import dev.shinyepo.torquecraft.recipes.grinding.GrinderRecipe;
import dev.shinyepo.torquecraft.recipes.grinding.GrinderRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class TorqueRecipes {
    public static class Types {
        public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, TorqueCraft.MODID);

        public static final Supplier<RecipeType<GrinderRecipe>> GRINDING = RECIPE_TYPES.register("grinding", registryName -> new RecipeType<>() {
            @Override
            public String toString() {
                return registryName.toString();
            }
        });
        public static final Supplier<RecipeType<AlloyFurnaceRecipe>> ALLOY_SMELTING = RECIPE_TYPES.register("alloy_smelting", registryName -> new RecipeType<>() {
            @Override
            public String toString() {
                return registryName.toString();
            }
        });
    }

    public static class Serializers {
        public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER,  TorqueCraft.MODID);

        public static final Supplier<RecipeSerializer<GrinderRecipe>> GRINDING_SERIALIZER = RECIPE_SERIALIZERS.register("grinding", GrinderRecipeSerializer::new);

        public static final Supplier<RecipeSerializer<AlloyFurnaceRecipe>> ALLOY_SMELTING_SERIALIZER = RECIPE_SERIALIZERS.register("alloy_smelting", AlloyFurnaceSerializer::new);
    }
}
