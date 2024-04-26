package dev.shinyepo.torquecraft.registries;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.prefab.CanolaCrop;
import dev.shinyepo.torquecraft.block.prefab.MechanicalFan;
import dev.shinyepo.torquecraft.block.prefab.SteamEngine;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TorqueCraft.MODID);

    public static final Supplier<Block> TUNGSTEN_BLOCK = BLOCKS.registerSimpleBlock("tungsten_block", BlockBehaviour.Properties.of().destroyTime(2f).requiresCorrectToolForDrops().mapColor(MapColor.STONE));

    public static final Supplier<Block> STEAM_ENGINE = BLOCKS.register("steam_engine",() -> new SteamEngine(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final Supplier<Block> MECHANICAL_FAN_BLOCK = BLOCKS.register("mechanical_fan",() -> new MechanicalFan(BlockBehaviour.Properties.of().destroyTime(1.5f).noOcclusion().requiresCorrectToolForDrops().mapColor(MapColor.STONE)));

    //Crops
    public static final Supplier<Block> CANOLA_CROP = BLOCKS.register("canola_crop", () -> new CanolaCrop(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noOcclusion().noCollission()));
}
