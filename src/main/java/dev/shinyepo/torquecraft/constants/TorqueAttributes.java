package dev.shinyepo.torquecraft.constants;

import dev.shinyepo.torquecraft.config.GearboxRatio;
import dev.shinyepo.torquecraft.config.RotaryMode;
import dev.shinyepo.torquecraft.config.UsageConfig;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class TorqueAttributes {
    public static final BooleanProperty OPERATIONAL = BooleanProperty.create("operational");
    public static final EnumProperty<UsageConfig> USAGE = EnumProperty.create("usage", UsageConfig.class);

    public static final EnumProperty<GearboxRatio> RATIO = EnumProperty.create("ratio", GearboxRatio.class);
    public static final EnumProperty<RotaryMode> ROTARY_MODE = EnumProperty.create("rotary_mode", RotaryMode.class);
}
