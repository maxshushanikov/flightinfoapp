package org.flightanalyzer.service.impl;

import org.flightanalyzer.service.AirportTimeZoneService;
import org.flightanalyzer.commons.ConfigProperties;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * In-memory implementation of the airport time zone service
 * Loads data from a properties file and provides quick access
 */
public class InMemoryAirportTimeZoneService implements AirportTimeZoneService {

    private static final Logger logger = Logger.getLogger(InMemoryAirportTimeZoneService.class.getName());

    private final Map<String, ZoneId> airportTimeZones;
    private final String propertiesFilePath;

    public InMemoryAirportTimeZoneService() {
        this("/airport-timezones.properties");
    }

    public InMemoryAirportTimeZoneService(String propertiesFilePath) {
        this.airportTimeZones = new ConcurrentHashMap<>();
        this.propertiesFilePath = propertiesFilePath;
        loadTimeZonesFromFile();
    }

    @Override
    public Optional<ZoneId> getTimeZone(String airportCode) {
        if (airportCode == null || airportCode.trim().isEmpty()) {
            return Optional.empty();
        }
        String normalizedCode = normalizeAirportCode(airportCode);
        return Optional.ofNullable(airportTimeZones.get(normalizedCode));
    }

    @Override
    public boolean isAirportKnown(String airportCode) {
        if (airportCode == null || airportCode.trim().isEmpty()) {
            return false;
        }
        String normalizedCode = normalizeAirportCode(airportCode);
        return airportTimeZones.containsKey(normalizedCode);
    }

    @Override
    public int getLoadedAirportCount() {
        return airportTimeZones.size();
    }

    /**
     * Loads airport-to-timezone mapping from the properties file
     */
    private void loadTimeZonesFromFile() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getResourceAsStream(propertiesFilePath)) {
            if (input == null) {
                logger.warning("Airport timezones file not found: " + propertiesFilePath);
                loadDefaultTimeZones();
                return;
            }

            properties.load(input);
            int loadedCount = 0;

            for (String airportCode : properties.stringPropertyNames()) {
                String timeZoneId = properties.getProperty(airportCode).trim();
                if (!timeZoneId.isEmpty()) {
                    try {
                        ZoneId zoneId = ZoneId.of(timeZoneId);
                        airportTimeZones.put(normalizeAirportCode(airportCode), zoneId);
                        loadedCount++;
                    } catch (Exception e) {
                        logger.warning("Invalid timezone '" + timeZoneId + "' for airport: " + airportCode);
                    }
                }
            }

            logger.info("Successfully loaded " + loadedCount + " airport timezones from: " + propertiesFilePath);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading airport timezones from: " + propertiesFilePath, e);
            loadDefaultTimeZones();
        }
    }

    /**
     * Loads the default base time zones
     */
    private void loadDefaultTimeZones() {
        Map<String, String> defaultTimeZones = Map.ofEntries(
                Map.entry("VVO", "Asia/Vladivostok"),
                Map.entry("TLV", "Asia/Jerusalem"),
                Map.entry("SVO", "Europe/Moscow"),
                Map.entry("DME", "Europe/Moscow"),
                Map.entry("VKO", "Europe/Moscow"),
                Map.entry("LED", "Europe/Moscow"),
                Map.entry("JFK", "America/New_York"),
                Map.entry("LAX", "America/Los_Angeles"),
                Map.entry("LHR", "Europe/London"),
                Map.entry("CDG", "Europe/Paris"),
                Map.entry("FRA", "Europe/Berlin"),
                Map.entry("DXB", "Asia/Dubai"),
                Map.entry("HKG", "Asia/Hong_Kong"),
                Map.entry("NRT", "Asia/Tokyo"),
                Map.entry("SYD", "Australia/Sydney")
        );

        defaultTimeZones.forEach((code, zone) -> {
            try {
                airportTimeZones.put(code, ZoneId.of(zone));
            } catch (Exception e) {
                logger.warning("Invalid default timezone '" + zone + "' for airport: " + code);
            }
        });

        logger.info("Loaded " + defaultTimeZones.size() + " default airport timezones");
    }

    /**
     * Force reloading data from file
     */
    public void reload() {
        logger.info("Reloading airport timezones from: " + propertiesFilePath);
        airportTimeZones.clear();
        loadTimeZonesFromFile();
    }

    /**
     * Adds or updates the time zone for an airport
     */
    public void addOrUpdateTimeZone(String airportCode, ZoneId zoneId) {
        String normalizedCode = normalizeAirportCode(airportCode);
        airportTimeZones.put(normalizedCode, zoneId);
        logger.fine("Updated timezone for " + normalizedCode + ": " + zoneId);
    }

    /**
     * Removes the airport from the cache
     */
    public void removeTimeZone(String airportCode) {
        String normalizedCode = normalizeAirportCode(airportCode);
        airportTimeZones.remove(normalizedCode);
        logger.fine("Removed timezone for: " + normalizedCode);
    }

    private String normalizeAirportCode(String code) {
        return code.toUpperCase().trim();
    }
}
