package com.example.rchhatre29.weatherclothes;

/**
 * Created by rchhatre29 on 6/9/16.
 */
public class DataWeather {

    private int minTemp;
    private int maxTemp;
    private boolean rain;
    private String male;
    private String female;

    /**
     * Creates a new Data weather with a min/max temp, rain, and a male and female recommendation
     * @param minTemp the minimum temperature
     * @param maxTemp the maximum temperature
     * @param rain whether it is raining or not
     * @param male boy recommendation
     * @param female girl recommendation
     */
    public DataWeather(int minTemp, int maxTemp, boolean rain, String male, String female) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.rain = rain;
        this.male = male;
        this.female = female;
    }

    /**
     * Returns the minimum temperature
     * @return the minimum temperature
     */
    public int getMinTemp() {
        return minTemp;
    }

    /**
     * Returns the maximum temperature
     * @return the maximum temperature
     */
    public int getMaxTemp() {
        return maxTemp;
    }

    /**
     * Returns whether there is rain or not
     * @return whether there is rain or not
     */
    public boolean isRain() {
        return rain;
    }

    /**
     * Returns the male recommendation
     * @return the male recommendation
     */
    public String getMale() {
        if(rain) {
            return "" + male + "\nCarry an Umbrella";
        } else {
            return "" + male;
        }
    }

    /**
     * Returns the female recommendation
     * @return the female recommendation
     */
    public String getFemale() {
        if(rain) {
            return "" + female + "\nCarry an Umbrella";
        } else {
            return "" + female;
        }
    }
}
