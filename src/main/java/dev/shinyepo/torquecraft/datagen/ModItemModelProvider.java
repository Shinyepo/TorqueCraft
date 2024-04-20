package dev.shinyepo.torquecraft.datagen;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.TCBlocks;
import dev.shinyepo.torquecraft.item.TCItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TorqueCraft.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(TCItems.TUNGSTEN_INGOT.get());
        withExistingParent(String.valueOf(TCItems.TUNGSTEN_BLOCK_ITEM.get()).toLowerCase(), String.valueOf(TCBlocks.TUNGSTEN_BLOCK.get()).toLowerCase());
    }
}
