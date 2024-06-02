package dev.shinyepo.torquecraft.config;

public enum GearboxRatio {
    RATIO_2(2),
    RATIO_4(4),
    RATIO_8(8),
    RATIO_16(16);

    private final int ratio;

    GearboxRatio(int ratio) {
        this.ratio = ratio;
    }

    public int getRatio() {
        return ratio;
    }
}
