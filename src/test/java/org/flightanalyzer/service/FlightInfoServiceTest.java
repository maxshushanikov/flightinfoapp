package org.flightanalyzer.service;

import org.flightanalyzer.app.FlightPriceProvider;
import org.flightanalyzer.app.FlightTimeProvider;
import org.flightanalyzer.domain.dto.FlightTicket;
import org.flightanalyzer.filter.FlightDataFilter;
import org.flightanalyzer.io.JsonFlightDataReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightInfoServiceTest {

    private ConfigurationService cfgService;
    private AirportTimeZoneService timeZoneService;
    private FlightInfoService flightInfoService;

    private List<FlightTicket> mockTickets;

    @BeforeEach
    void setUp() {
        cfgService = mock(ConfigurationService.class);
        timeZoneService = mock(AirportTimeZoneService.class);

        when(cfgService.getFlightFilePath()).thenReturn("/tickets.json");

        // Create mock tickets
        mockTickets = createMockTickets();

        // Create service instance
        flightInfoService = new FlightInfoService(cfgService, timeZoneService);
    }

    @Test
    void testGetMinFlightTimes() {
        // Mock the dependencies
        try (MockedConstruction<JsonFlightDataReader> readerMock = mockConstruction(JsonFlightDataReader.class,
                (mock, context) -> when(mock.loadTickets()).thenReturn(mockTickets));
             MockedConstruction<FlightDataFilter> filterMock = mockConstruction(FlightDataFilter.class,
                     (mock, context) -> when(mock.getFilteredTickets()).thenReturn(mockTickets));
             MockedConstruction<FlightTimeProvider> providerMock = mockConstruction(FlightTimeProvider.class,
                     (mock, context) -> {
                         Map<String, Duration> expectedResult = Map.of(
                                 "TK", Duration.ofHours(5).plusMinutes(30),
                                 "SU", Duration.ofHours(6).plusMinutes(15)
                         );
                         when(mock.provideMinFlightTimes()).thenReturn(expectedResult);
                     })) {

            // Execute
            Map<String, Duration> result = flightInfoService.getMinFlightTimes();

            // Verify
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.containsKey("TK"));
            assertTrue(result.containsKey("SU"));
            assertEquals(Duration.ofHours(5).plusMinutes(30), result.get("TK"));

            // Verify interactions
            verify(cfgService, atLeastOnce()).getFlightFilePath();
        }
    }

    @Test
    void testGetMinFlightTimesWithoutTimeZone() {
        // Mock the dependencies
        try (MockedConstruction<JsonFlightDataReader> readerMock = mockConstruction(JsonFlightDataReader.class,
                (mock, context) -> when(mock.loadTickets()).thenReturn(mockTickets));
             MockedConstruction<FlightDataFilter> filterMock = mockConstruction(FlightDataFilter.class,
                     (mock, context) -> when(mock.getFilteredTickets()).thenReturn(mockTickets));
             MockedConstruction<FlightTimeProvider> providerMock = mockConstruction(FlightTimeProvider.class,
                     (mock, context) -> {
                         Map<String, Integer> expectedResult = Map.of(
                                 "TK", 330,  // 5 hours 30 minutes in minutes
                                 "SU", 375   // 6 hours 15 minutes in minutes
                         );
                         when(mock.provideMinFlightTimesWithoutTimeZone()).thenReturn(expectedResult);
                     })) {

            // Execute
            Map<String, Integer> result = flightInfoService.getMinFlightTimesWithoutTimeZone();

            // Verify
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.containsKey("TK"));
            assertTrue(result.containsKey("SU"));
            assertEquals(330, result.get("TK"));
            assertEquals(375, result.get("SU"));
        }
    }

    @Test
    void testGetPriceDifference() {
        // Mock the dependencies
        try (MockedConstruction<JsonFlightDataReader> readerMock = mockConstruction(JsonFlightDataReader.class,
                (mock, context) -> when(mock.loadTickets()).thenReturn(mockTickets));
             MockedConstruction<FlightDataFilter> filterMock = mockConstruction(FlightDataFilter.class,
                     (mock, context) -> when(mock.getFilteredTickets()).thenReturn(mockTickets));
             MockedConstruction<FlightPriceProvider> providerMock = mockConstruction(FlightPriceProvider.class,
                     (mock, context) -> when(mock.getPriceDifference()).thenReturn(1250.0))) {

            // Execute
            double result = flightInfoService.getPriceDifference();

            // Verify
            assertEquals(1250.0, result, 0.001);
        }
    }

    @Test
    void testProcessFlightData() {
        // Mock the dependencies
        try (MockedConstruction<JsonFlightDataReader> readerMock = mockConstruction(JsonFlightDataReader.class,
                (mock, context) -> when(mock.loadTickets()).thenReturn(mockTickets));
             MockedConstruction<FlightDataFilter> filterMock = mockConstruction(FlightDataFilter.class,
                     (mock, context) -> when(mock.getFilteredTickets()).thenReturn(mockTickets))) {

            // Execute
            flightInfoService.processFlightData(cfgService);

            // Verify that the reader was created with correct file path
            List<JsonFlightDataReader> constructedReaders = readerMock.constructed();
            assertEquals(1, constructedReaders.size());

            List<FlightDataFilter> constructedFilters = filterMock.constructed();
            assertEquals(1, constructedFilters.size());

            // Verify configuration service was used
            verify(cfgService, atLeastOnce()).getFlightFilePath();
        }
    }

    @Test
    void testGetStatistics() {
        // Mock the dependencies
        try (MockedConstruction<JsonFlightDataReader> readerMock = mockConstruction(JsonFlightDataReader.class,
                (mock, context) -> when(mock.loadTickets()).thenReturn(mockTickets));
             MockedConstruction<FlightDataFilter> filterMock = mockConstruction(FlightDataFilter.class,
                     (mock, context) -> when(mock.getFilteredTickets()).thenReturn(mockTickets))) {

            when(timeZoneService.getLoadedAirportCount()).thenReturn(50);

            // Execute
            String statistics = flightInfoService.getStatistics();

            // Verify
            assertNotNull(statistics);
            assertTrue(statistics.contains("Tickets: 3"));
            assertTrue(statistics.contains("Airports: 50"));
        }
    }

    @Test
    void testProcessFlightData_EmptyTickets() {
        // Mock empty ticket list
        try (MockedConstruction<JsonFlightDataReader> readerMock = mockConstruction(JsonFlightDataReader.class,
                (mock, context) -> when(mock.loadTickets()).thenReturn(List.of()));
             MockedConstruction<FlightDataFilter> filterMock = mockConstruction(FlightDataFilter.class,
                     (mock, context) -> when(mock.getFilteredTickets()).thenReturn(List.of()))) {

            // Execute
            flightInfoService.processFlightData(cfgService);

            // Verify no exceptions thrown with empty data
            assertDoesNotThrow(() -> flightInfoService.getPriceDifference());
        }
    }

    @Test
    void testConstructor_Initialization() {
        // Verify that service is properly initialized
        assertNotNull(flightInfoService);

        // The actual processing happens in constructor, so we need to mock it
        try (MockedConstruction<JsonFlightDataReader> ignoredReader = mockConstruction(JsonFlightDataReader.class,
                (mock, context) -> when(mock.loadTickets()).thenReturn(mockTickets));
             MockedConstruction<FlightDataFilter> ignoredFilter = mockConstruction(FlightDataFilter.class,
                     (mock, context) -> when(mock.getFilteredTickets()).thenReturn(mockTickets))) {

            // Create new instance to trigger constructor
            FlightInfoService service = new FlightInfoService(cfgService, timeZoneService);
            assertNotNull(service);
        }
    }

    /**
     * Creates mock flight tickets for testing
     */
    private List<FlightTicket> createMockTickets() {
        FlightTicket ticket1 = new FlightTicket();
        ticket1.setOrigin("VVO");
        ticket1.setDestination("TLV");
        ticket1.setCarrier("TK");
        ticket1.setDepartureDate("12.05.18");
        ticket1.setDepartureTime("10:00");
        ticket1.setArrivalDate("12.05.18");
        ticket1.setArrivalTime("15:30");
        ticket1.setPrice(12000);

        FlightTicket ticket2 = new FlightTicket();
        ticket2.setOrigin("VVO");
        ticket2.setDestination("TLV");
        ticket2.setCarrier("SU");
        ticket2.setDepartureDate("12.05.18");
        ticket2.setDepartureTime("11:00");
        ticket2.setArrivalDate("12.05.18");
        ticket2.setArrivalTime("17:15");
        ticket2.setPrice(15000);

        FlightTicket ticket3 = new FlightTicket();
        ticket3.setOrigin("VVO");
        ticket3.setDestination("TLV");
        ticket3.setCarrier("TK");
        ticket3.setDepartureDate("12.05.18");
        ticket3.setDepartureTime("14:00");
        ticket3.setArrivalDate("12.05.18");
        ticket3.setArrivalTime("20:45");
        ticket3.setPrice(11000);

        return Arrays.asList(ticket1, ticket2, ticket3);
    }
}