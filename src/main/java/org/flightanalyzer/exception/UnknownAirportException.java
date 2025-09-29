package org.flightanalyzer.exception;

/**
 * Exception thrown when the airport code is unknown.
 */
public class UnknownAirportException extends RuntimeException {

    public UnknownAirportException(String message) {
        super(message);
    }

    public UnknownAirportException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownAirportException(String airportCode, String additionalInfo) {
        super(String.format("Unknown airport code: '%s'. %s", airportCode, additionalInfo));
    }
}
