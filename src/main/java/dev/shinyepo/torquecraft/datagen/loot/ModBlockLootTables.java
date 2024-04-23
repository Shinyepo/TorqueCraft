package dev.shinyepo.torquecraft.datagen.loot;

import dev.shinyepo.torquecraft.registries.TorqueBlocks;
import net.minecraft.core.Holder;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(TorqueBlocks.TUNGSTEN_BLOCK.get());
        dropSelf(TorqueBlocks.STEAM_ENGINE.get());

//        add(TCBlocks.ORE.get(),
//                block -> createRedstoneOreDrops(TCBlocks.ORE.get(), TCItems.RAW.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return TorqueBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
