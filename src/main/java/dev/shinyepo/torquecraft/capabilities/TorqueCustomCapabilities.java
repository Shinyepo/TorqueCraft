package dev.shinyepo.torquecraft.capabilities;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.capabilities.handlers.rotary.IRotaryHandler;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

public class TorqueCustomCapabilities {

    public static final BlockCapability<IRotaryHandler, @Nullable Direction> ROTARY_HANDLER_BLOCK = BlockCapability.createSided(new ResourceLocation(TorqueCraft.MODID, "rotary_handler"), IRotaryHandler.class);
}
