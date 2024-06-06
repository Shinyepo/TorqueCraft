package dev.shinyepo.torquecraft.datagen.helpers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public abstract class CustomBlockStateProvider extends BlockStateProvider {

    public CustomBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }


    protected void blockWithItem(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }

    protected void blockMaterialWithItem(Block block) {
        simpleBlockWithItem(block, cubeAll(block, "materials/"));
    }

    private ModelFile cubeAll(Block block, String folder) {
        return models().cubeAll(name(block), blockTexture(block, folder));
    }

    public ResourceLocation blockTexture(Block block, String folder) {
        ResourceLocation name = key(block);
        return new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + folder + name.getPath());
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
}
