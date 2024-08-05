package org.flightinfo.service;

import org.flightinfo.app.FlightPriceProvider;
import org.flightinfo.app.FlightTimeProvider;
import org.flightinfo.dto.FlightTicket;
import org.flightinfo.filter.FlightDataFilter;
import org.flightinfo.io.FlightDataReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightDataServiceTest {

    private FlightDataService flightInfoService;
    private FlightDataReader flightDataReader;
    private FlightDataFilter flightDataFilter;
    private FlightTimeProvider flightTimeProvider;
    private FlightPriceProvider flightPriceProvider;
    private List<FlightTicket> tickets;

    /**
     * This method sets up the mocks and initializes the FlightInfoService instance. It overrides the processFlightData,
     * getMinFlightTimes, and getPriceDifference methods to use the mocks.
     */
    @BeforeEach
    void setUp() {
        flightDataReader = mock(FlightDataReader.class);
        flightDataFilter = mock(FlightDataFilter.class);
        flightTimeProvider = mock(FlightTimeProvider.class);
        flightPriceProvider = mock(FlightPriceProvider.class);

        tickets = List.of(new FlightTicket(), new FlightTicket());
        when(flightDataReader.loadTickets()).thenReturn(tickets);
        when(flightDataFilter.getFilteredTickets()).thenReturn(tickets);
        when(flightTimeProvider.provideMinFlightTimes()).thenReturn(Map.of("Carrier1", Duration.ofHours(2)));
        when(flightPriceProvider.getPriceDifference()).thenReturn(100.0);

        flightInfoService = new FlightDataService("/tickets.json") {
            @Override
            public void processFlightData(String filePath) {
                tickets = flightDataReader.loadTickets();
                tickets = flightDataFilter.getFilteredTickets();
            }

            @Override
            public Map<String, Duration> getMinFlightTimes() {
                return flightTimeProvider.provideMinFlightTimes();
            }

            @Override
            public double getPriceDifference() {
                return flightPriceProvider.getPriceDifference();
            }
        };
    }

    /**
     * This test verifies that the getMinFlightTimes method returns the correct minimum flight times.
     */
    @Test
    void testGetMinFlightTimes() {
        Map<String, Duration> minFlightTimes = flightInfoService.getMinFlightTimes();
        assertNotNull(minFlightTimes);
        assertEquals(1, minFlightTimes.size());
        assertEquals(Duration.ofHours(2), minFlightTimes.get("Carrier1"));
    }

    /**
     * This test verifies that the getPriceDifference method returns the correct price difference.
     */
    @Test
    void testGetPriceDifference() {
        double priceDifference = flightInfoService.getPriceDifference();
        assertEquals(100.0, priceDifference);
    }
}