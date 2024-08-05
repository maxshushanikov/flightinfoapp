package org.flightinfo.filter;

import org.flightinfo.dto.FlightTicket;

import java.util.List;

/**
 * This class filters the flights
 */
public class FlightDataFilter {

    private final List<FlightTicket> tickets;

    public FlightDataFilter(List<FlightTicket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Filters tickets based on origin and destination city
     * @return list of tickets
     */
    public List<FlightTicket> getFilteredTickets() {
        return this.tickets.stream()
            .filter(ticket -> ticket.getOrigin().equals("VVO")
                && ticket.getDestination().equals("TLV"))
            .toList();
    }
}
