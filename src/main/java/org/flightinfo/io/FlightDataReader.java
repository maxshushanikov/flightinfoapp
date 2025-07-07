package org.flightinfo.io;

import org.flightinfo.domain.dto.FlightTicket;

import java.util.List;

public interface FlightDataReader {
    List<FlightTicket> loadTickets();
}
