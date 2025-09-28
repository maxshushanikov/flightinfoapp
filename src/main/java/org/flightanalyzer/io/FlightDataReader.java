package org.flightanalyzer.io;

import org.flightanalyzer.domain.dto.FlightTicket;

import java.util.List;

public interface FlightDataReader {
    List<FlightTicket> loadTickets();
}
