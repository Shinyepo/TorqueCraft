package dev.shinyepo.torquecraft.config;

public enum ModelType {
    MATERIALS,
    COMPONENTS,
    FURNACE,
    GEARBOX,
    ITEM,
    PARTIAL,
    RADIATOR;

    public String getSerializedName() {
        return name().toLowerCase();
    }
}
