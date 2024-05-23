package dev.shinyepo.torquecraft.config;

import dev.shinyepo.torquecraft.model.baker.helpers.PipeConnection;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;

public enum UsageConfig implements StringRepresentable {
    ZERO (0),
    LOW (25),
    MEDIUM (50),
    HIGH (75),
    FULL (100);

    private final int percent;

    public static final UsageConfig[] VALUES = values();

    UsageConfig(int percent) {
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    @Override
    @Nonnull
    public String getSerializedName() {
        return name().toLowerCase();
    }

    public UsageConfig getNext() {
        return VALUES[(this.ordinal() + 1) % VALUES.length];
    }
}
