package dev.shinyepo.torquecraft.config;

public enum TransmitterConfig implements IRotaryConfig {
    SHAFT (2048F, 2048F),
    JUNCTION (0,0),
    BEVEL(0,0);

    private final float torque;
    private final float angular;

    TransmitterConfig(float angular, float torque) {
        this.angular = angular;
        this.torque = torque;
    }

    public float getAngular() {
        return angular;
    }

    public float getTorque() {
        return torque;
    }
}
