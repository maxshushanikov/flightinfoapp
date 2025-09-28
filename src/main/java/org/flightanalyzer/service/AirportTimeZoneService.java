package org.flightanalyzer.service;

import java.time.ZoneId;
import java.util.Optional;

/**
 * Service for obtaining an airport's time zone by its IATA code
 */
public interface AirportTimeZoneService {

    /**
     * Returns the time zone for an airport by IATA code
     * @param airportCode Airport code
     * @return Optional with the time zone, empty if the airport is not found
     */
    Optional<ZoneId> getTimeZone(String airportCode);

    /**
     * Checks if the airport is known to the system
     */
    boolean isAirportKnown(String airportCode);

    /**
     * Returns the number of busy airports
     * @return the number of airports in the system
     */
    int getLoadedAirportCount();
}
