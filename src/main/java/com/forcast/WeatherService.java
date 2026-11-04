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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Requires Java 11+ and Gson. Weather data: https://open-meteo.com/ */
public class WeatherService {
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();

    /**
     * Returns hourly forecasts for the date at the event location.
     * Units: Fahrenheit, percent, inches and miles per hour.
     * Use a JavaFX background Task: this method performs network requests.
     * Missing measurements remain null. Repeated local hours are possible
     * when daylight saving time ends. Forecasts extend up to 16 days.
     * Thunderstorm probability is optional NBM data, with limited coverage
     * and forecast horizon. It may differ from the main forecast weather code.
     * Lightning-strike data is not supplied by this integration.
     */
    public static ArrayList<Forecast> getForecast(
            LocalDate date, double latitude, double longitude
    ) throws IOException, InterruptedException {
        Objects.requireNonNull(date, "An event date is required.");
        if (!Double.isFinite(latitude) || latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Invalid latitude.");
        }
        if (!Double.isFinite(longitude) || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Invalid longitude.");
        }

        String locationAndDate = "?latitude=" + latitude
                + "&longitude=" + longitude
                + "&start_date=" + date + "&end_date=" + date
                + "&timezone=auto&timeformat=unixtime";

        String url = "https://api.open-meteo.com/v1/forecast"
                + locationAndDate
                + "&hourly=temperature_2m,precipitation_probability,wind_speed_10m,"
                + "precipitation,uv_index,wind_gusts_10m,relative_humidity_2m,"
                + "apparent_temperature,weather_code"
                + "&temperature_unit=fahrenheit&wind_speed_unit=mph"
                + "&precipitation_unit=inch";

        JsonObject root = requestJson(url);
        try {
            ZoneId zone = ZoneId.of(root.get("timezone").getAsString());
            JsonObject hourly = root.getAsJsonObject("hourly");
            if (hourly == null) {
                throw new IOException("No hourly forecast was returned.");
            }
            JsonArray times = hourly.getAsJsonArray("time");
            if (times == null) {
                throw new IOException("No forecast times were returned.");
            }

            // NBM supplies a separate storm probability for supported US areas.
            // Match by timestamp, not index, because coverage may differ.
            Map<Long, Double> stormChances = getStormChances(locationAndDate);
            ArrayList<Forecast> forecasts = new ArrayList<>();
            for (int i = 0; i < times.size(); i++) {
                long timestamp = times.get(i).getAsLong();
                ZonedDateTime time = Instant.ofEpochSecond(timestamp).atZone(zone);
                if (time.toLocalDate().equals(date)) {
                    Double code = getNumber(hourly.getAsJsonArray("weather_code"), i);
                    forecasts.add(new Forecast(
                            time.getHour(),
                            getNumber(hourly.getAsJsonArray("temperature_2m"), i),
                            getNumber(hourly.getAsJsonArray("precipitation_probability"), i),
                            getNumber(hourly.getAsJsonArray("wind_speed_10m"), i),
                            getNumber(hourly.getAsJsonArray("precipitation"), i),
                            getNumber(hourly.getAsJsonArray("uv_index"), i),
                            getNumber(hourly.getAsJsonArray("wind_gusts_10m"), i),
                            getNumber(hourly.getAsJsonArray("relative_humidity_2m"), i),
                            getNumber(hourly.getAsJsonArray("apparent_temperature"), i),
                            stormChances.get(timestamp),
                            code == null ? null : code.intValue()));
                }
            }
            return forecasts;
        } catch (RuntimeException exception) {
            throw new IOException("Could not read the weather response.", exception);
        }
    }

    private static JsonObject requestJson(String url)
            throws IOException, InterruptedException {
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
            return JsonParser.parseString(response.body()).getAsJsonObject();
        } catch (RuntimeException exception) {
            throw new IOException("Could not read the weather response.", exception);
        }
    }

    /** An unavailable optional forecast must not discard the basic forecast. */
    private static Map<Long, Double> getStormChances(String locationAndDate)
            throws InterruptedException {
        Map<Long, Double> chances = new HashMap<>();
        String url = "https://api.open-meteo.com/v1/forecast" + locationAndDate
                + "&hourly=thunderstorm_probability&models=ncep_nbm_conus";
        try {
            JsonObject hourly = requestJson(url).getAsJsonObject("hourly");
            if (hourly == null) { return chances; }
            JsonArray times = hourly.getAsJsonArray("time");
            JsonArray values = hourly.getAsJsonArray("thunderstorm_probability");
            if (times == null) { return chances; }
            for (int i = 0; i < times.size(); i++) {
                Double chance = getNumber(values, i);
                if (chance != null) {
                    chances.put(times.get(i).getAsLong(), chance);
                }
            }
        } catch (IOException | RuntimeException exception) {
            // No coverage, unavailable model, or failed optional request.
            // Return no probabilities so the display says Unavailable, not 0%.
            chances.clear();
        }
        return chances;
    }

    private static Double getNumber(JsonArray values, int index) {
        if (values == null || index >= values.size()) { return null; }
        JsonElement value = values.get(index);
        return value.isJsonNull() ? null : value.getAsDouble();
    }
}
