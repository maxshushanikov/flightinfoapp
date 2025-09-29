package org.flightanalyzer.service.factory;

import org.flightanalyzer.service.AirportTimeZoneService;
import org.flightanalyzer.service.impl.InMemoryAirportTimeZoneService;

/**
 * Factory for creating AirportTimeZoneService instances
 */
public class AirportTimeZoneServiceFactory {

    /**
     * Creates a service with default settings
     */
    public static AirportTimeZoneService createDefaultService() {
        return new InMemoryAirportTimeZoneService();
    }

    /**
     * Creates a service with the specified configuration file
     */
    public static AirportTimeZoneService createServiceWithCustomFile(String propertiesFilePath) {
        return new InMemoryAirportTimeZoneService(propertiesFilePath);
    }
}
