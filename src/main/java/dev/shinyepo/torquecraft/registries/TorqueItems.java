package dev.shinyepo.torquecraft.registries;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.item.prefab.CanolaMeal;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueItems {
    //Main registry for items
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TorqueCraft.MODID);

    //Items
    public static final Supplier<Item> TUNGSTEN_INGOT = ITEMS.registerSimpleItem("tungsten_ingot", new Item.Properties().food(new FoodProperties.Builder().fast().alwaysEdible().nutrition(10).build()));
    public static final Supplier<Item> CANOLA_MEAL = ITEMS.register("canola_meal", () -> new CanolaMeal(new Item.Properties()));

    //Block items
    public static final Supplier<BlockItem> TUNGSTEN_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("tungsten_block", TorqueBlocks.TUNGSTEN_BLOCK);

    //machine block items
    public static final Supplier<BlockItem> STEAM_ENGINE_ITEM = ITEMS.registerSimpleBlockItem("steam_engine", TorqueBlocks.STEAM_ENGINE);
    public static final Supplier<BlockItem> MECHANICAL_FAN_ITEM = ITEMS.registerSimpleBlockItem("mechanical_fan", TorqueBlocks.MECHANICAL_FAN);
    public static final Supplier<BlockItem> GRINDER_ITEM = ITEMS.registerSimpleBlockItem("grinder", TorqueBlocks.GRINDER);
    public static final Supplier<BlockItem> PUMP_ITEM = ITEMS.registerSimpleBlockItem("pump", TorqueBlocks.PUMP);

    //Seeds
    public static final Supplier<Item> CANOLA_SEEDS = ITEMS.register("canola_seeds", () -> new ItemNameBlockItem(TorqueBlocks.CANOLA_CROP.get(), new Item.Properties()));

    //fluid buckets
    public static final Supplier<BucketItem> LUBRICANT_BUCKET = ITEMS.register("lubricant_bucket", () -> new BucketItem(TorqueFluids.SOURCE_LUBRICANT.get(),
            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final Supplier<BucketItem> JET_FUEL_BUCKET = ITEMS.register("jet_fuel_bucket", () -> new BucketItem(TorqueFluids.SOURCE_JET_FUEL.get(),
            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));


}
