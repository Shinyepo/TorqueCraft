package dev.shinyepo.torquecraft.datagen.models;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.config.ModelType;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class TorqueItemModels extends ItemModelGenerators {
    public TorqueItemModels(ItemModelOutput itemModelOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
    }

    @Override
    public void run() {
        //Flat items
        generateFlatItem(TorqueItems.CRUSHED_SEEDS.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(TorqueItems.CANOLA_SEEDS.get(), ModelTemplates.FLAT_ITEM);
//      generateFlatItem(TorqueItems.CANOLA_MEAL.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(TorqueItems.PRESSURE_GAUGE.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(TorqueItems.ROTARY_WRENCH.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(TorqueItems.LUBRICANT_BUCKET.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(TorqueItems.JET_FUEL_BUCKET.get(), ModelTemplates.FLAT_ITEM);

        //Materials
        generateFlatItemWithType(TorqueItems.CAST_IRON_INGOT.get(), ModelType.MATERIALS);
        generateFlatItemWithType(TorqueItems.COPPER_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(TorqueItems.DIAMOND_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(TorqueItems.EMERALD_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(TorqueItems.GOLD_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(TorqueItems.HSLA_INGOT.get(), ModelType.MATERIALS);
        generateFlatItemWithType(TorqueItems.IRON_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(TorqueItems.NETHERITE_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(TorqueItems.OBSIDIAN_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(TorqueItems.QUARTZ_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(TorqueItems.SILICON.get(), ModelType.MATERIALS);
        generateFlatItemWithType(TorqueItems.TUNGSTEN_INGOT.get(), ModelType.MATERIALS);

        //Components
        generateFlatItemWithType(TorqueItems.CIRCUIT_MODULE.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(TorqueItems.HSLA_CASING.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(TorqueItems.HSLA_GEAR.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(TorqueItems.HSLA_GEARS_2.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(TorqueItems.HSLA_GEARS_4.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(TorqueItems.HSLA_GEARS_8.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(TorqueItems.HSLA_GEARS_16.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(TorqueItems.HSLA_PISTON.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(TorqueItems.HSLA_PLATE.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(TorqueItems.HSLA_ROD.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(TorqueItems.HSLA_STEEL_SHAFT.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(TorqueItems.HSLA_TANK.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(TorqueItems.SHARP_HSLA_GEAR.get(), ModelType.COMPONENTS);

        generateItemWithExistingParent(TorqueItems.ROTARY_MONITOR.get(), "rotary_monitor");
    }

    private String getPathWithType(Item block, ModelType type) {
        var name = block.getName().getString().split("\\.")[2];
        return "item/" + type.getSerializedName() + "/" + name;
    }

    private void generateItemWithExistingParent(Item item, String parent) {
        generateItemWithExistingModel(item, fromNamespaceAndPath(TorqueCraft.MODID, "block/" + parent));
    }

    private void generateItemWithExistingModel(Item item, ResourceLocation modelLocation) {
        itemModelOutput.accept(
                item,
                ItemModelUtils.plainModel(modelLocation)
        );
    }

    private void generateFlatItemWithType(Item item, ModelType type) {
        var rl = fromNamespaceAndPath(TorqueCraft.MODID, getPathWithType(item, type));

        itemModelOutput.accept(
                item,
                ItemModelUtils.plainModel(
                        ModelTemplates.FLAT_ITEM.create(rl, TextureMapping.layer0(rl), modelOutput)
                )
        );
    }
}
