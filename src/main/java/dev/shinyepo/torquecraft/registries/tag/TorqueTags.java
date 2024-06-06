package dev.shinyepo.torquecraft.registries.tag;

import dev.shinyepo.torquecraft.TorqueCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TorqueTags {
    public static final TagKey<Block> HEAT_SOURCE = TagKey.create(Registries.BLOCK, new ResourceLocation(TorqueCraft.MODID, "heat_sources"));
    public static final TagKey<Item> SILICON = TagKey.create(Registries.ITEM, new ResourceLocation("c", "silicon"));
}
