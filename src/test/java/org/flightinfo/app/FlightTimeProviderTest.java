package org.flightinfo.app;

import org.flightinfo.dto.FlightTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FlightTimeProviderTest {

    private List<FlightTicket> tickets;
    private FlightTimeProvider flightTimeProvider;

    /**
     * This method initializes the FlightTimeProvider instance and sets up some sample tickets before each test.
     */
    @BeforeEach
    void setUp() {
        FlightTicket ticket1 = new FlightTicket();
        ticket1.setCarrier("Carrier1");
        ticket1.setDepartureDate("01.01.23");
        ticket1.setDepartureTime("10:00");
        ticket1.setArrivalDate("01.01.23");
        ticket1.setArrivalTime("14:00");

        FlightTicket ticket2 = new FlightTicket();
        ticket2.setCarrier("Carrier2");
        ticket2.setDepartureDate("01.01.23");
        ticket2.setDepartureTime("11:00");
        ticket2.setArrivalDate("01.01.23");
        ticket2.setArrivalTime("15:00");

        tickets = List.of(ticket1, ticket2);
        flightTimeProvider = new FlightTimeProvider(tickets);
    }

    /**
     * This test verifies that the provideMinFlightTimes method returns the correct minimum flight times
     * for each carrier.
     */
    @Test
    void testProvideMinFlightTimes() {
        Map<String, Duration> minFlightTimes = flightTimeProvider.provideMinFlightTimes();
        assertNotNull(minFlightTimes);
        assertEquals(2, minFlightTimes.size());
    }

    /**
     * This test verifies that the getFlightTime method returns the correct flight time duration for a given ticket.
     */
    @Test
    void testGetFlightTime() {
        FlightTicket ticket = tickets.get(0);
        Duration flightTime = flightTimeProvider.getFlightTime(ticket);
        assertNotNull(flightTime);
    }

    /**
     * This test verifies that the getZonedDateTime method returns the correct ZonedDateTime object for
     * a given date, time, and time zone.
     */
    @Test
    void testGetZonedDateTime() {
        String date = "01.01.23";
        String time = "10:00";
        String zoneId = "Asia/Vladivostok";
        ZonedDateTime zonedDateTime = flightTimeProvider.getZonedDateTime(date, time, zoneId);
        assertNotNull(zonedDateTime);
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getMonthValue());
        assertEquals(1, zonedDateTime.getDayOfMonth());
        assertEquals(10, zonedDateTime.getHour());
        assertEquals(0, zonedDateTime.getMinute());
        assertEquals(zoneId, zonedDateTime.getZone().getId());
    }

    /**
     * This test verifies that the formatTime method correctly formats a time string by appending
     * a leading zero if necessary.
     * @throws Exception
     */
    @Test
    void testFormatTime() throws Exception {
        Method method = FlightTimeProvider.class.getDeclaredMethod("formatTime", String.class);
        method.setAccessible(true);
        String formattedTime = (String) method.invoke(flightTimeProvider, "9:30");
        assertEquals("09:30", formattedTime);
    }
}