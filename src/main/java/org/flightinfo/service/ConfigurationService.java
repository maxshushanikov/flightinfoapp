package org.flightinfo.service;

import org.flightinfo.commons.ConfigProperties;

import java.util.Locale;

/**
 * This class provides configuration settings
 */
public class ConfigurationService {

    private String flightFilePath;
    private Locale locale;

    /**
     * Constructor that takes an array of arguments and parses them.
     * @param args The command-line arguments.
     */
    public ConfigurationService(String[] args) {
        this.flightFilePath = ConfigProperties.FLIGHT_FILE_PATH;
        this.locale = ConfigProperties.LOCAL_RU;
        parseParameters(args);
    }

    /**
     * Getter method for the flight file path.
     * @return the flight file path.
     */
    public String getFlightFilePath() {
        return this.flightFilePath;
    }

    /**
     * Getter method for the flight locale.
     * @return the flight locale.
     */
    public Locale getFlightLocale() {
        return this.locale;
    }

    /**
     * Method to parse the command line arguments and set the file path and locale accordingly.
     * @param args The command-line arguments.
     */
    private void parseParameters(String[] args) {
        if (args.length == 1) {
            if (isLocale(args[0])) {
                // One argument, if it's a locale, use default file path and provided locale
                this.locale = new Locale(args[0].toLowerCase(), args[0].toUpperCase());
            } else {
                // One argument, if it's a file path, use provided file path and default locale
                this.flightFilePath = args[0];
            }
        } else if (args.length == 2) {
            // Two arguments, use provided file path and locale
            this.flightFilePath = args[0];
            this.locale = new Locale(args[1].toLowerCase(), args[1].toUpperCase());
        } else if (args.length > 2) {
            // Throw an exception if the number of arguments is invalid
            throw new IllegalArgumentException("Invalid number of arguments");
        }
    }

    /**
     * Method to check if a string is a valid locale (two letters).
     * @param arg The string to check.
     * @return true if the string is a valid locale, false otherwise.
     */
    private boolean isLocale(String arg) {
        return arg.length() == 2 && arg.chars().allMatch(Character::isLetter);
    }
}
