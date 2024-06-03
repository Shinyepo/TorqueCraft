package dev.shinyepo.torquecraft.config;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum GearboxRatio implements StringRepresentable {
    RATIO_2(2),
    RATIO_4(4),
    RATIO_8(8),
    RATIO_16(16);

    private final int ratio;

    GearboxRatio(int ratio) {
        this.ratio = ratio;
    }

    public int getRatio() {
        return ratio;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }
}
