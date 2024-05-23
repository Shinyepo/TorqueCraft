package dev.shinyepo.torquecraft.datagen.tag;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.tag.TorqueTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TorqueBlockTagGenerator extends BlockTagsProvider {
    public TorqueBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TorqueCraft.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(TorqueTags.HEAT_SOURCE)
                .add(Blocks.LAVA, Blocks.MAGMA_BLOCK)
                .addTag(BlockTags.FIRE)
                .addTag(BlockTags.CAMPFIRES);

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(TorqueBlocks.TUNGSTEN_BLOCK.get())
                .add(TorqueBlocks.STEAM_ENGINE.get())
                .add(TorqueBlocks.MECHANICAL_FAN.get())
                .add(TorqueBlocks.GRINDER.get())
                .add(TorqueBlocks.FLUID_TANK.get())
                .add(TorqueBlocks.PUMP.get())
                .add(TorqueBlocks.COOLING_RADIATOR.get())
                .add(TorqueBlocks.SHAFT.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(TorqueBlocks.TUNGSTEN_BLOCK.get())
                .add(TorqueBlocks.STEAM_ENGINE.get())
                .add(TorqueBlocks.MECHANICAL_FAN.get())
                .add(TorqueBlocks.GRINDER.get())
                .add(TorqueBlocks.FLUID_TANK.get())
                .add(TorqueBlocks.PUMP.get())
                .add(TorqueBlocks.COOLING_RADIATOR.get())
                .add(TorqueBlocks.SHAFT.get());
    }
}
