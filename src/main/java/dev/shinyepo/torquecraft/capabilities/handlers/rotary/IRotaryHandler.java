package dev.shinyepo.torquecraft.capabilities.handlers.rotary;

public interface IRotaryHandler {
    float getPower();

    float getAngular();

    float getTorque();

    void setAngular(float angular);

    void setTorque(float torque);
}
