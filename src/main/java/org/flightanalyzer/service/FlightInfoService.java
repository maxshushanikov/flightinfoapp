package org.flightanalyzer.service;

import org.flightanalyzer.app.FlightPriceProvider;
import org.flightanalyzer.app.FlightTimeProvider;
import org.flightanalyzer.domain.dto.FlightTicket;
import org.flightanalyzer.filter.FlightDataFilter;
import org.flightanalyzer.io.FlightDataReader;
import org.flightanalyzer.io.JsonFlightDataReader;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * This class provides flight information
 */
public class FlightInfoService {

    private List<FlightTicket> tickets;
    private final AirportTimeZoneService timeZoneService;

    /**
     * Constructor to initialize the {@link FlightInfoService} with a configuration service.
     * It processes the flight data based on the provided configuration.
     *
     * @param cfgService The configuration service.
     */
    public FlightInfoService(ConfigurationService cfgService, AirportTimeZoneService timeZoneService) {
        this.timeZoneService = timeZoneService;
        processFlightData(cfgService);
    }

    /**
     * Returns the minimum flight time for each carrier.
     *
     * @return map of minimum flight times for each carrier
     */
    public Map<String, Duration> getMinFlightTimes() {
        FlightTimeProvider provider = new FlightTimeProvider(this.tickets, this.timeZoneService);
        return provider.provideMinFlightTimes();
    }

    /**
     * Returns the minimum flight times for each carrier, without considering time zones.
     *
     * @return A map with the carrier as the key and the minimum flight time as the value.
     */
    public Map<String, Integer> getMinFlightTimesWithoutTimeZone() {
        FlightTimeProvider provider = new FlightTimeProvider(this.tickets, this.timeZoneService);
        return provider.provideMinFlightTimesWithoutTimeZone();
    }

    /**
     * Returns the difference between the average and median price.
     *
     * @return difference between average and median price
     */
    public double getPriceDifference() {
        FlightPriceProvider provider = new FlightPriceProvider(tickets);
        return provider.getPriceDifference();
    }

    /**
     * Processes the flight data by loading it from a file and filtering it based on specific criteria.
     *
     * @param cfgService The configuration service containing the file path of the flight data.
     */
    public void processFlightData(ConfigurationService cfgService){
        String filePath = cfgService.getFlightFilePath();
        JsonFlightDataReader reader = new JsonFlightDataReader(filePath);
        this.tickets = reader.loadTickets();

        FlightDataFilter filter = new FlightDataFilter(this.tickets);
        this.tickets = filter.getFilteredTickets();
    }

    /**
     * Returns statistics for the loaded data
     */
    public String getStatistics() {
        return String.format("Tickets: %d, Airports: %d",
                tickets.size(),
                timeZoneService.getLoadedAirportCount());
    }
}
