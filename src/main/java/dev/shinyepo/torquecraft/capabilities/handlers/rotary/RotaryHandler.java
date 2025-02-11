package dev.shinyepo.torquecraft.capabilities.handlers.rotary;

import dev.shinyepo.torquecraft.constants.TorqueNBT;
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

    protected double TEMP = 0;

    public RotaryHandler(float maxAngular, float maxTorque) {
        MAX_ANGULAR = maxAngular;
        MAX_TORQUE = maxTorque;
    }

    public void setTemp(double temp) {
        this.TEMP = temp;
        this.markDirty();
    }

    public double getTemp() {
        return this.TEMP;
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

    public float getMaxTorque() {
        return MAX_TORQUE;
    }

    public float getMaxAngular() {
        return MAX_ANGULAR;
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
        setAngular(nbt.contains(TorqueNBT.ANGULAR) ? nbt.getFloat(TorqueNBT.ANGULAR) : 0);
        setTorque(nbt.contains(TorqueNBT.TORQUE) ? nbt.getFloat(TorqueNBT.TORQUE) : 0);
        setTemp(nbt.contains(TorqueNBT.TEMP) ? nbt.getFloat(TorqueNBT.TEMP) : 0);
    }

    public Tag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat(TorqueNBT.ANGULAR, getAngular());
        nbt.putFloat(TorqueNBT.TORQUE, getTorque());
        nbt.putDouble(TorqueNBT.TEMP, (double) Math.round(getTemp() * 100) / 100);
        return nbt;
    }
}
