package org.flightinfo.service;

import java.time.Duration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This class provides methods to generate reports for flight information,
 * including minimum flight times and price differences.
 */
public class FlightReportService {

    private final ResourceBundle messages;
    private final Locale locale;

    /**
     * Constructor to initialize the {@link FlightReportService} with a configuration service.
     * It sets the locale and loads the corresponding resource bundle for messages.
     *
     * @param cfgService The configuration service.
     */
    public FlightReportService(ConfigurationService cfgService) {
        this.locale = cfgService.getFlightLocale();
        this.messages = ResourceBundle.getBundle("messages", this.locale);
    }

    /**
     * Reports the minimum flight times for each carrier, with time zones.
     *
     * @param minFlightTimes The map with the carrier as the key and the minimum flight time as the value.
     */
    public void reportMinFlightTimes(Map<String, Duration> minFlightTimes) {
        System.out.println(this.messages.getString("minFlightTimes"));
        minFlightTimes.forEach(
            (carrier, time) ->
                System.out.println(" - " + carrier + ": " + time.toHours() + " " + this.messages.getString("hours")
                + " " + String.format("%02d", time.toMinutesPart()) + " " + this.messages.getString("minutes")));
    }

    /**
     * Reports the minimum flight times for each carrier, without time zones.
     *
     * @param minFlightTimes The map with the carrier as the key and the minimum flight time as the value.
     */
    public void reportMinFlightTimesWithoutTimeZone(Map<String, Integer> minFlightTimes) {
        System.out.println(this.messages.getString("minFlightTimes"));
        minFlightTimes.forEach(
                (carrier, time) ->
                        System.out.println(" - " + carrier + ": " + getFormatedTime(time)));
    }

    /**
     * Reports the absolute difference between the average price and the median price of the flight tickets.
     *
     * @param priceDifference The absolute difference between the average price and the median price.
     */
    public void reportPriceDifference(double priceDifference) {
        System.out.println(this.messages.getString("priceDifference"));
        System.out.println(" - " + getFormatedPrice(priceDifference));
    }

    /**
     * Formats the flight time in hours and minutes.
     *
     * @param time The flight time in minutes.
     * @return The formatted flight time as a string.
     */
    private String getFormatedTime(int time){
        Duration duration = Duration.ofMinutes(time);
        return String.format(this.locale, "%d %s %02d %s", duration.toHours(),
                this.messages.getString("hours"), duration.toMinutesPart(), this.messages.getString("minutes"));
    }

    /**
     * Formats the price difference into a string with major and minor currency units.
     *
     * @param priceDifference The price difference.
     * @return The formatted price difference as a string.
     */
    private String getFormatedPrice(double priceDifference){
        int majorPart = (int) priceDifference;
        int minorPart = (int) Math.round((priceDifference - majorPart) * 100);
        return String.format("%d %s %02d %s", majorPart, this.messages.getString("currencymajor"),
                minorPart, this.messages.getString("currencyminor"));
    }
}
