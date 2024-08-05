package org.flightinfo;

import org.flightinfo.service.FlightDataService;
import org.flightinfo.service.FlightReportService;

import java.time.Duration;
import java.util.Locale;
import java.util.Map;

public class App {
    private final FlightDataService infoService;
    private final FlightReportService reportService;

    public App(FlightDataService infoService, FlightReportService reportService) {
        this.infoService = infoService;
        this.reportService = reportService;
    }

    public void run(String[] args) {

        Map<String, Duration> minFlightTimes = infoService.getMinFlightTimes();
        double priceDifference = infoService.getPriceDifference();

        reportService.reportMinFlightTimes(minFlightTimes);
        reportService.reportPriceDifference(priceDifference);
    }

    public static void main(String[] args) {
        String flightFile = "/tickets.json";
        FlightDataService infoService = new FlightDataService(flightFile);
        FlightReportService reportService = new FlightReportService(Locale.of("ru", "RU"));
        App app = new App(infoService, reportService);
        app.run(args);
    }
}