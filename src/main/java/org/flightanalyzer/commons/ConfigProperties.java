package org.flightanalyzer.commons;

import java.util.Locale;

/**
 * This class contains configuration properties for the flight information application.
 * It includes constants for the flight data file path and supported locales.
 */
public class ConfigProperties {

    /**
     * The file path to the JSON file containing flight tickets.
     */
    public static final String FLIGHT_FILE_PATH = "/tickets.json";
    public static final Locale LOCAL_EN = Locale.of("en", "EN");
    public static final Locale LOCAL_RU = Locale.of("ru", "RU");

    public static final String DEFAULT_DEPARTURE_TIMEZONE = "Asia/Vladivostok";
    public static final String DEFAULT_ARRIVAL_TIMEZONE = "Asia/Jerusalem";
    public static final boolean USE_AIRPORT_TIMEZONES = true;
    public static final String TIMEZONE_SERVICE_IMPLEMENTATION = "in-memory";

    public static final String AIRPORT_TIMEZONES_FILE = "/airport-timezones.properties";

}
