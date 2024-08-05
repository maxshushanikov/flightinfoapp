package org.flightinfo.service;

import java.time.Duration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This class represents the minimum flight time and price differences in console
 */
public class FlightReportService {

    private ResourceBundle messages;
    private Locale locale;

    public FlightReportService(ConfigService cfgService) {
        this.locale = cfgService.getFlightLocal();
        this.messages = ResourceBundle.getBundle("messages", this.locale);
    }

    /**
     * Outputs the minimum flight time for each carrier to the console
     */
    public void reportMinFlightTimes(Map<String, Duration> minFlightTimes) {
        System.out.println(this.messages.getString("minFlightTimes"));
        minFlightTimes.forEach(
            (carrier, time) ->
                System.out.println(" - " + carrier + ": " + time.toHours() + " " + this.messages.getString("hours")
                + " " + String.format("%02d", time.toMinutesPart()) + " " + this.messages.getString("minutes")));
    }

    /**
     * Outputs the minimum flight time without time zone for each carrier to the console
     */
    public void reportMinFlightTimesWithoutTimeZone(Map<String, Integer> minFlightTimes) {
        System.out.println(this.messages.getString("minFlightTimes"));
        minFlightTimes.forEach(
                (carrier, time) ->
                        System.out.println(" - " + carrier + ": " + getFormatedTime(time)));
    }

    /**
     * Outputs the difference between average and median price to the console
     */
    public void reportPriceDifference(double priceDifference) {
        System.out.println(this.messages.getString("priceDifference"));
        System.out.println(" - " + getFormatedPrice(priceDifference));
    }

    /**
     * Extracts and formats the number of minutes to represents in console
     * @param time number of minutes
     * @return formatted string of
     */
    private String getFormatedTime(int time){
        Duration duration = Duration.ofMinutes(time);
        return String.format(this.locale, "%d %s %02d %s", duration.toHours(),
                this.messages.getString("hours"), duration.toMinutesPart(), this.messages.getString("minutes"));
    }

    private String getFormatedPrice(double priceDifference){
        int majorPart = (int) priceDifference;
        int minorPart = (int) Math.round((priceDifference - majorPart) * 100);
        return String.format("%d %s %02d %s", majorPart, this.messages.getString("currencymajor"),
                minorPart, this.messages.getString("currencyminor"));
    }
}
