package dev.shinyepo.torquecraft.block;

import dev.shinyepo.torquecraft.TorqueCraft;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TCBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TorqueCraft.MODID);

    public static final Supplier<Block> TUNGSTEN_BLOCK = BLOCKS.registerSimpleBlock("tungsten_block", BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(MapColor.STONE));
}
