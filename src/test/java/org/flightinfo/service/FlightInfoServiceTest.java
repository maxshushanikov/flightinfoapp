package org.flightinfo.service;

import org.flightinfo.dto.FlightTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.flightinfo.service.ConfigurationService;
import org.flightinfo.service.FlightInfoService;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightInfoServiceTest  {

    private ConfigurationService cfgService;
    private FlightInfoService flightInfoService;

    @BeforeEach
    public void setUp() {
        cfgService = mock(ConfigurationService.class);
        when(cfgService.getFlightFilePath()).thenReturn("/tickets.json");
        flightInfoService = new FlightInfoService(cfgService);
    }

    @Test
    public void testGetMinFlightTimes() {
        Map<String, Duration> minFlightTimes = flightInfoService.getMinFlightTimes();
        assertNotNull(minFlightTimes);
        // Add more assertions based on expected values
    }

    @Test
    public void testGetMinFlightTimesWithoutTimeZone() {
        Map<String, Integer> minFlightTimesWithoutTimeZone = flightInfoService.getMinFlightTimesWithoutTimeZone();
        assertNotNull(minFlightTimesWithoutTimeZone);
        // Add more assertions based on expected values
    }

    @Test
    public void testGetPriceDifference() {
        double priceDifference = flightInfoService.getPriceDifference();
        assertTrue(priceDifference >= 0);
        // Add more assertions based on expected values
    }

    /*@Test
    public void testProcessFlightData() {
        flightInfoService.processFlightData(cfgService);
        List<FlightTicket> tickets = flightInfoService.testGetTicketsUsingReflection();
        assertNotNull(tickets);
        // Add more assertions based on expected values
    }*/
}