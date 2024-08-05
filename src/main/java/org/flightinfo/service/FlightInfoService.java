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

    public FlightInfoService(ConfigService cfgService) {
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
     * Returns the minimum flight time for each carrier.
     *
     * @return map of minimum flight times for each carrier
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
     * Processes flight data from a JSON file.
     *
     * @param cfgService path to the file with flight data
     */
    public void processFlightData(ConfigService cfgService){
        String filePath = cfgService.getFlightFilePath();
        FlightDataReader reader = new FlightDataReader(filePath);
        this.tickets = reader.loadTickets();

        FlightDataFilter filter = new FlightDataFilter(this.tickets);
        this.tickets = filter.getFilteredTickets();
    }
}
