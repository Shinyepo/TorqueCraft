package dev.shinyepo.torquecraft.datagen.tag;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import dev.shinyepo.torquecraft.registries.tag.TorqueTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class TorqueItemTagProvider extends ItemTagsProvider {

    public TorqueItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags) {
        super(pOutput, pLookupProvider, pBlockTags, TorqueCraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        tag(Tags.Items.SEEDS)
                .add(TorqueItems.CANOLA_SEEDS.get())
                .replace(false);

        tag(Tags.Items.BUCKETS)
                .add(TorqueItems.JET_FUEL_BUCKET.get())
                .add(TorqueItems.LUBRICANT_BUCKET.get())
                .replace(false);

        tag(Tags.Items.TOOLS)
                .add(TorqueItems.ROTARY_WRENCH.get())
                .add(TorqueItems.PRESSURE_GAUGE.get())
                .add(TorqueItems.ROTARY_MONITOR.get())
                .replace(false);

        tag(Tags.Items.TOOLS_WRENCH)
                .add(TorqueItems.ROTARY_WRENCH.get())
                .replace(false);

        tag(TorqueTags.SHAFTS)
                .add(TorqueItems.HSLA_SHAFT_ITEM.get())
                .add(TorqueItems.HSLA_THREE_WAY_ITEM.get())
                .add(TorqueItems.HSLA_BEVEL_GEARS_ITEM.get())
                .add(TorqueItems.HSLA_GEARBOX1_2_ITEM.get())
                .add(TorqueItems.HSLA_GEARBOX1_4_ITEM.get());

        tag(TorqueTags.MACHINES)
                .add(TorqueItems.STEAM_ENGINE_ITEM.get())
                .add(TorqueItems.MECHANICAL_FAN_ITEM.get())
                .add(TorqueItems.GRINDER_ITEM.get())
                .add(TorqueItems.PUMP_ITEM.get())
                .add(TorqueItems.SPRINKLER_ITEM.get())
                .add(TorqueItems.VACUUM_ITEM.get());

        tag(TorqueTags.MISC_MACHINES)
                .add(TorqueItems.FLUID_TANK_ITEM.get())
                .add(TorqueItems.COOLING_RADIATOR_ITEM.get());

        tag(TorqueTags.COMPONENTS)
                .add(TorqueItems.HSLA_ROD.get())
                .add(TorqueItems.HSLA_STEEL_SHAFT.get())
                .add(TorqueItems.SHARP_HSLA_GEAR.get())
                .add(TorqueItems.HSLA_GEARS_2.get())
                .add(TorqueItems.HSLA_GEARS_4.get())
                .add(TorqueItems.HSLA_GEARS_8.get())
                .add(TorqueItems.HSLA_GEARS_16.get())
                .add(TorqueItems.HSLA_CASING.get())
                .add(TorqueItems.HSLA_PISTON.get())
                .add(TorqueItems.HSLA_TANK.get())
                .add(TorqueItems.HSLA_PLATE.get())
                .add(TorqueItems.CIRCUIT_MODULE.get());

        tag(Tags.Items.INGOTS)
                .add(TorqueItems.CAST_IRON_INGOT.get())
                .add(TorqueItems.TUNGSTEN_INGOT.get())
                .add(TorqueItems.HSLA_INGOT.get())
                .replace(false);

        tag(Tags.Items.DUSTS)
                .add(TorqueItems.QUARTZ_DUST.get())
                .add(TorqueItems.COPPER_DUST.get())
                .add(TorqueItems.IRON_DUST.get())
                .add(TorqueItems.GOLD_DUST.get())
                .add(TorqueItems.DIAMOND_DUST.get())
                .add(TorqueItems.EMERALD_DUST.get())
                .add(TorqueItems.NETHERITE_DUST.get())
                .add(TorqueItems.OBSIDIAN_DUST.get())
                .replace(false);

        tag(TorqueTags.SILICON)
                .add(TorqueItems.SILICON.get());
    }
}
