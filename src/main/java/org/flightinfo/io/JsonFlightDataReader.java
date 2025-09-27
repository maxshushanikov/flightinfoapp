package org.flightinfo.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flightinfo.domain.dto.FlightTicket;
import org.flightinfo.domain.dto.FlightTicketWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class loads flight tickets from a JSON file into a {@link FlightTicketWrapper} object
 * and extracts the list of flight tickets.
 */
public class JsonFlightDataReader implements FlightDataReader {

    private static final Logger logger = Logger.getLogger(JsonFlightDataReader.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();
    private final String filePath;

    /**
     * Constructor to initialize the FlightDataReader with the file path of the JSON file.
     *
     * @param filePath The file path of the JSON file.
     */
    public JsonFlightDataReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads the JSON file and maps it into a FlightTicketWrapper object.
     * Extracts and returns the list of flight tickets from the wrapper.
     *
     * @return The list of flight tickets from the wrapper, or an empty list if an error occurs.
     */
    public List<FlightTicket> loadTickets() {
        try (InputStream in = JsonFlightDataReader.class.getResourceAsStream(this.filePath)) {
            if (in == null) {
                this.logger.log(Level.SEVERE, "File not found: " + this.filePath);
                return Collections.emptyList();
            }
            FlightTicketWrapper wrapper = this.mapper.readValue(in, FlightTicketWrapper.class);
            return wrapper.getTickets();
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Error reading tickets from file: " + this.filePath, e);
            return Collections.emptyList();
        }
    }
}
