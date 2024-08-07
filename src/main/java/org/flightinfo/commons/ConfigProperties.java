package org.flightinfo.commons;

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

    /**
     * The locale for English language.
     */
    public static final Locale LOCAL_EN = Locale.of("en", "EN");

    /**
     * The locale for Russian language.
     */
    public static final Locale LOCAL_RU = Locale.of("ru", "RU");
}
