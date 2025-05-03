package dev.shinyepo.torquecraft.datagen.models;

import dev.shinyepo.torquecraft.TorqueCraft;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;


public class TorqueModelProvider extends ModelProvider {
    public TorqueModelProvider(PackOutput output) {
        super(output, TorqueCraft.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        new TorqueBlockModels(blockModels.blockStateOutput, blockModels.itemModelOutput, blockModels.modelOutput).run();
        new TorqueItemModels(itemModels.itemModelOutput, itemModels.modelOutput).run();
    }
}
