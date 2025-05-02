package dev.shinyepo.torquecraft.datagen.block;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.config.ModelType;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class TorqueBlockStateProvider extends ModelProvider {
    public TorqueBlockStateProvider(PackOutput output) {
        super(output, TorqueCraft.MODID);
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        createTrivialBlock(blockModels,TorqueBlocks.TUNGSTEN_BLOCK.get(), ModelType.MATERIALS);
        createTrivialBlock(blockModels,TorqueBlocks.HSLA_BLOCK.get(), ModelType.MATERIALS);
        createTrivialBlock(blockModels,TorqueBlocks.CAST_IRON_BLOCK.get(), ModelType.MATERIALS);
//        registerPipe(TorqueBlocks.FLUID_PIPE);
//        registerPipe(TorqueBlocks.STEAM_PIPE);

        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.MECHANICAL_FAN.get());
        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.STEAM_ENGINE.get());
        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.GRINDER.get());
        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.PUMP.get());
        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.SPRINKLER.get());
        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.HSLA_SHAFT.get());
        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.HSLA_BEVEL_GEARS.get());
        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.HSLA_THREE_WAY.get());
        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.VACUUM.get());
        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.COOLING_RADIATOR.get(), "block/radiator/cooling_radiator_full");
        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.ALLOY_FURNACE.get(), "block/furnace/alloy_furnace");
        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.HSLA_GEARBOX1_2.get(), "block/gearbox/hsla_gearbox1_2");
        horizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.HSLA_GEARBOX1_4.get(), "block/gearbox/hsla_gearbox1_4");


//        horizontalWithExistingModel(blockModels, TorqueBlocks.FLUID_TANK.get());

        blockModels.createCropBlock(TorqueBlocks.CANOLA_CROP.get(), BlockStateProperties.AGE_7, 0, 1, 1, 2, 3, 4, 4, 5);

        blockModels.createNonTemplateModelBlock(TorqueBlocks.LUBRICANT_BLOCK.get());
        blockModels.createNonTemplateModelBlock(TorqueBlocks.JET_FUEL_BLOCK.get());

        //Flat items
        itemModels.generateFlatItem(TorqueItems.CRUSHED_SEEDS.get(), ModelTemplates.FLAT_ITEM);
//        itemModels.generateFlatItem(TorqueItems.CANOLA_SEEDS.get(), ModelTemplates.FLAT_ITEM);
//        itemModels.generateFlatItem(TorqueItems.CANOLA_MEAL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(TorqueItems.PRESSURE_GAUGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(TorqueItems.ROTARY_WRENCH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(TorqueItems.LUBRICANT_BUCKET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(TorqueItems.JET_FUEL_BUCKET.get(), ModelTemplates.FLAT_ITEM);

        //Materials
        generateFlatItemWithType(itemModels, TorqueItems.CAST_IRON_INGOT.get(), ModelType.MATERIALS);
        generateFlatItemWithType(itemModels, TorqueItems.COPPER_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(itemModels, TorqueItems.DIAMOND_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(itemModels, TorqueItems.EMERALD_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(itemModels, TorqueItems.GOLD_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(itemModels, TorqueItems.HSLA_INGOT.get(), ModelType.MATERIALS);
        generateFlatItemWithType(itemModels, TorqueItems.IRON_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(itemModels, TorqueItems.NETHERITE_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(itemModels, TorqueItems.OBSIDIAN_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(itemModels, TorqueItems.QUARTZ_DUST.get(), ModelType.MATERIALS);
        generateFlatItemWithType(itemModels, TorqueItems.SILICON.get(), ModelType.MATERIALS);
        generateFlatItemWithType(itemModels, TorqueItems.TUNGSTEN_INGOT.get(), ModelType.MATERIALS);

        //Components
        generateFlatItemWithType(itemModels, TorqueItems.CIRCUIT_MODULE.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(itemModels, TorqueItems.HSLA_CASING.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(itemModels, TorqueItems.HSLA_GEAR.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(itemModels, TorqueItems.HSLA_GEARS_2.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(itemModels, TorqueItems.HSLA_GEARS_4.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(itemModels, TorqueItems.HSLA_GEARS_8.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(itemModels, TorqueItems.HSLA_GEARS_16.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(itemModels, TorqueItems.HSLA_PISTON.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(itemModels, TorqueItems.HSLA_PLATE.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(itemModels, TorqueItems.HSLA_ROD.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(itemModels, TorqueItems.HSLA_STEEL_SHAFT.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(itemModels, TorqueItems.HSLA_TANK.get(), ModelType.COMPONENTS);
        generateFlatItemWithType(itemModels, TorqueItems.SHARP_HSLA_GEAR.get(), ModelType.COMPONENTS);

        itemWithBlockParent(itemModels, TorqueItems.ROTARY_MONITOR.get(), "rotary_monitor");

    }

    private static void horizontalWithExistingModel(ItemModelGenerators itemModels, BlockModelGenerators blockModels, Block block, String path) {
        var blockRLocation = ResourceLocation.fromNamespaceAndPath(TorqueCraft.MODID, path);
        var variant = new Variant(blockRLocation);
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block,
                        new MultiVariant(
                                WeightedList.of(variant))
                ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        itemWithBlockParent(itemModels, block.asItem(), blockRLocation);
    }

    private static void itemWithBlockParent(ItemModelGenerators itemModels, Item item, ResourceLocation parent) {
        itemModels.itemModelOutput.accept(
                item,
                ItemModelUtils.plainModel(parent)
        );
    }

    private static void itemWithBlockParent(ItemModelGenerators itemModels, Item item, String parent) {
        var parentLocation = ResourceLocation.fromNamespaceAndPath(TorqueCraft.MODID, "block/" + parent);
        itemModels.itemModelOutput.accept(
                item,
                ItemModelUtils.plainModel(parentLocation)
        );
    }

    private static void horizontalWithExistingModel(ItemModelGenerators itemModels, BlockModelGenerators blockModels, Block block) {
        var blockRLocation = ModelLocationUtils.getModelLocation(block);
        var variant = new Variant(blockRLocation);
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block,
                        new MultiVariant(
                                WeightedList.of(variant))
                ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        itemModels.itemModelOutput.accept(
                block.asItem(),
                ItemModelUtils.plainModel(blockRLocation)
        );
    }

    private static void generateFlatItemWithType(ItemModelGenerators itemModels, Item item, ModelType type) {
        var name = item.getName().getString().split("\\.")[2];
        var rl = ResourceLocation.fromNamespaceAndPath(TorqueCraft.MODID, "item/" + type.getSerializedName() + "/" + name);
        var template = ModelTemplates.FLAT_ITEM.create(rl, TextureMapping.layer0(rl), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(
                item,
                ItemModelUtils.plainModel(template)
        );
    }

    private static void createTrivialBlock(BlockModelGenerators blockModels, Block block, ModelType type) {
        var name = block.getName().getString().split("\\.")[2];
        var rl = ResourceLocation.fromNamespaceAndPath(TorqueCraft.MODID, "block/" + type.getSerializedName() + "/" + name);
        var texturedModel = TexturedModel.CUBE.updateTexture((t) ->
                t.put(TextureSlot.ALL, rl)
        );
        blockModels.createTrivialBlock(block, texturedModel);
    }
}
