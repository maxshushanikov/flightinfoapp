package org.flightanalyzer.domain.repository;

import org.flightanalyzer.domain.dto.FlightTicket;

import java.util.List;

public interface TicketRepositoy {
    List<FlightTicket> getAllTickets();
}
