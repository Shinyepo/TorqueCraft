package dev.shinyepo.torquecraft.config;

import net.minecraft.util.StringRepresentable;

public enum RotaryMode implements StringRepresentable {
    ANGULAR(),
    TORQUE();


    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
