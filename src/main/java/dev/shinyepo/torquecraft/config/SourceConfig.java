package dev.shinyepo.torquecraft.config;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;

public enum SourceConfig implements IRotaryConfig {
    STEAM_ENGINE (512,16, 15, 64000, Tags.Fluids.WATER, 15),
    GASOLINE_ENGINE (2048,256, 8, 16000, Tags.Fluids.WATER, 15),
    DIESEL_ENGINE (2048,512, 8, 16000, Tags.Fluids.WATER, 15),
    MICRO_TURBINE (262144,64, 60, 64000, Tags.Fluids.WATER, 15),
    TURBINE (524288,512, 60, 64000, Tags.Fluids.WATER, 15);

    private final float torque;
    private final float angular;
    private final int windup;
    private final int capacity;
    private final TagKey<Fluid> fuel;
    private final int usage;

    SourceConfig(float angular, float torque, int windup, int capacity, TagKey<Fluid> fuel, int usage) {
        this.torque = torque;
        this.angular = angular;
        this.windup = windup;
        this.capacity = capacity;
        this.fuel = fuel;
        this.usage = usage;
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

    public int getCapacity() {
        return capacity;
    }

    public TagKey<Fluid> getFuel() {
        return fuel;
    }

    public int getUsage() {
        return usage;
    }
}
