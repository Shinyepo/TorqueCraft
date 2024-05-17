package dev.shinyepo.torquecraft.config;

public enum SourceConfig {
    STEAM_ENGINE (512,512);

    private final float torque;
    private final float angular;

    SourceConfig(float torque, float angular) {
        this.torque = torque;
        this.angular = angular;
    }

    public float getAngular() {
        return angular;
    }

    public float getTorque() {
        return torque;
    }
}
