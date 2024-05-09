package dev.shinyepo.torquecraft.datagen.loot;

import dev.shinyepo.torquecraft.block.prefab.CanolaCrop;
import dev.shinyepo.torquecraft.registries.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.TorqueItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(TorqueBlocks.TUNGSTEN_BLOCK.get());
        dropSelf(TorqueBlocks.STEAM_ENGINE.get());
        dropSelf(TorqueBlocks.MECHANICAL_FAN.get());
        dropSelf(TorqueBlocks.GRINDER.get());
        dropSelf(TorqueBlocks.PUMP.get());
        dropSelf(TorqueBlocks.SHAFT.get());
        dropSelf(TorqueBlocks.FLUID_PIPE.get());
        dropSelf(TorqueBlocks.STEAM_PIPE.get());
        dropSelf(TorqueBlocks.FLUID_TANK.get());

        //Crops
        LootItemCondition.Builder licBuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(TorqueBlocks.CANOLA_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CanolaCrop.AGE, 5));

        add(TorqueBlocks.CANOLA_CROP.get(), createCropDrops(TorqueBlocks.CANOLA_CROP.get(), TorqueItems.CANOLA_SEEDS.get(), TorqueItems.CANOLA_SEEDS.get(), licBuilder));

//        add(TCBlocks.ORE.get(),
//                block -> createRedstoneOreDrops(TCBlocks.ORE.get(), TCItems.RAW.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return TorqueBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
