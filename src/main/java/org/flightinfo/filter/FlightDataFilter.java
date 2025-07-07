package org.flightinfo.filter;

import org.flightinfo.domain.dto.FlightTicket;

import java.util.List;

/**
 * This class provides methods to filter flight tickets based on specific criteria.
 */
public class FlightDataFilter {

    private final List<FlightTicket> tickets;

    /**
     * Constructor to initialize the {@link FlightDataFilter} with a list of flight tickets.
     *
     * @param tickets The list of flight tickets.
     */
    public FlightDataFilter(List<FlightTicket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Filters the flight tickets to include only those with the origin "VVO" and destination "TLV".
     *
     * @return a list of filtered flight tickets.
     */
    public List<FlightTicket> getFilteredTickets() {
        return this.tickets.stream()
            .filter(ticket -> ticket.getOrigin().equals("VVO")
                && ticket.getDestination().equals("TLV"))
            .toList();
    }
}
