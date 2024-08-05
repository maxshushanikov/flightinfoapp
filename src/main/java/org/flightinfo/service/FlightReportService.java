package org.flightinfo.service;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class displays the minimum flight time and price differences in console
 */
public class FlightReportService {

    private ResourceBundle messages;
    private static final Logger logger = Logger.getLogger(FlightReportService.class.getName());

    public FlightReportService(Locale locale) {
        this.messages = ResourceBundle.getBundle("messages", locale);
    }

    /**
     * Outputs the minimum flight time for each carrier to the console
     */
    public void reportMinFlightTimes(Map<String, Duration> minFlightTimes) {
        System.out.println(messages.getString("minFlightTimes"));
        minFlightTimes.forEach(
            (carrier, time) ->
                System.out.println(" - " + carrier + ": " + time.toHours() + " " + messages.getString("hours") + " " +
                            String.format("%02d", time.toMinutesPart()) + " " + messages.getString("minutes")));
    }

    /**
     * Outputs the minimum flight time without time zone for each carrier to the console
     */
    public void reportMinFlightTimesWithoutTimeZone(Map<String, Integer> minFlightTimes) {
        System.out.println(messages.getString("minFlightTimes"));
        minFlightTimes.forEach(
                (carrier, time) ->
                        System.out.println(" - " + carrier + ": " + formatTime(time)));
    }

    /**
     * Outputs the difference between average and median price to the console
     */
    public void reportPriceDifference(double priceDifference) {
        System.out.println(messages.getString("priceDifference"));
        System.out.println(" - " + priceDifference);
    }

    private String formatTime(int time){
        Duration dur = Duration.ofMinutes(time);
        return String.format(Locale.forLanguageTag("ru-RU"), "%d %s %02d %s", dur.toHours(),
                messages.getString("hours"), dur.toMinutesPart(), messages.getString("minutes"));
    }
}
