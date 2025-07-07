package org.flightinfo;

import org.flightinfo.service.ConfigurationService;
import org.flightinfo.service.FlightInfoService;
import org.flightinfo.service.FlightReportService;

import java.util.Map;

/**
 * This is the main application class that orchestrates the flight information and reporting services.
 */
public class App {

    private final FlightInfoService infoService;
    private final FlightReportService reportService;

    /**
     * Constructor to initialize the App with flight information and report services.
     *
     * @param infoService The flight information service.
     * @param reportService The flight report service.
     */
    public App(FlightInfoService infoService, FlightReportService reportService) {
        this.infoService = infoService;
        this.reportService = reportService;
    }

    /**
     * Runs the application by retrieving flight information and generating reports.
     */
    public void run() {

        // Retrieve minimum flight times, without time zones
        Map<String, Integer> minFlightTimes = infoService.getMinFlightTimesWithoutTimeZone();

        // Calculate the price difference between average and median prices
        double priceDifference = infoService.getPriceDifference();

        // Generate reports for minimum flight times and price difference
        reportService.reportMinFlightTimesWithoutTimeZone(minFlightTimes);
        reportService.reportPriceDifference(priceDifference);
    }

    /**
     * The main method to start the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        try {
            // Initialize the configuration service with command-line arguments
            ConfigurationService cfgService = new ConfigurationService(args);

            // Initialize the flight information and report services
            FlightInfoService infoService = new FlightInfoService(cfgService);
            FlightReportService reportService = new FlightReportService(cfgService);

            // Create and run the application
            App app = new App(infoService, reportService);
            app.run();
        }catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            System.exit(1);
        }
    }
}