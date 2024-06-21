package dev.shinyepo.torquecraft.datagen.loot;

import dev.shinyepo.torquecraft.block.prefab.CanolaCrop;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Set;

public class TorqueBlockLootTables extends BlockLootSubProvider {
    public TorqueBlockLootTables(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        dropSelf(TorqueBlocks.TUNGSTEN_BLOCK.get());
        dropSelf(TorqueBlocks.HSLA_BLOCK.get());
        dropSelf(TorqueBlocks.CAST_IRON_BLOCK.get());
        dropSelf(TorqueBlocks.ALLOY_FURNACE.get());
        dropSelf(TorqueBlocks.STEAM_ENGINE.get());
        dropSelf(TorqueBlocks.MECHANICAL_FAN.get());
        dropSelf(TorqueBlocks.GRINDER.get());
        dropSelf(TorqueBlocks.PUMP.get());
        dropSelf(TorqueBlocks.HSLA_SHAFT.get());
        dropSelf(TorqueBlocks.HSLA_BEVEL_GEARS.get());
        dropSelf(TorqueBlocks.HSLA_THREE_WAY.get());
        dropSelf(TorqueBlocks.FLUID_PIPE.get());
        dropSelf(TorqueBlocks.STEAM_PIPE.get());
        dropSelf(TorqueBlocks.FLUID_TANK.get());
        dropSelf(TorqueBlocks.HSLA_GEARBOX1_2.get());
        dropSelf(TorqueBlocks.HSLA_GEARBOX1_4.get());
        dropSelf(TorqueBlocks.COOLING_RADIATOR.get());
        dropSelf(TorqueBlocks.SPRINKLER.get());
        dropSelf(TorqueBlocks.VACUUM.get());

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
