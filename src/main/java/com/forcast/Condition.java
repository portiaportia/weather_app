package com.forcast;

public enum Condition {
    CLEAR_SKY("Clear sky"),
    MAINLY_CLEAR("Mainly clear"),
    PARTLY_CLOUDLY("Partly cloudy"),
    OVERCAST("Overcast"),
    FOG("Fog"),
    DRIZZLE("Drizzle"),
    FREEZING_DRIZZLE("Freezing drizzle"),
    LIGHT_RAIN("Light rain"),
    MODERATE_RAIN("Moderate rain"),
    HEAVY_RAIN("Heavy rain"),
    FREEZING_RAIN("Freezing rain"),
    LIGHT_SNOW("Light snow"),
    MODERATE_SNOW("Moderate snow"),
    HEAVY_SNOW("Heavy snow"),
    SNOW_GRAINS("Snow grains"),
    RAIN_SHOWERS("Rain showers"),
    SNOW_SHOWERS("Snow showers"),
    THUNDERSTORM("Thunderstorm"),
    THUNDERSTOM_HAIL("Thunderstorm with slight hail"),
    HEAVY_THUNDERSTORM("Heavy thunderstorm"),
    THUNDERSTORM_HEAVY_HAIL("Thunderstorm with heavy hail"),
    UNKNOWN("Unknown");

    private final String label;

    Condition(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}