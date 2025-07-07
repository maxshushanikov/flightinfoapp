package org.flightinfo.filter;

import org.flightinfo.domain.dto.FlightTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightDataFilterTest {

    private List<FlightTicket> tickets;
    private FlightDataFilter flightDataFilter;

    /**
     * This method initializes the FlightDataFilter instance and sets up some sample tickets before each test.
     */
    @BeforeEach
    void setUp() {
        FlightTicket ticket1 = new FlightTicket();
        ticket1.setOrigin("VVO");
        ticket1.setDestination("TLV");

        FlightTicket ticket2 = new FlightTicket();
        ticket2.setOrigin("VVO");
        ticket2.setDestination("JFK");

        FlightTicket ticket3 = new FlightTicket();
        ticket3.setOrigin("SVO");
        ticket3.setDestination("TLV");

        tickets = List.of(ticket1, ticket2, ticket3);
        flightDataFilter = new FlightDataFilter(tickets);
    }

    /**
     * This test verifies that the getFilteredTickets method returns the correct list of filtered tickets based
     * on the origin and destination.
     */
    @Test
    void testGetFilteredTickets() {
        List<FlightTicket> filteredTickets = flightDataFilter.getFilteredTickets();
        assertNotNull(filteredTickets);
        assertEquals(1, filteredTickets.size());
        assertEquals("VVO", filteredTickets.get(0).getOrigin());
        assertEquals("TLV", filteredTickets.get(0).getDestination());
    }
}