package dev.shinyepo.torquecraft.config.side;

public enum SideType {
    NONE (0),
    INPUT (2),
    OUTPUT (1);

    private final int opposite;

    SideType(int opposite) {
        this.opposite = opposite;
    }

    public int getOpposite() {
        return opposite;
    }
}
