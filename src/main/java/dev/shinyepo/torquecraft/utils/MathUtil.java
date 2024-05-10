package dev.shinyepo.torquecraft.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {
    public static float roundFloat(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.DOWN);
        return bd.floatValue();
    }
}
