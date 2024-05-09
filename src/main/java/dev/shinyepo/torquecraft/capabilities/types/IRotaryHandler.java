package dev.shinyepo.torquecraft.capabilities.types;

import net.minecraft.network.chat.Component;

public interface IRotaryHandler {
    float getPower();

    float getAngular();

    float getTorque();

    void setAngular(float angular);

    void setTorque(float torque);
}
