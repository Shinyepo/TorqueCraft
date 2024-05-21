package dev.shinyepo.torquecraft.config;

public enum ClientConfig implements IRotaryConfig {
    GRINDER(2048, 2048);

    private final float torque;
    private final float angular;

    ClientConfig(float angular, float torque) {
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
