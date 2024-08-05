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
public class FlightDataService {

    private List<FlightTicket> tickets;

    public FlightDataService(String filePath) {
        processFlightData(filePath);
    }

    /**
     * Returns the minimum flight time for each carrier.
     *
     * @return map of minimum flight times for each carrier
     */
    public Map<String, Duration> getMinFlightTimes() {
        FlightTimeProvider provider = new FlightTimeProvider(tickets);
        return provider.provideMinFlightTimes();
    }

    /**
     * Returns the minimum flight time for each carrier.
     *
     * @return map of minimum flight times for each carrier
     */
    public Map<String, Integer> getMinFlightTimesWithoutTimeZone() {
        FlightTimeProvider provider = new FlightTimeProvider(tickets);
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
     * @param filePath path to the file with flight data
     */
    public void processFlightData(String filePath){
        FlightDataReader reader = new FlightDataReader(filePath);
        tickets = reader.loadTickets();

        FlightDataFilter filter = new FlightDataFilter(tickets);
        tickets = filter.getFilteredTickets();
    }
}
