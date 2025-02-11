package dev.shinyepo.torquecraft.registries.tag;

import dev.shinyepo.torquecraft.TorqueCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class TorqueTags {
    public static final TagKey<Block> HEAT_SOURCE = TagKey.create(Registries.BLOCK, fromNamespaceAndPath(TorqueCraft.MODID, "heat_sources"));
    public static final TagKey<Item> SILICON = TagKey.create(Registries.ITEM, fromNamespaceAndPath("c", "silicon"));
}
