package dev.shinyepo.torquecraft.config;

public enum SourceConfig implements IRotaryConfig {
    STEAM_ENGINE (512,50, 10);

    private final float torque;
    private final float angular;
    private final int windup;

    SourceConfig(float angular, float torque, int windup) {
        this.torque = torque;
        this.angular = angular;
        this.windup = windup;
    }

    public float getAngular() {
        return angular;
    }

    public float getTorque() {
        return torque;
    }

    public int getWindup() {
        return windup;
    }
}
