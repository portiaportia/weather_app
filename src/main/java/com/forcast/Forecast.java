package com.forcast;

import java.util.HashMap;

public class Forecast {
    private int hour;
    private Double temperatureF;
    private Double precipitationChancePercent;
    private Double windSpeedMph;
    private Double precipitationInches;
    private Double uvIndex;
    private Double windGustMph;
    private Double humidityPercent;
    private Double feelsLikeF;
    private Double thunderstormChancePercent;
    private Integer weatherCode;

    public Forecast(int hour, Double temperatureF,
                    Double precipitationChancePercent, Double windSpeedMph,
                    Double precipitationInches, Double uvIndex,
                    Double windGustMph, Double humidityPercent,
                    Double feelsLikeF, Double thunderstormChancePercent,
                    Integer weatherCode) {
        this.hour = hour;
        this.temperatureF = temperatureF;
        this.precipitationChancePercent = precipitationChancePercent;
        this.windSpeedMph = windSpeedMph;
        this.precipitationInches = precipitationInches;
        this.uvIndex = uvIndex;
        this.windGustMph = windGustMph;
        this.humidityPercent = humidityPercent;
        this.feelsLikeF = feelsLikeF;
        this.thunderstormChancePercent = thunderstormChancePercent;
        this.weatherCode = weatherCode;
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

    public Double getPrecipitationInches() { 
        return precipitationInches; 
    }

    public Double getUvIndex() { 
        return uvIndex; 
    }

    public Double getWindGustMph() { 
        return windGustMph; 
    }

    public Double getHumidityPercent() { 
        return humidityPercent; 
    }

    public Double getFeelsLikeF() { 
        return feelsLikeF; 
    }

    public Double getThunderstormChancePercent() {
        return thunderstormChancePercent;
    }

    @Override
    public String toString() {
        String result = String.format("Hour: %02d:00%n", hour);
        result += "Temperature: " + temperatureF + " F\n";
        result += "Feels like: " + feelsLikeF + " F\n";
        result += "Humidity: " + humidityPercent + "%\n";
        result += "Precipitation chance: " + precipitationChancePercent + "%\n";
        result += "Precipitation (preceding hour): "+ precipitationInches + " inches\n";
        result += "Wind speed: " + windSpeedMph + " mph\n";
        result += "Wind gusts (preceding hour max): " + windGustMph + " mph\n";
        result += "UV index: " + uvIndex + "\n";
        result += "Thunderstorm chance (NBM): " + thunderstormChancePercent + "%\n";
        result += "Condition: " + getCondition() + "\n";

        return result;
    }

    private Condition getCondition() {
        HashMap<Integer, Condition> conditionsMap = new HashMap<>();

        conditionsMap.put(0, Condition.CLEAR_SKY);
        conditionsMap.put(1, Condition.MAINLY_CLEAR);
        conditionsMap.put(2, Condition.PARTLY_CLOUDLY);
        conditionsMap.put(3, Condition.OVERCAST);

        conditionsMap.put(45, Condition.FOG);
        conditionsMap.put(48, Condition.FOG);

        conditionsMap.put(51, Condition.DRIZZLE);
        conditionsMap.put(53, Condition.DRIZZLE);
        conditionsMap.put(55, Condition.DRIZZLE);

        conditionsMap.put(56, Condition.FREEZING_DRIZZLE);
        conditionsMap.put(57, Condition.FREEZING_DRIZZLE);

        conditionsMap.put(61, Condition.LIGHT_RAIN);
        conditionsMap.put(63, Condition.MODERATE_RAIN);
        conditionsMap.put(65, Condition.HEAVY_RAIN);

        conditionsMap.put(66, Condition.FREEZING_RAIN);
        conditionsMap.put(67, Condition.FREEZING_RAIN);

        conditionsMap.put(71, Condition.LIGHT_SNOW);
        conditionsMap.put(73, Condition.MODERATE_SNOW);
        conditionsMap.put(75, Condition.HEAVY_SNOW);
        conditionsMap.put(77, Condition.SNOW_GRAINS);

        conditionsMap.put(80, Condition.RAIN_SHOWERS);
        conditionsMap.put(81, Condition.RAIN_SHOWERS);
        conditionsMap.put(82, Condition.RAIN_SHOWERS);

        conditionsMap.put(85, Condition.SNOW_SHOWERS);
        conditionsMap.put(86, Condition.SNOW_SHOWERS);

        conditionsMap.put(95, Condition.THUNDERSTORM);
        conditionsMap.put(96, Condition.THUNDERSTOM_HAIL);
        conditionsMap.put(97, Condition.HEAVY_THUNDERSTORM);
        conditionsMap.put(99, Condition.THUNDERSTORM_HEAVY_HAIL);

        return conditionsMap.getOrDefault(weatherCode, Condition.UNKNOWN);
    }
}
