package dev.shinyepo.torquecraft.config;

public enum MaterialConfig {
    IRON(512, 32);

    private final float angular;
    private final float torque;

    MaterialConfig(float angular, float torque) {
        this.angular = angular;
        this.torque = torque;
    }

    public float getTorque() {
        return torque;
    }

    public float getAngular() {
        return angular;
    }
}
