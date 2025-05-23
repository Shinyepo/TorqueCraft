package dev.shinyepo.torquecraft.registries.model.partial;

import dev.shinyepo.torquecraft.TorqueCraft;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class TorquePartialModels {
    public static final List<ResourceLocation> PARTIAL_CACHE = new ArrayList<>();
    private static final String PATH = "block/partial/";

    public static final ResourceLocation BEVEL_INPUT = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "bevel_input"));
    public static final ResourceLocation BEVEL_OUTPUT = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "bevel_output"));

    public static final ResourceLocation FAN_BLADE = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "fan_blade"));

    public static final ResourceLocation GRINDER_SHAFT = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "grinder_shaft"));

    public static final ResourceLocation ROTARY_MONITOR = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "rotary_monitor"));

    public static final ResourceLocation SHAFT_ROD = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "shaft_rod"));

    public static final ResourceLocation OUTPUT_SHAFT_ROD = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "output_shaft_rod"));
    public static final ResourceLocation INPUT_SHAFT_ROD = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "input_shaft_rod"));

    public static final ResourceLocation SPRINKLER_HEAD = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "sprinkler_head"));

    public static final ResourceLocation THREE_WAY_INPUT = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "three_way_in_out"));
    public static final ResourceLocation THREE_WAY_SIDE = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "three_way_side"));

    public static final ResourceLocation HIGHLIGHT_BOX = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "highlight/box"));
    public static final ResourceLocation HIGHLIGHT_ARROW = register(fromNamespaceAndPath(TorqueCraft.MODID, PATH + "highlight/arrow"));

    private static ResourceLocation register(ResourceLocation loc) {
        PARTIAL_CACHE.add(loc);
        return loc;

    }
}
