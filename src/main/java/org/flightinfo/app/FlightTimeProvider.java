package org.flightinfo.app;

import org.flightinfo.dto.FlightTicket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides flight time
 */
public class FlightTimeProvider {

    private final List<FlightTicket> tickets;

    public FlightTimeProvider(List<FlightTicket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Provides map contains carrier and minimum flight time with time zone
     * @return map with carrier and minimum flight time
     */
    public Map<String, Duration> provideMinFlightTimes() {
        Map<String, Duration> minFlightTimes = new HashMap<>();

        for (FlightTicket ticket : this.tickets) {
            Duration flightTime = getFlightTime(ticket);
            minFlightTimes.put(ticket.getCarrier(), flightTime);
        }
        return minFlightTimes;
    }

    /**
     * Provides map contains carrier and minimum flight time without time zone
     * @return map with carrier and minimum flight time
     */
    public Map<String, Integer> provideMinFlightTimesWithoutTimeZone() {
        Map<String, Integer> minFlightTimes = new HashMap<>();

        for (FlightTicket ticket : this.tickets) {
            int flightTime = getFlightTimeWithoutTimeZone(ticket);
            minFlightTimes.put(ticket.getCarrier(), Math.min(minFlightTimes.getOrDefault(ticket.getCarrier(), Integer.MAX_VALUE), flightTime));
        }
        return minFlightTimes;
    }

    /**
     * Calculates the duration between departure and arrival time
     * @param ticket flight data
     * @return flight time duration, not null
     */
    public Duration getFlightTime(FlightTicket ticket) {

        String departureTimeStr = formatTime(ticket.getDepartureTime());
        String arrivalTimeStr = formatTime(ticket.getArrivalTime());

        ZonedDateTime departureTime = getZonedDateTime(ticket.getDepartureDate(), departureTimeStr, "Asia/Vladivostok");
        ZonedDateTime arrivalTime = getZonedDateTime(ticket.getArrivalDate(), arrivalTimeStr, "Asia/Jerusalem");

        return Duration.between(departureTime, arrivalTime);
    }

    /**
     * Creates a zoned date-time matching the input local date-time
     * @param date flight date
     * @param time flight time
     * @param zoneId the time-zone id, not null
     * @return the zoned date-time, not null
     */
    public ZonedDateTime getZonedDateTime(String date, String time, String zoneId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return ZonedDateTime.of(
                LocalDateTime.parse(date + " " + time, formatter),
                ZoneId.of(zoneId));
    }

    /**
     * Appends a leading zero to the time string, if it is a single digit
     * @param time flight time
     * @return formatted time as string
     */
    private String formatTime(String time) {
        String[] parts = time.split(":");
        if (parts[0].length() == 1) {
            parts[0] = "0" + parts[0];
        }
        return parts[0] + ":" + parts[1];
    }

    /**
     *
     * @param ticket
     * @return
     */
    private int getFlightTimeWithoutTimeZone(FlightTicket ticket) {
        String[] depParts = ticket.getDepartureTime().split(":");
        String[] arrParts = ticket.getArrivalTime().split(":");
        int depMinutes = Integer.parseInt(depParts[0]) * 60 + Integer.parseInt(depParts[1]);
        int arrMinutes = Integer.parseInt(arrParts[0]) * 60 + Integer.parseInt(arrParts[1]);
        return arrMinutes - depMinutes;
    }
}
