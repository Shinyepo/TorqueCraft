package dev.shinyepo.torquecraft.factory.rotary.render;

public interface IRotaryIO {

    default float getProgress() {
        return 0F;
    }

    default void updateAnimation() { }

    default void renderTick() {
        updateAnimation();
    }

    default void setProgress(float dur) { }

}
