package dev.shinyepo.torquecraft.config;

import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;

public enum RotaryMode implements StringRepresentable {
    ANGULAR,
    TORQUE;

    private static final RotaryMode[] VALUES = values();

    @Override
    @Nonnull
    public String getSerializedName() {
        return name().toLowerCase();
    }

    public RotaryMode getNext() {
        return VALUES[(this.ordinal() + 1) % VALUES.length];
    }
}
