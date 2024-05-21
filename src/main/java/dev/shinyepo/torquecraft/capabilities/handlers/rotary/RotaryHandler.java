package dev.shinyepo.torquecraft.capabilities.handlers.rotary;

import dev.shinyepo.torquecraft.utils.MathUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class RotaryHandler implements IRotaryHandler {
    protected float TORQUE = 0;
    protected float MAX_TORQUE;
    protected float ANGULAR = 0;
    protected float MAX_ANGULAR;
    protected float POWER = 0;
    private float ANGULAR_ACC;
    private float TORQUE_ACC;

    public RotaryHandler(float maxAngular, float maxTorque) {
        MAX_ANGULAR = maxAngular;
        MAX_TORQUE = maxTorque;
    }

    public void setAcceleration(int speedupTime) {
        ANGULAR_ACC = MAX_ANGULAR / speedupTime / 20;
        TORQUE_ACC = MAX_TORQUE / speedupTime / 20;
    }

    public void setMaxTorque(float torque) {
        this.MAX_TORQUE = torque;
        validateValues();
    }

    public void setMaxAngular(float angular) {
        this.MAX_ANGULAR = angular;
        validateValues();
    }

    public void calculatePower() {
        POWER = ANGULAR * TORQUE;
        validateValues();
    }

    public void setTorque(float torque) {
        TORQUE = torque;
    }

    public void setAngular(float angular) {
        ANGULAR = angular;
    }

    private void validateValues() {
        if (POWER < 0f) {
            POWER = 0f;
        }
        if (ANGULAR < 0f) {
            ANGULAR = 0f;
        }
        if (TORQUE < 0f) {
            TORQUE = 0f;
        }

        if (ANGULAR > MAX_ANGULAR) {
            ANGULAR = MAX_ANGULAR;
        }
        if (TORQUE > MAX_TORQUE) {
            TORQUE = MAX_TORQUE;
        }
    }


    public void markDirty() {
        this.calculatePower();
    }

    @Override
    public float getPower() {
        return MathUtil.roundFloat(this.POWER, 2);
    }

    @Override
    public float getAngular() {
        return MathUtil.roundFloat(this.ANGULAR, 2);
    }

    @Override
    public float getTorque() {
        return MathUtil.roundFloat(this.TORQUE, 2);
    }

    public void slowDownAngular() {
        if (ANGULAR  <= 0) return;
        ANGULAR = (float) (ANGULAR - ANGULAR_ACC * 1.3);
    }

    public void slowDownTorque() {
        if (TORQUE <= 0) return;
        TORQUE = (float) (TORQUE - TORQUE_ACC * 1.3);
    }

    public void speedupAngular() {
        if (ANGULAR >= MAX_ANGULAR) return;
        ANGULAR = ANGULAR + ANGULAR_ACC;
    }

    public void speedupTorque() {
        if (TORQUE >= MAX_TORQUE) return;
        TORQUE = TORQUE + TORQUE_ACC;
    }

    public void spinupSource() {
        this.speedupAngular();
        this.speedupTorque();
        this.markDirty();
    }

    public void slowDownSource() {
        this.slowDownAngular();
        this.slowDownTorque();
        this.markDirty();
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        setAngular(nbt.contains("Angular") ? nbt.getFloat("Angular") : 0);
        setTorque(nbt.contains("Torque") ? nbt.getFloat("Torque") : 0);
    }

    public Tag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("Angular", getAngular());
        nbt.putFloat("Torque", getTorque());
        return nbt;
    }
}
