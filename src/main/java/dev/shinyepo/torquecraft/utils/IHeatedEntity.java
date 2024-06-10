package dev.shinyepo.torquecraft.utils;

public interface IHeatedEntity {
    double getTemp();

    void setTemp(double temp);

    default double getCoef(double ambient) {
        return 0;
    }

    ;
}
