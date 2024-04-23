package dev.shinyepo.torquecraft.registries;

import dev.shinyepo.torquecraft.TorqueCraft;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueItems {
    //Main registry for items
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TorqueCraft.MODID);

    //Items
    public static final Supplier<Item> TUNGSTEN_INGOT = ITEMS.registerSimpleItem("tungsten_ingot", new Item.Properties().food(new FoodProperties.Builder().fast().alwaysEat().nutrition(10).build()));

    //Block items
    public static final Supplier<BlockItem> TUNGSTEN_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("tungsten_block", TorqueBlocks.TUNGSTEN_BLOCK);
    public static final Supplier<BlockItem> STEAM_ENGINE_ITEM = ITEMS.registerSimpleBlockItem("steam_engine", TorqueBlocks.STEAM_ENGINE);

}
