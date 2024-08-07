package org.flightinfo.service;

import org.flightinfo.app.FlightPriceProvider;
import org.flightinfo.app.FlightTimeProvider;
import org.flightinfo.dto.FlightTicket;
import org.flightinfo.filter.FlightDataFilter;
import org.flightinfo.io.FlightDataReader;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * This class provides flight information
 */
public class FlightInfoService {

    private List<FlightTicket> tickets;

    /**
     * Constructor to initialize the {@link FlightInfoService} with a configuration service.
     * It processes the flight data based on the provided configuration.
     *
     * @param cfgService The configuration service.
     */
    public FlightInfoService(ConfigurationService cfgService) {
        processFlightData(cfgService);
    }

    /**
     * Returns the minimum flight time for each carrier.
     *
     * @return map of minimum flight times for each carrier
     */
    public Map<String, Duration> getMinFlightTimes() {
        FlightTimeProvider provider = new FlightTimeProvider(this.tickets);
        return provider.provideMinFlightTimes();
    }

    /**
     * Returns the minimum flight times for each carrier, without considering time zones.
     *
     * @return A map with the carrier as the key and the minimum flight time as the value.
     */
    public Map<String, Integer> getMinFlightTimesWithoutTimeZone() {
        FlightTimeProvider provider = new FlightTimeProvider(this.tickets);
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
        FlightDataReader reader = new FlightDataReader(filePath);
        this.tickets = reader.loadTickets();

        FlightDataFilter filter = new FlightDataFilter(this.tickets);
        this.tickets = filter.getFilteredTickets();
    }
}
