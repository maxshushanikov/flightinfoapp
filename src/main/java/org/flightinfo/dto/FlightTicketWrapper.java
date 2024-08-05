package org.flightinfo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * This class represents the JSON structure, holds a list of tickets.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightTicketWrapper {
    private List<FlightTicket> tickets;

    public List<FlightTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<FlightTicket> tickets) {
        this.tickets = tickets;
    }
}
