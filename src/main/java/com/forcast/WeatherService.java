package com.forcast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Objects;

/** Requires Java 11+ and Gson. Weather data: https://open-meteo.com/ */
public class WeatherService {
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();

    /**
     * Returns hourly forecasts for the date at the event location.
     * Measurements use Fahrenheit, percent (0-100), and miles per hour.
     * Dates must be within the API forecast window (up to 16 days).
     * Call from a background Task when using JavaFX.
     * A repeated hour can occur when daylight saving time ends.
     */
    public static ArrayList<Forecast> getForecast(LocalDate date, double latitude, double longitude) throws IOException, InterruptedException {
        Objects.requireNonNull(date, "An event date is required.");
        if (!Double.isFinite(latitude) || latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Invalid latitude.");
        }
        
        if (!Double.isFinite(longitude) || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Invalid longitude.");
        }

        String url = "https://api.open-meteo.com/v1/forecast"
                + "?latitude=" + latitude + "&longitude=" + longitude
                + "&start_date=" + date + "&end_date=" + date
                + "&hourly=temperature_2m,precipitation_probability,wind_speed_10m"
                + "&temperature_unit=fahrenheit&wind_speed_unit=mph"
                + "&timezone=auto&timeformat=unixtime";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)).timeout(Duration.ofSeconds(20))
                .GET().build();
        HttpResponse<String> response = CLIENT.send(
                request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Forecast unavailable. HTTP "
                    + response.statusCode() + ": " + response.body());
        }

        try {
            JsonObject root = JsonParser.parseString(response.body())
                    .getAsJsonObject();
            ZoneId zone = ZoneId.of(root.get("timezone").getAsString());
            JsonObject hourly = root.getAsJsonObject("hourly");

            if (hourly == null) {
                throw new IOException("No hourly forecast was returned.");
            }

            JsonArray times = hourly.getAsJsonArray("time");

            if (times == null) {
                throw new IOException("No forecast times were returned.");
            }
            JsonArray temperatures = hourly.getAsJsonArray("temperature_2m");
            JsonArray precipitation = hourly.getAsJsonArray(
                    "precipitation_probability");
            JsonArray wind = hourly.getAsJsonArray("wind_speed_10m");

            ArrayList<Forecast> forecasts = new ArrayList<>();
            
            for (int i = 0; i < times.size(); i++) {
                ZonedDateTime time = Instant.ofEpochSecond(
                        times.get(i).getAsLong()).atZone(zone);
                if (time.toLocalDate().equals(date)) {
                    forecasts.add(new Forecast(time.getHour(),
                            getNumber(temperatures, i),
                            getNumber(precipitation, i), getNumber(wind, i)));
                }
            }
            return forecasts;
        } catch (RuntimeException exception) {
            throw new IOException("Could not read the weather response.", exception);
        }
    }

    private static Double getNumber(JsonArray values, int index) {
        if (values == null || index >= values.size()) {
            return null;
        }
        JsonElement value = values.get(index);
        return value.isJsonNull() ? null : value.getAsDouble();
    }
}
