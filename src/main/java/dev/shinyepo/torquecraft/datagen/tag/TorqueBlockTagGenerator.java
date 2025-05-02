package dev.shinyepo.torquecraft.datagen.tag;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.tag.TorqueTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class TorqueBlockTagGenerator extends BlockTagsProvider {
    public TorqueBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, TorqueCraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        tag(Tags.Blocks.STORAGE_BLOCKS)
                .add(TorqueBlocks.CAST_IRON_BLOCK.get())
                .replace(false);

        tag(TorqueTags.HEAT_SOURCE)
                .add(Blocks.LAVA, Blocks.MAGMA_BLOCK)
                .addTag(BlockTags.FIRE)
                .addTag(BlockTags.CAMPFIRES);

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(TorqueBlocks.ALLOY_FURNACE.get())
                .add(TorqueBlocks.TUNGSTEN_BLOCK.get())
                .add(TorqueBlocks.STEAM_ENGINE.get())
                .add(TorqueBlocks.MECHANICAL_FAN.get())
                .add(TorqueBlocks.GRINDER.get())
//                .add(TorqueBlocks.FLUID_TANK.get())
                .add(TorqueBlocks.PUMP.get())
                .add(TorqueBlocks.COOLING_RADIATOR.get())
                .add(TorqueBlocks.HSLA_THREE_WAY.get())
                .add(TorqueBlocks.HSLA_BEVEL_GEARS.get())
                .add(TorqueBlocks.HSLA_GEARBOX1_2.get())
                .add(TorqueBlocks.HSLA_GEARBOX1_4.get())
                .add(TorqueBlocks.HSLA_SHAFT.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(TorqueBlocks.ALLOY_FURNACE.get())
                .add(TorqueBlocks.TUNGSTEN_BLOCK.get())
                .add(TorqueBlocks.STEAM_ENGINE.get())
                .add(TorqueBlocks.MECHANICAL_FAN.get())
                .add(TorqueBlocks.GRINDER.get())
//                .add(TorqueBlocks.FLUID_TANK.get())
                .add(TorqueBlocks.PUMP.get())
                .add(TorqueBlocks.COOLING_RADIATOR.get())
                .add(TorqueBlocks.HSLA_THREE_WAY.get())
                .add(TorqueBlocks.HSLA_BEVEL_GEARS.get())
                .add(TorqueBlocks.HSLA_GEARBOX1_2.get())
                .add(TorqueBlocks.HSLA_GEARBOX1_4.get())
                .add(TorqueBlocks.HSLA_SHAFT.get());
    }
}
