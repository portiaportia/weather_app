package com.forcast;

public class Forecast {

    private int hour;
    private Double temperatureF;
    private Double precipitationChancePercent;
    private Double windSpeedMph;

    public Forecast(int hour, Double temperatureF,
                    Double precipitationChancePercent,
                    Double windSpeedMph) {
        this.hour = hour;
        this.temperatureF = temperatureF;
        this.precipitationChancePercent = precipitationChancePercent;
        this.windSpeedMph = windSpeedMph;
    }

    public int getHour() {
        return hour;
    }

    public Double getTemperatureF() {
        return temperatureF;
    }

    public Double getPrecipitationChancePercent() {
        return precipitationChancePercent;
    }

    public Double getWindSpeedMph() {
        return windSpeedMph;
    }

    public String toString(){
        String result = "";
        result += String.format("Hour: %02d:00%n", getHour());
        result += "Temperature: " + getTemperatureF() + "F\n";
        result += "Precipitation chance: " + getPrecipitationChancePercent() + "%\n";
        result += "Wind speed: " + getWindSpeedMph() + " mph\n";

        return result;
    }
}