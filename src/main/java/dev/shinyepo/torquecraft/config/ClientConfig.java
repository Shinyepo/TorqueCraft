package dev.shinyepo.torquecraft.config;

public enum ClientConfig implements IRotaryConfig {
    GRINDER(2048, 2048,512,32),
    MECHANICAL_FAN(1024, 2048, 128, 64),
    VACUUM(4096, 2048, 128, 8),
    SPRINKLER(32, 32, 32, 32),
    CENTRIFUGE(262144, 32, 16384, 32);

    private final float minTorque;
    private final float minAngular;
    private final float torque;
    private final float angular;


    ClientConfig(float angular, float torque, float minAngular, float minTorque) {
        this.angular = angular;
        this.torque = torque;
        this.minAngular = minAngular;
        this.minTorque = minTorque;
    }

    public float getMinAngular() {
        return minAngular;
    }

    public float getMinTorque() {
        return minTorque;
    }

    @Override
    public float getAngular() {
        return angular;
    }

    @Override
    public float getTorque() {
        return torque;
    }
}
