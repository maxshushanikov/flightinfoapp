package org.flightinfo.app;

import org.flightinfo.domain.dto.FlightTicket;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides methods to calculate flight times.
 */
public class FlightTimeProvider {

    private final List<FlightTicket> tickets;

    /**
     * Constructor to initialize the {@link FlightTimeProvider} with a list of flight tickets.
     *
     * @param tickets The list of flight tickets.
     */
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
     * Provides a map contains carrier and minimum flight time without time zone
     * @return The map with the carrier as the key and the minimum flight time as the value.
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
     * @param ticket The {@link FlightTicket} containing departure and arrival information.
     * @return The flight time as a {@link Duration} object, not null
     */
    public Duration getFlightTime(FlightTicket ticket) {

        String departureTimeFormated = formatTime(ticket.getDepartureTime());
        String arrivalTimeFormated = formatTime(ticket.getArrivalTime());

        ZonedDateTime departureTime = getZonedDateTime(ticket.getDepartureDate(), departureTimeFormated, "Asia/Vladivostok");
        ZonedDateTime arrivalTime = getZonedDateTime(ticket.getArrivalDate(), arrivalTimeFormated, "Asia/Jerusalem");

        return Duration.between(departureTime, arrivalTime);
    }

    /**
     * Creates a {@link ZonedDateTime} object from the given date, time, and time zone ID.
     *
     * @param date The date in the format "dd.MM.yy".
     * @param time The time in the format "HH:mm".
     * @param zoneId The time zone ID.
     * @return The {@link ZonedDateTime} object.
     */
    public ZonedDateTime getZonedDateTime(String date, String time, String zoneId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return ZonedDateTime.of(
                LocalDateTime.parse(date + " " + time, formatter),
                ZoneId.of(zoneId));
    }

    /**
     * Appends a leading zero to the time string, if it is a single digit.
     * @param time The flight time string.
     * @return The formatted time as string
     */
    private String formatTime(String time) {
        String[] parts = time.split(":");
        if (parts[0].length() == 1) {
            parts[0] = "0" + parts[0];
        }
        return parts[0] + ":" + parts[1];
    }


    /**
     * Calculates the flight time in minutes without time zones. The result of the method
     * is always a positive flight time, even if the flight takes several days.
     * @param ticket {@link FlightTicket} object with departure and arrival information
     * @return The flight time in minutes.
     */
    private int getFlightTimeWithoutTimeZone(FlightTicket ticket) {
        String[] depParts = ticket.getDepartureTime().split(":");
        String[] arrParts = ticket.getArrivalTime().split(":");
        int depMinutes = Integer.parseInt(depParts[0]) * 60 + Integer.parseInt(depParts[1]);
        int arrMinutes = Integer.parseInt(arrParts[0]) * 60 + Integer.parseInt(arrParts[1]);

        // Calculates the difference in days between departure and arrival
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        LocalDate depDate = LocalDate.parse(ticket.getDepartureDate(), dateFormatter);
        LocalDate arrDate = LocalDate.parse(ticket.getArrivalDate(), dateFormatter);
        long daysDifference = Duration.between(depDate.atStartOfDay(), arrDate.atStartOfDay()).toDays();

        // Adjust the arrival minutes by adding the difference in days (in minutes)
        arrMinutes += daysDifference * 24 * 60;

        return arrMinutes - depMinutes;
    }

}
