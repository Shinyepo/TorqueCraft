package dev.shinyepo.torquecraft.datagen.block;

import com.google.gson.JsonObject;
import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.prefab.CanolaCrop;
import dev.shinyepo.torquecraft.block.prefab.CoolingRadiator;
import dev.shinyepo.torquecraft.datagen.helpers.CustomBlockStateProvider;
import dev.shinyepo.torquecraft.model.baker.helpers.PipeModelLoader;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class TorqueBlockStateProvider extends CustomBlockStateProvider {
    public TorqueBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TorqueCraft.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockMaterialWithItem(TorqueBlocks.TUNGSTEN_BLOCK.get());
        blockMaterialWithItem(TorqueBlocks.HSLA_BLOCK.get());
        blockMaterialWithItem(TorqueBlocks.CAST_IRON_BLOCK.get());
        registerPipe(TorqueBlocks.FLUID_PIPE);
        registerPipe(TorqueBlocks.STEAM_PIPE);

        //TEMP
        registerHorizontalMachineWithExistingModel("block/mechanical_fan", TorqueBlocks.MECHANICAL_FAN);
        registerHorizontalMachineWithExistingModel("block/steam_engine", TorqueBlocks.STEAM_ENGINE);
        registerHorizontalMachineWithExistingModel("block/grinder", TorqueBlocks.GRINDER);
        registerHorizontalMachineWithExistingModel("block/pump", TorqueBlocks.PUMP);
        registerHorizontalMachineWithExistingModel("block/sprinkler", TorqueBlocks.SPRINKLER);
        registerHorizontalMachineWithExistingModel("block/shaft", TorqueBlocks.SHAFT);
        registerHorizontalMachineWithExistingModel("block/bevel_gears", TorqueBlocks.BEVEL_GEARS);
        registerHorizontalMachineWithExistingModel("block/gearbox/1_2", TorqueBlocks.GEARBOX_1_2);
        registerHorizontalMachineWithExistingModel("block/gearbox/1_4", TorqueBlocks.GEARBOX_1_4);
        registerHorizontalMachineWithExistingModel("block/gearbox/1_8", TorqueBlocks.GEARBOX_1_8);
        registerHorizontalMachineWithExistingModel("block/gearbox/1_16", TorqueBlocks.GEARBOX_1_16);
        registerHorizontalMachineWithExistingModel("block/three_way", TorqueBlocks.THREE_WAY);
        registerHorizontalMachineWithExistingModel("block/fluid_tank", TorqueBlocks.FLUID_TANK);
        registerHorizontalMachineWithExistingModel("block/vacuum", TorqueBlocks.VACUUM);
        registerMachineWithExistingModel("block/centrifuge", TorqueBlocks.CENTRIFUGE);

        makeHorizontalLitBlock("block/furnace/alloy_furnace", TorqueBlocks.ALLOY_FURNACE);
        registerFluid(TorqueBlocks.LUBRICANT_BLOCK);
        registerFluid(TorqueBlocks.JET_FUEL_BLOCK);
    
        makeCanolaCrop((CropBlock) TorqueBlocks.CANOLA_CROP.get(), "canola_stage", "canola_stage");
        makeRadiator(TorqueBlocks.COOLING_RADIATOR);

    }

    private void registerPipe(Supplier<Block> pipe) {
        BlockModelBuilder model = models().getBuilder("pipe")
                .parent(models().getExistingFile(mcLoc("cube")))
                .customLoader((builder, helper) -> new PipeLoaderBuilder(PipeModelLoader.GENERATOR_LOADER, builder, helper))
                .end();
        simpleBlock(pipe.get(), model);
    }

    public void makeRadiator(Supplier<Block> block) {
        this.getVariantBuilder(block.get()).forAllStatesExcept(blockState ->{
            Direction dir = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            var usage = blockState.getValue(((CoolingRadiator) block.get()).getUsage()).getSerializedName();
            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(fromNamespaceAndPath(TorqueCraft.MODID, "block/radiator/cooling_radiator_" + usage)))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }

    private void makeHorizontalLitBlock(String modelPath, Supplier<Block> block) {
        this.getVariantBuilder(block.get()).forAllStates(blockState -> {
            Direction dir = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            Boolean lit = blockState.getValue(BlockStateProperties.LIT);
            ModelFile model = models().getExistingFile(modLoc(modelPath + (lit ? "_lit" : "")));
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }

    public void makeCanolaCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> canolaStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] canolaStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((CanolaCrop) block).getAgeProperty()),
                fromNamespaceAndPath(TorqueCraft.MODID, "block/seeds/canola/" + textureName + state.getValue(((CanolaCrop) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    public void registerHorizontalMachineWithExistingModel(String modelPath, Supplier<Block> block) {
        ModelFile machineModel = models().getExistingFile(modLoc(modelPath + "/block"));
        this.getVariantBuilder(block.get()).forAllStatesExcept(blockState ->{
            Direction dir = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            return ConfiguredModel.builder()
                    .modelFile(machineModel)
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }

    public void registerMachineWithExistingModel(String modelPath, Supplier<Block> block) {
        ModelFile machineModel = models().getExistingFile(modLoc(modelPath + "/block"));
        this.getVariantBuilder(block.get()).forAllStatesExcept(blockState -> ConfiguredModel.builder()
                .modelFile(machineModel)
                .build());
    }

    public void registerFluid(Supplier<LiquidBlock> block) {
        ResourceLocation location = BuiltInRegistries.BLOCK.getKey(block.get());
        BlockModelBuilder model = models().getBuilder(location.getPath()).texture("particle", "minecraft:block/water_still");

        getVariantBuilder(block.get()).partialState().setModels(new ConfiguredModel(model));
    }

    public static class PipeLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {

//        private final boolean facade;

        public PipeLoaderBuilder(ResourceLocation loader, BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
            super(loader, parent, existingFileHelper, false);
//            this.facade = facade;
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            JsonObject obj = super.toJson(json);
//            obj.addProperty("facade", facade);
            return obj;
        }
    }
}
