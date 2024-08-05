package org.flightinfo.app;

import org.flightinfo.dto.FlightTicket;

import java.util.List;

/**
 * This class provides flight price
 */
public class FlightPriceProvider {

    private final List<FlightTicket> tickets;

    public FlightPriceProvider(List<FlightTicket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Calculates difference between price average and price median
     * @return double value
     */
    public double getPriceDifference() {
        return Math.abs(calculateAveragePrice() - calculateMedianPrice());
    }

    /**
     * Calculates flight average price.
     * @return average price
     */
    public double calculateAveragePrice() {
        return tickets.stream().mapToInt(FlightTicket::getPrice).average().orElse(0);
    }

    /**
     * Calculates flight median price. The median price is sorted and calculated.
     * @return median price
     */
    public double calculateMedianPrice() {
        List<Integer> prices = tickets.stream().map(FlightTicket::getPrice).sorted().toList();
        return prices.size() % 2 == 0
                ? (prices.get(prices.size() / 2 - 1) + prices.get(prices.size() / 2)) / 2.0
                : prices.get(prices.size() / 2);
    }
}
