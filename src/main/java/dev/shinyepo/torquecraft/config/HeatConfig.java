package dev.shinyepo.torquecraft.config;

public enum HeatConfig {
    ALLOY_FURNACE(2100);

    private final double maxTemp;

    HeatConfig(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }
}
