package dev.shinyepo.torquecraft.datagen;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.registries.TorqueBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TorqueCraft.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(TorqueBlocks.TUNGSTEN_BLOCK);

        ModelFile steamEngine = models().getExistingFile(modLoc("block/steam_engine"));
        this.getVariantBuilder(TorqueBlocks.STEAM_ENGINE.get()).forAllStatesExcept(blockState ->{
                    Direction dir = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    return ConfiguredModel.builder()
                            .modelFile(steamEngine)
                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                            .build();
                });
    }

    private void blockWithItem(Supplier<Block> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
}
