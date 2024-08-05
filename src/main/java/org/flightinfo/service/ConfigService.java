package org.flightinfo.service;

import org.flightinfo.commons.ConfigProperties;

import java.util.Locale;

public class ConfigService {

    private String flightFilePath = ConfigProperties.FLIGHT_FILE_PATH;
    private Locale local = ConfigProperties.LOCAL_RU;

    public ConfigService(String[] args) {
        parseParameters(args);
    }

    public String getFlightFilePath() {
        return this.flightFilePath;
    }

    public Locale getFlightLocal() {
        return this.local;
    }

    private void parseParameters(String[] args) {
        if (args.length == 1) {
            this.flightFilePath = args[0];
            this.local = ConfigProperties.LOCAL_RU;
        }
        if (args.length == 2) {
            this.flightFilePath = args[0];
            String language = args[1].toLowerCase();
            String country = args[1].toUpperCase();
            this.local = Locale.of(language, country);
        }
    }
}
