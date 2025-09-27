package org.flightinfo.app;

import org.flightinfo.domain.dto.FlightTicket;

import java.util.List;

/**
 * This class provides methods to calculate price-related information for a list of flight tickets.
 */
public class FlightPriceProvider {

    private final List<FlightTicket> tickets;

    /**
     * Constructor to initialize the {@link FlightPriceProvider} with a list of flight tickets.
     *
     * @param tickets The list of flight tickets.
     */
    public FlightPriceProvider(List<FlightTicket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Calculates the absolute difference between the average price and the median price of the flight tickets.
     *
     * @return The absolute difference between the average price and the median price.
     */
    public double getPriceDifference() {
        return Math.abs(calculateAveragePrice() - calculateMedianPrice());
    }

    /**
     * Calculates the average price of the flight tickets.
     *
     * @return The average price of the flight tickets.
     */
    public double calculateAveragePrice() {
        return this.tickets.stream().mapToInt(FlightTicket::getPrice).average().orElse(0);
    }

    /**
     * Calculates the median price of the flight tickets.
     *
     * @return The median price of the flight tickets.
     */
    public double calculateMedianPrice() {
        List<Integer> prices = this.tickets.stream().map(FlightTicket::getPrice).sorted().toList();
        return prices.size() % 2 == 0
                ? (prices.get(prices.size() / 2 - 1) + prices.get(prices.size() / 2)) / 2.0
                : prices.get(prices.size() / 2);
    }
}
