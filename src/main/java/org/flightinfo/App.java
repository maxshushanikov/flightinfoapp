package org.flightinfo;

import org.flightinfo.service.ConfigService;
import org.flightinfo.service.FlightInfoService;
import org.flightinfo.service.FlightReportService;

import java.util.Map;

public class App {

    private final FlightInfoService infoService;
    private final FlightReportService reportService;

    public App(FlightInfoService infoService, FlightReportService reportService) {
        this.infoService = infoService;
        this.reportService = reportService;
    }

    public void run() {

        Map<String, Integer> minFlightTimes = infoService.getMinFlightTimesWithoutTimeZone();
        double priceDifference = infoService.getPriceDifference();

        reportService.reportMinFlightTimesWithoutTimeZone(minFlightTimes);
        reportService.reportPriceDifference(priceDifference);
    }

    public static void main(String[] args) {

        ConfigService cfgService = new ConfigService(args);

        FlightInfoService infoService = new FlightInfoService(cfgService);
        FlightReportService reportService = new FlightReportService(cfgService);

        App app = new App(infoService, reportService);
        app.run();
    }
}