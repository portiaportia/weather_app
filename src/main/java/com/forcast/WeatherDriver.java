package com.forcast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

public class WeatherDriver {
        private static double LATITUDE = 34.0007;
        private static double LONGITUDE = -81.0348;

        public static LocalDate stringToDate(String dateString) {
                DateTimeFormatter formatter = DateTimeFormatter
                        .ofPattern("MM/dd/uuuu")
                        .withResolverStyle(ResolverStyle.STRICT);

                return LocalDate.parse(dateString.trim(), formatter);
        }

        public void showForcast(LocalDate date) throws Exception {
                ArrayList<Forecast> forecasts = WeatherService.getForecast(date, LATITUDE, LONGITUDE);

                System.out.println("Forecast for " + date);
                System.out.println("-------------------------");

                if (forecasts.isEmpty()) {
                        System.out.println("No forecast available for this date.");
                        return;
                }

                for (Forecast forecast : forecasts) {
                        System.out.println(forecast);
                }
        }

    public static void main(String[] args) throws Exception {
        LocalDate eventDate = stringToDate("11/05/2026");
        WeatherDriver driver = new WeatherDriver();
        driver.showForcast(eventDate);
    }
}