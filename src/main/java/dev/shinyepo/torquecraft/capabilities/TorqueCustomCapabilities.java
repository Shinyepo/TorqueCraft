package dev.shinyepo.torquecraft.capabilities;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.capabilities.handlers.rotary.IRotaryHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class TorqueCustomCapabilities {

    public static final BlockCapability<IRotaryHandler, @Nullable Direction> ROTARY_HANDLER_BLOCK = BlockCapability.createSided(fromNamespaceAndPath(TorqueCraft.MODID, "rotary_handler"), IRotaryHandler.class);
}
