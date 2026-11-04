package com.forcast;

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

    private String getCondition() {
        if (weatherCode == null) {
            return "Unavailable";
        }

        switch (weatherCode) {
            case 0:
                return "Clear sky";
            case 1:
                return "Mainly clear";
            case 2:
                return "Partly cloudy";
            case 3:
                return "Overcast";
            case 45:
            case 48:
                return "Fog";
            case 51:
            case 53:
            case 55:
                return "Drizzle";
            case 56:
            case 57:
                return "Freezing drizzle";
            case 61:
                return "Light rain";
            case 63:
                return "Moderate rain";
            case 65:
                return "Heavy rain";
            case 66:
            case 67:
                return "Freezing rain";
            case 71:
                return "Light snow";
            case 73:
                return "Moderate snow";
            case 75:
                return "Heavy snow";
            case 77:
                return "Snow grains";
            case 80:
            case 81:
            case 82:
                return "Rain showers";
            case 85:
            case 86:
                return "Snow showers";
            case 95:
                return "Thunderstorm";
            case 96:
                return "Thunderstorm with slight hail";
            case 97:
                return "Heavy thunderstorm";
            case 99:
                return "Thunderstorm with heavy hail";
            default:
                return "Unknown";
        }
    }
}
