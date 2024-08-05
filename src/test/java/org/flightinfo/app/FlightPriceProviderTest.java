package org.flightinfo.app;

import org.flightinfo.dto.FlightTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlightPriceProviderTest {

    private List<FlightTicket> tickets;
    private FlightPriceProvider flightPriceProvider;

    /**
     * This method initializes the FlightPriceProvider instance and sets up some sample tickets before each test.
     */
    @BeforeEach
    void setUp() {
        FlightTicket ticket1 = new FlightTicket();
        ticket1.setPrice(100);

        FlightTicket ticket2 = new FlightTicket();
        ticket2.setPrice(200);

        FlightTicket ticket3 = new FlightTicket();
        ticket3.setPrice(300);

        tickets = List.of(ticket1, ticket2, ticket3);
        flightPriceProvider = new FlightPriceProvider(tickets);
    }

    /**
     * This test verifies that the calculateAveragePrice method returns the correct average price.
     */
    @Test
    void testCalculateAveragePrice() {
        double averagePrice = flightPriceProvider.calculateAveragePrice();
        assertEquals(200.0, averagePrice);
    }

    /**
     * This test verifies that the calculateMedianPrice method returns the correct median price.
     */
    @Test
    void testCalculateMedianPrice() {
        double medianPrice = flightPriceProvider.calculateMedianPrice();
        assertEquals(200.0, medianPrice);
    }

    /**
     * This test verifies that the getPriceDifference method returns the correct price difference.
     */
    @Test
    void testGetPriceDifference() {
        double priceDifference = flightPriceProvider.getPriceDifference();
        assertEquals(0.0, priceDifference);
    }

    /**
     * This test verifies that the calculateAveragePrice method returns the correct average price when there
     * is an even number of tickets.
     */
    @Test
    void testCalculateAveragePriceWithEvenNumberOfTickets() {
        FlightTicket ticket4 = new FlightTicket();
        ticket4.setPrice(400);

        tickets = List.of(tickets.get(0), tickets.get(1), tickets.get(2), ticket4);
        flightPriceProvider = new FlightPriceProvider(tickets);

        double averagePrice = flightPriceProvider.calculateAveragePrice();
        assertEquals(250.0, averagePrice);
    }

    /**
     * This test verifies that the calculateMedianPrice method returns the correct median price when there
     * is an even number of tickets.
     */
    @Test
    void testCalculateMedianPriceWithEvenNumberOfTickets() {
        FlightTicket ticket4 = new FlightTicket();
        ticket4.setPrice(400);

        tickets = List.of(tickets.get(0), tickets.get(1), tickets.get(2), ticket4);
        flightPriceProvider = new FlightPriceProvider(tickets);

        double medianPrice = flightPriceProvider.calculateMedianPrice();
        assertEquals(250.0, medianPrice);
    }
}