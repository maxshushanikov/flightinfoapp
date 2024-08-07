package org.flightinfo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * This class is a wrapper for a list of flight tickets.
 * It is used to deserialize JSON data into a list of {@link FlightTicket} objects.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightTicketWrapper {
    private List<FlightTicket> tickets;

    /**
     * Gets the list of flight tickets.
     *
     * @return The list of flight tickets.
     */
    public List<FlightTicket> getTickets() {
        return tickets;
    }

    /**
     * Sets the list of flight tickets.
     *
     * @param tickets The list of flight tickets.
     */
    public void setTickets(List<FlightTicket> tickets) {
        this.tickets = tickets;
    }
}
