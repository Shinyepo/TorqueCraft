package dev.shinyepo.torquecraft.config;

public enum TransmitterConfig implements IRotaryConfig {
    SHAFT (2048F, 2048F),
    THREE_WAY(2048F, 2048F),
    BEVEL_GEARS(2048F, 2048F);

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
