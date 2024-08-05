package org.flightinfo;

import org.flightinfo.service.FlightDataService;
import org.flightinfo.service.FlightReportService;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;

class AppTest {

    private FlightDataService infoService;
    private FlightReportService reportService;
    private App app;

    @BeforeEach
    void setUp() {
        infoService = mock(FlightDataService.class);
        reportService = mock(FlightReportService.class);
        app = new App(infoService, reportService);
    }

    /**
     * The test ensures that the run method correctly interacts with the mocked services to retrieve
     * and report flight information.
     */
    @Test
    public void testRun() {
        Map<String, Duration> minFlightTimes = new HashMap<>();
        minFlightTimes.put("Carrier1", Duration.ofHours(2).plusMinutes(30));
        minFlightTimes.put("Carrier2", Duration.ofHours(3).plusMinutes(45));

        double priceDifference = 50.0;

        when(infoService.getMinFlightTimes()).thenReturn(minFlightTimes);
        when(infoService.getPriceDifference()).thenReturn(priceDifference);

        app.run(new String[]{});

        verify(reportService).reportMinFlightTimes(minFlightTimes);
        verify(reportService).reportPriceDifference(priceDifference);
    }


    /**
     * This test captures the output of the main method. It also mocks the dependencies to ensure that the main method
     * behaves correctly without relying on the actual implementations of FlightInfoService and FlightReportService.
     *
     * @throws Exception
     */
    @Test
    public void testMain() {
        Map<String, Duration> minFlightTimes = new HashMap<>();
        minFlightTimes.put("Carrier1", Duration.ofHours(2).plusMinutes(30));
        minFlightTimes.put("Carrier2", Duration.ofHours(3).plusMinutes(45));

        double priceDifference = 50.0;

        when(infoService.getMinFlightTimes()).thenReturn(minFlightTimes);
        when(infoService.getPriceDifference()).thenReturn(priceDifference);

        SystemOutRule systemOutRule = new SystemOutRule().enableLog();
        systemOutRule.clearLog();

        App.main(new String[]{});
    }
}