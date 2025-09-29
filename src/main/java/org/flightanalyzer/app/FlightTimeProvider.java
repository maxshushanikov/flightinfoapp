package org.flightanalyzer.app;

import org.flightanalyzer.domain.dto.FlightTicket;
import org.flightanalyzer.service.AirportTimeZoneService;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Provider for calculating flight times across time zones
 */
public class FlightTimeProvider {

    private static final Logger logger = Logger.getLogger(FlightTimeProvider.class.getName());

    private final List<FlightTicket> tickets;
    private final AirportTimeZoneService timeZoneService;
    private final ZoneId defaultDepartureZone;
    private final ZoneId defaultArrivalZone;

    // Cache formatters for performance
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public FlightTimeProvider(List<FlightTicket> tickets, AirportTimeZoneService timeZoneService) {
        this(tickets, timeZoneService,
                ZoneId.of("Asia/Vladivostok"),
                ZoneId.of("Asia/Jerusalem"));
    }

    public FlightTimeProvider(List<FlightTicket> tickets,
                              AirportTimeZoneService timeZoneService,
                              ZoneId defaultDepartureZone,
                              ZoneId defaultArrivalZone) {
        this.tickets = tickets;
        this.timeZoneService = timeZoneService;
        this.defaultDepartureZone = defaultDepartureZone;
        this.defaultArrivalZone = defaultArrivalZone;
    }

    /**
     * Provides a map with minimum flight times for each carrier
     */
    public Map<String, Duration> provideMinFlightTimes() {
        Map<String, Duration> minFlightTimes = new HashMap<>();

        for (FlightTicket ticket : this.tickets) {
            Duration flightTime = getFlightTime(ticket);
            String carrier = ticket.getCarrier();

            minFlightTimes.merge(carrier, flightTime,
                    (current, newValue) -> current.compareTo(newValue) < 0 ? current : newValue);
        }

        return minFlightTimes;
    }

    /**
     * Provides a map with minimum flight times, ignoring time zones
     */
    public Map<String, Integer> provideMinFlightTimesWithoutTimeZone() {
        Map<String, Integer> minFlightTimes = new HashMap<>();

        for (FlightTicket ticket : this.tickets) {
            int flightTime = getFlightTimeWithoutTimeZone(ticket);
            String carrier = ticket.getCarrier();

            minFlightTimes.merge(carrier, flightTime,
                    Math::min);
        }

        return minFlightTimes;
    }

    /**
     * Calculates flight duration taking into account time zones
     */
    public Duration getFlightTime(FlightTicket ticket) {
        ZoneId departureZone = getDepartureZoneId(ticket);
        ZoneId arrivalZone = getArrivalZoneId(ticket);

        ZonedDateTime departureTime = getZonedDateTime(
                ticket.getDepartureDate(),
                formatTime(ticket.getDepartureTime()),
                departureZone
        );

        ZonedDateTime arrivalTime = getZonedDateTime(
                ticket.getArrivalDate(),
                formatTime(ticket.getArrivalTime()),
                arrivalZone
        );

        return Duration.between(departureTime, arrivalTime);
    }

    private ZoneId getDepartureZoneId(FlightTicket ticket) {
        return timeZoneService.getTimeZone(ticket.getOrigin())
                .orElseGet(() -> {
                    logger.warning("Unknown departure airport: " + ticket.getOrigin() + ", using default: " + defaultDepartureZone);
                    return defaultDepartureZone;
                });
    }

    private ZoneId getArrivalZoneId(FlightTicket ticket) {
        return timeZoneService.getTimeZone(ticket.getDestination())
                .orElseGet(() -> {
                    logger.warning("Unknown arrival airport: " + ticket.getDestination() + ", using default: " + defaultArrivalZone);
                    return defaultArrivalZone;
                });
    }

    /**
     * Creates a ZonedDateTime from string representations of a date and time
     */
    public ZonedDateTime getZonedDateTime(String date, String time, ZoneId zoneId) {
        return ZonedDateTime.of(
                LocalDateTime.parse(date + " " + time, DATE_TIME_FORMATTER),
                zoneId
        );
    }

    /**
     * Formats the time, adding leading zeros if necessary
     */
    private String formatTime(String time) {
        try {
            // Let's try standard parsing first
            LocalTime localTime = LocalTime.parse(time, TIME_FORMATTER);
            return localTime.format(TIME_FORMATTER);
        } catch (DateTimeException e) {
            // Fallback for invalid formats
            String[] parts = time.split(":");
            if (parts.length >= 2) {
                String hours = parts[0].length() == 1 ? "0" + parts[0] : parts[0];
                return hours + ":" + parts[1];
            }
            throw new IllegalArgumentException("Invalid time format: " + time, e);
        }
    }

    /**
     * Calculates flight time in minutes, ignoring time zones
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

        // Adjusts the arrival time taking into account the difference in days
        arrMinutes += daysDifference * 24 * 60;

        return arrMinutes - depMinutes;
    }
}