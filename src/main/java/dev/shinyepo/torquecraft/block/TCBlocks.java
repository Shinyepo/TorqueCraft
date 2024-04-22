package dev.shinyepo.torquecraft.block;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.custom.Engines;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TCBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TorqueCraft.MODID);

    public static final Supplier<Block> TUNGSTEN_BLOCK = BLOCKS.registerSimpleBlock("tungsten_block", BlockBehaviour.Properties.of().destroyTime(2f).requiresCorrectToolForDrops().mapColor(MapColor.STONE));
    public static final Supplier<Block> STEAM_ENGINE = BLOCKS.register("steam_engine",() -> new Engines(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
}
