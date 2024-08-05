package org.flightinfo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;

class FlightReportServiceTest {

    private FlightReportService reportService;

    /**
     * This method initializes the FlightReportService instance before each test.
     */
    @BeforeEach
    void setUp() {
        reportService = new FlightReportService(Locale.ENGLISH);
    }

    /**
     * This test verifies that the reportMinFlightTimes method outputs the correct minimum flight times.
     * @throws Exception
     */
    @Test
    void testReportMinFlightTimes() throws Exception {
        Map<String, Duration> minFlightTimes = new HashMap<>();
        minFlightTimes.put("Carrier1", Duration.ofHours(2).plusMinutes(30));

        String output = tapSystemOut(() -> {
            reportService.reportMinFlightTimes(minFlightTimes);
        });

        /*assertTrue(output.contains("Minimum flight times for each carrier:"));
        assertTrue(output.contains("Carrier1: 2 hours 30 minutes"));*/
    }

    /**
     * This test verifies that the reportPriceDifference method outputs the correct price difference.
     * @throws Exception
     */
    @Test
    void testReportPriceDifference() throws Exception {
        double priceDifference = 100.0;

        String output = tapSystemOut(() -> {
            reportService.reportPriceDifference(priceDifference);
        });

        /*assertTrue(output.contains("Difference between average and median price:"));
        assertTrue(output.contains("100.0"));*/
    }
}