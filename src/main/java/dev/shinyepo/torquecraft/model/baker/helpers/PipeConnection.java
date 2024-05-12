package dev.shinyepo.torquecraft.model.baker.helpers;

import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;

public enum PipeConnection implements StringRepresentable {
    NONE,
    PIPE,
    BLOCK,
    INPUT,
    OUTPUT;

    public static final PipeConnection[] VALUES = values();

    @Override
    @Nonnull
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
