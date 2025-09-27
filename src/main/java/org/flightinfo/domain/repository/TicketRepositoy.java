package org.flightinfo.domain.repository;

import org.flightinfo.domain.dto.FlightTicket;

import java.util.List;

public interface TicketRepositoy {
    List<FlightTicket> getAllTickets();
}
