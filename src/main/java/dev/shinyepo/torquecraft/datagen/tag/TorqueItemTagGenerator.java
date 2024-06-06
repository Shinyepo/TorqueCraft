package dev.shinyepo.torquecraft.datagen.tag;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import dev.shinyepo.torquecraft.registries.tag.TorqueTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TorqueItemTagGenerator extends ItemTagsProvider {
    public TorqueItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, TorqueCraft.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(Tags.Items.SEEDS)
                .add(TorqueItems.CANOLA_SEEDS.get())
                .replace(false);

        tag(Tags.Items.BUCKETS)
                .add(TorqueItems.JET_FUEL_BUCKET.get())
                .add(TorqueItems.LUBRICANT_BUCKET.get())
                .replace(false);

        tag(Tags.Items.INGOTS)
                .add(TorqueItems.CAST_IRON_INGOT.get())
                .replace(false);

        tag(TorqueTags.SILICON)
                .add(TorqueItems.SILICON.get());

        tag(Tags.Items.STORAGE_BLOCKS)
                .add(TorqueItems.CAST_IRON_BLOCK_ITEM.get())
                .replace(false);
    }
}
