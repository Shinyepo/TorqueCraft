package dev.shinyepo.torquecraft.datagen.models;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.config.ModelType;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class TorqueBlockModels extends BlockModelGenerators {
    public TorqueBlockModels(Consumer<BlockModelDefinitionGenerator> blockStateOutput, ItemModelOutput itemModelOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
        super(blockStateOutput, itemModelOutput, modelOutput);
    }

    @Override
    public void run() {
        createNonTemplateModelBlock(TorqueBlocks.LUBRICANT_BLOCK.get());
        createNonTemplateModelBlock(TorqueBlocks.JET_FUEL_BLOCK.get());

        generateTrivialBlockWithType(TorqueBlocks.TUNGSTEN_BLOCK.get(), ModelType.MATERIALS);
        generateTrivialBlockWithType(TorqueBlocks.HSLA_BLOCK.get(), ModelType.MATERIALS);
        generateTrivialBlockWithType(TorqueBlocks.CAST_IRON_BLOCK.get(), ModelType.MATERIALS);

        generateHorizontalWithExistingModel(TorqueBlocks.MECHANICAL_FAN.get());
        generateHorizontalWithExistingModel(TorqueBlocks.STEAM_ENGINE.get());
        generateHorizontalWithExistingModel(TorqueBlocks.GRINDER.get());
        generateHorizontalWithExistingModel(TorqueBlocks.PUMP.get());
        generateHorizontalWithExistingModel(TorqueBlocks.SPRINKLER.get());
        generateHorizontalWithExistingModel(TorqueBlocks.HSLA_SHAFT.get());
        generateHorizontalWithExistingModel(TorqueBlocks.HSLA_BEVEL_GEARS.get());
        generateHorizontalWithExistingModel(TorqueBlocks.HSLA_THREE_WAY.get());
        generateHorizontalWithExistingModel(TorqueBlocks.VACUUM.get());
        generateHorizontalWithExistingModel(TorqueBlocks.FLUID_TANK.get());
        generateHorizontalWithExistingModel(TorqueBlocks.COOLING_RADIATOR.get(), ModelType.RADIATOR);
        generateHorizontalWithExistingModel(TorqueBlocks.ALLOY_FURNACE.get(), ModelType.FURNACE);
        generateHorizontalWithExistingModel(TorqueBlocks.HSLA_GEARBOX1_2.get(), ModelType.GEARBOX);
        generateHorizontalWithExistingModel(TorqueBlocks.HSLA_GEARBOX1_4.get(), ModelType.GEARBOX);

        generateCropBlockWithName(TorqueBlocks.CANOLA_CROP.get(), "canola", BlockStateProperties.AGE_5, 0, 1, 2, 3, 4, 5);


    }

    private String getPathWithType(Block block, ModelType type) {
        return "block/" + type.getSerializedName() + "/" + block.getName().getString().split("\\.")[2];
    }

    private void generateHorizontalWithExistingModel(Block block, ResourceLocation modelLocation) {
        blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block,
                        new MultiVariant(
                                WeightedList.of(
                                        new Variant(modelLocation)
                                ))
                ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        itemModelOutput.accept(
                block.asItem(),
                ItemModelUtils.plainModel(modelLocation)
        );
    }

    private void generateHorizontalWithExistingModel(Block block, ModelType type) {
        generateHorizontalWithExistingModel(block, fromNamespaceAndPath(TorqueCraft.MODID, getPathWithType(block, type)));
    }

    private void generateHorizontalWithExistingModel(Block block) {
        generateHorizontalWithExistingModel(block, ModelLocationUtils.getModelLocation(block));
    }

    private void generateTrivialBlockWithType(Block block, ModelType type) {
        var texturedModel = TexturedModel.CUBE.updateTexture((t) ->
                t.put(
                        TextureSlot.ALL,
                        fromNamespaceAndPath(TorqueCraft.MODID, getPathWithType(block, type))
                )
        );
        createTrivialBlock(block, texturedModel);
    }

    private void generateCropBlockWithName(Block block, String seedName, Property<Integer> ageProperty, int... ageToMap) {
        if (ageProperty.getPossibleValues().size() != ageToMap.length) {
            throw new IllegalArgumentException();
        } else {
            Int2ObjectMap<ResourceLocation> int2ObjectMap = new Int2ObjectOpenHashMap<>();
            blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block)
                            .with(
                                    PropertyDispatch.initial(ageProperty)
                                            .generate(gen -> {
                                                int i = ageToMap[gen];
                                                return BlockModelGenerators.plainVariant(
                                                        int2ObjectMap.computeIfAbsent(
                                                                i,
                                                                idx -> createVariantWithTexturePath(ModelLocationUtils.getModelLocation(block, "_stage" + idx), "block/seeds/" + seedName + "/" + seedName + "_stage" + idx, TextureMapping::crop)
                                                        )
                                                );
                                            })
                            )
            );
        }
    }

    private ResourceLocation createVariantWithTexturePath(ResourceLocation modelLoc, String path, Function<ResourceLocation, TextureMapping> textureMappingGetter) {
        return ModelTemplates.CROP.extend().renderType("minecraft:cutout").build().create(
                modelLoc,
                textureMappingGetter.apply(fromNamespaceAndPath(TorqueCraft.MODID, path)),
                modelOutput
        );
    }
}
