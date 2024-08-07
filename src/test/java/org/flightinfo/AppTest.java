package org.flightinfo;

import org.flightinfo.service.ConfigurationService;
import org.flightinfo.service.FlightInfoService;
import org.flightinfo.service.FlightReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AppTest {

    private FlightInfoService mockInfoService;
    private FlightReportService mockReportService;
    private App app;

    @BeforeEach
    public void setUp() {
        mockInfoService = mock(FlightInfoService.class);
        mockReportService = mock(FlightReportService.class);
        app = new App(mockInfoService, mockReportService);
    }

    @Test
    public void testRun() {
        // Mock the data
        Map<String, Integer> mockMinFlightTimes = Map.of("TK", 350, "SU", 400);
        double mockPriceDifference = 7250.0;

        // Set up the mock behavior
        when(mockInfoService.getMinFlightTimesWithoutTimeZone()).thenReturn(mockMinFlightTimes);
        when(mockInfoService.getPriceDifference()).thenReturn(mockPriceDifference);

        // Call the method to test
        app.run();

        // Verify the interactions and capture the arguments
        ArgumentCaptor<Map<String, Integer>> minFlightTimesCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Double> priceDifferenceCaptor = ArgumentCaptor.forClass(Double.class);

        verify(mockInfoService).getMinFlightTimesWithoutTimeZone();
        verify(mockInfoService).getPriceDifference();
        verify(mockReportService).reportMinFlightTimesWithoutTimeZone(minFlightTimesCaptor.capture());
        verify(mockReportService).reportPriceDifference(priceDifferenceCaptor.capture());

        // Assert the captured arguments
        assertEquals(mockMinFlightTimes, minFlightTimesCaptor.getValue());
        assertEquals(mockPriceDifference, priceDifferenceCaptor.getValue());
    }

    @Test
    public void testMain() {
        // Mock the configuration service
        ConfigurationService mockConfigService = mock(ConfigurationService.class);
        when(mockConfigService.getFlightFilePath()).thenReturn("/tickets.json");
        when(mockConfigService.getFlightLocale()).thenReturn(Locale.ENGLISH);

        // Mock the flight information and report services
        FlightInfoService mockInfoService = mock(FlightInfoService.class);
        FlightReportService mockReportService = mock(FlightReportService.class);

        // Create the app instance
        App app = new App(mockInfoService, mockReportService);

        // Call the main method
        //App.main(new String[]{});
    }
}