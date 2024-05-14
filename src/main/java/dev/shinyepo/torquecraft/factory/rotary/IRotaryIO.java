package dev.shinyepo.torquecraft.factory.rotary;

public interface IRotaryIO {

    default float getProgress(float pPartialTicks) { return 0F; }

    default void updateAnimation() { }

    default void renderTick() {
        updateAnimation();
    }

    default void setProgress(float dur) { }

}
