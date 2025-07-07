package org.flightinfo.io;

import org.flightinfo.dto.FlightTicket;

import java.util.List;

public interface FlightDataReader {
    List<FlightTicket> loadTickets();
}
