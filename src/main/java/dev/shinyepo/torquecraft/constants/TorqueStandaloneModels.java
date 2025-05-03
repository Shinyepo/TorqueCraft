package dev.shinyepo.torquecraft.constants;

import dev.shinyepo.torquecraft.TorqueCraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class TorqueStandaloneModels {
    public static final StandaloneModelKey<BlockStateModel> FAN_BLADE = new StandaloneModelKey<>(fromNamespaceAndPath(TorqueCraft.MODID, "block/partial/fan_blade"));
    public static final StandaloneModelKey<BlockStateModel> SHAFT_ROD = new StandaloneModelKey<>(fromNamespaceAndPath(TorqueCraft.MODID, "block/partial/shaft_rod"));
    public static final StandaloneModelKey<BlockStateModel> HSLA_SHAFT_ROD = new StandaloneModelKey<>(fromNamespaceAndPath(TorqueCraft.MODID, "block/partial/hsla_shaft_rod"));
    public static final StandaloneModelKey<BlockStateModel> HSLA_SHORT_SHAFT_ROD = new StandaloneModelKey<>(fromNamespaceAndPath(TorqueCraft.MODID, "block/partial/hsla_short_shaft_rod"));
    public static final StandaloneModelKey<BlockStateModel> SHORT_SHAFT_ROD = new StandaloneModelKey<>(fromNamespaceAndPath(TorqueCraft.MODID, "block/partial/short_shaft_rod"));
    public static final StandaloneModelKey<BlockStateModel> GRINDER_SHAFT = new StandaloneModelKey<>(fromNamespaceAndPath(TorqueCraft.MODID, "block/partial/grinder_shaft"));
    public static final StandaloneModelKey<BlockStateModel> ROTARY_MONITOR = new StandaloneModelKey<>(fromNamespaceAndPath(TorqueCraft.MODID, "block/partial/rotary_monitor"));
}
