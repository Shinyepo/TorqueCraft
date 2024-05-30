package dev.shinyepo.torquecraft.factory;

import net.minecraft.core.Direction;

public interface IWrenchInteraction {
    void configureSides(Direction newFace);

    void resetSides();
}
