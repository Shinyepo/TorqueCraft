package dev.shinyepo.torquecraft.datagen.block;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.config.ModelType;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class TorqueBlockStateProvider extends ModelProvider {
    public TorqueBlockStateProvider(PackOutput output) {
        super(output, TorqueCraft.MODID);
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        generateTrivialBlockWithType(blockModels,TorqueBlocks.TUNGSTEN_BLOCK.get(), ModelType.MATERIALS);
        generateTrivialBlockWithType(blockModels,TorqueBlocks.HSLA_BLOCK.get(), ModelType.MATERIALS);
        generateTrivialBlockWithType(blockModels,TorqueBlocks.CAST_IRON_BLOCK.get(), ModelType.MATERIALS);
//        registerPipe(TorqueBlocks.FLUID_PIPE);

//        registerPipe(TorqueBlocks.STEAM_PIPE);

        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.MECHANICAL_FAN.get());
        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.STEAM_ENGINE.get());
        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.GRINDER.get());
        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.PUMP.get());
        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.SPRINKLER.get());
        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.HSLA_SHAFT.get());
        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.HSLA_BEVEL_GEARS.get());
        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.HSLA_THREE_WAY.get());
        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.VACUUM.get());
        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.COOLING_RADIATOR.get(), "block/radiator/cooling_radiator_full");
        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.ALLOY_FURNACE.get(), "block/furnace/alloy_furnace");
        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.HSLA_GEARBOX1_2.get(), "block/gearbox/hsla_gearbox1_2");
        generateHorizontalWithExistingModel(itemModels, blockModels, TorqueBlocks.HSLA_GEARBOX1_4.get(), "block/gearbox/hsla_gearbox1_4");


//        horizontalWithExistingModel(blockModels, TorqueBlocks.FLUID_TANK.get());

        generateCropBlockWithName(blockModels, TorqueBlocks.CANOLA_CROP.get(),"canola", BlockStateProperties.AGE_5, 0, 1, 2, 3, 4, 5);

        blockModels.createNonTemplateModelBlock(TorqueBlocks.LUBRICANT_BLOCK.get());
        blockModels.createNonTemplateModelBlock(TorqueBlocks.JET_FUEL_BLOCK.get());

        //Flat items
        itemModels.generateFlatItem(TorqueItems.CRUSHED_SEEDS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(TorqueItems.CANOLA_SEEDS.get(), ModelTemplates.FLAT_ITEM);
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

        generateItemWithExistingParent(itemModels, TorqueItems.ROTARY_MONITOR.get(), "rotary_monitor");

    }

    private static void generateHorizontalWithExistingModel(ItemModelGenerators itemModels, BlockModelGenerators blockModels, Block block, String path) {
        var blockRLocation = ResourceLocation.fromNamespaceAndPath(TorqueCraft.MODID, path);
        var variant = new Variant(blockRLocation);
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block,
                        new MultiVariant(
                                WeightedList.of(variant))
                ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        generateItemWithExistingParent(itemModels, block.asItem(), blockRLocation);
    }

    private static void generateItemWithExistingParent(ItemModelGenerators itemModels, Item item, ResourceLocation parent) {
        itemModels.itemModelOutput.accept(
                item,
                ItemModelUtils.plainModel(parent)
        );
    }

    private static void generateItemWithExistingParent(ItemModelGenerators itemModels, Item item, String parent) {
        var parentLocation = ResourceLocation.fromNamespaceAndPath(TorqueCraft.MODID, "block/" + parent);
        itemModels.itemModelOutput.accept(
                item,
                ItemModelUtils.plainModel(parentLocation)
        );
    }

    private static void generateHorizontalWithExistingModel(ItemModelGenerators itemModels, BlockModelGenerators blockModels, Block block) {
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

    private static void generateTrivialBlockWithType(BlockModelGenerators blockModels, Block block, ModelType type) {
        var name = block.getName().getString().split("\\.")[2];
        var rl = ResourceLocation.fromNamespaceAndPath(TorqueCraft.MODID, "block/" + type.getSerializedName() + "/" + name);
        var texturedModel = TexturedModel.CUBE.updateTexture((t) ->
                t.put(TextureSlot.ALL, rl)
        );
        blockModels.createTrivialBlock(block, texturedModel);
    }

    private static void generateCropBlockWithName(BlockModelGenerators blockModels, Block block, String seedName, Property<Integer> ageProperty, int... ageToMap) {
        if (ageProperty.getPossibleValues().size() != ageToMap.length) {
            throw new IllegalArgumentException();
        } else {
            Int2ObjectMap<ResourceLocation> int2ObjectMap = new Int2ObjectOpenHashMap<>();
            blockModels.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block)
                            .with(
                                    PropertyDispatch.initial(ageProperty)
                                            .generate(gen -> {
                                                int i = ageToMap[gen];
                                                return BlockModelGenerators.plainVariant(
                                                        int2ObjectMap.computeIfAbsent(
                                                                i,
                                                                idx -> createVariantWithTexturePath(ModelLocationUtils.getModelLocation(block, "_stage"+idx), "block/seeds/"+seedName+"/"+seedName+"_stage"+idx,TextureMapping::crop,  blockModels)
                                                        )
                                                );
                                            })
                            )
            );
        }
    }

    private static ResourceLocation createVariantWithTexturePath(ResourceLocation modelLoc, String path, Function<ResourceLocation, TextureMapping> textureMappingGetter, BlockModelGenerators blockModels) {
        var location = ResourceLocation.fromNamespaceAndPath(TorqueCraft.MODID,path);
        return ModelTemplates.CROP.extend().renderType("minecraft:cutout").build().create(modelLoc,textureMappingGetter.apply(location), blockModels.modelOutput);
    }


}
