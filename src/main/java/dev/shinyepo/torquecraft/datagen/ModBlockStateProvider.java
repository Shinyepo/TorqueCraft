package dev.shinyepo.torquecraft.datagen;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.prefab.CanolaCrop;
import dev.shinyepo.torquecraft.registries.TorqueBlocks;
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
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TorqueCraft.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(TorqueBlocks.TUNGSTEN_BLOCK.get());

        //TEMP
        registerHorizontalMachineWithExistingModel("block/mechanical_fan", TorqueBlocks.MECHANICAL_FAN);
        registerHorizontalMachineWithExistingModel("block/steam_engine", TorqueBlocks.STEAM_ENGINE);
        registerHorizontalMachineWithExistingModel("block/grinder", TorqueBlocks.GRINDER);
        registerHorizontalMachineWithExistingModel("block/pump", TorqueBlocks.PUMP);

        registerFluid(TorqueBlocks.LUBRICANT_BLOCK);
        registerFluid(TorqueBlocks.JET_FUEL_BLOCK);
    
        makeCanolaCrop((CropBlock) TorqueBlocks.CANOLA_CROP.get(), "canola_stage", "canola_stage");

    }

    private void blockWithItem(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }

    public void makeCanolaCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> canolaStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] canolaStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((CanolaCrop) block).getAgeProperty()),
                new ResourceLocation(TorqueCraft.MODID, "block/"+ textureName + state.getValue(((CanolaCrop) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    public void registerHorizontalMachineWithExistingModel(String modelPath, Supplier<Block> block) {
        ModelFile machineModel = models().getExistingFile(modLoc(modelPath));
        this.getVariantBuilder(block.get()).forAllStatesExcept(blockState ->{
            Direction dir = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            return ConfiguredModel.builder()
                    .modelFile(machineModel)
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }

    public void registerFluid(Supplier<LiquidBlock> block) {
        ResourceLocation location = BuiltInRegistries.BLOCK.getKey(block.get());
        BlockModelBuilder model = models().getBuilder(location.getPath()).texture("particle", "minecraft:block/water_still");

        getVariantBuilder(block.get()).partialState().setModels(new ConfiguredModel(model));
    }
}
