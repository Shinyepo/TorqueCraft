package dev.shinyepo.torquecraft.datagen;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.TCBlocks;
import dev.shinyepo.torquecraft.item.TCItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TorqueCraft.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(TCItems.TUNGSTEN_INGOT.get());

        withExistingParent(String.valueOf(TCItems.TUNGSTEN_BLOCK_ITEM.get()).toLowerCase(),
                "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(TCBlocks.TUNGSTEN_BLOCK.get()).getPath());

        withExistingParent(String.valueOf(TCItems.STEAM_ENGINE_ITEM.get()).toLowerCase(),
                "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(TCBlocks.STEAM_ENGINE.get()).getPath());
    }
}
