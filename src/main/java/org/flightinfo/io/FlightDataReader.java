package org.flightinfo.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flightinfo.dto.FlightTicket;
import org.flightinfo.dto.FlightTicketWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class reads the JSON file tickets.json into a TicketWrapper object
 * and extracts the list of tickets.
 */
public class FlightDataReader {

    private static final Logger logger = Logger.getLogger(FlightDataReader.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();
    private final String filePath;

    public FlightDataReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads the JSON file and maps it to a TicketWrapper.
     *
     * @return the list of tickets from the wrapper
     */
    public List<FlightTicket> loadTickets() {
        try (InputStream in = FlightDataReader.class.getResourceAsStream(this.filePath)) {
            if (in == null) {
                logger.log(Level.SEVERE, "File not found: " + this.filePath);
                return Collections.emptyList();
            }
            FlightTicketWrapper wrapper = mapper.readValue(in, FlightTicketWrapper.class);
            return wrapper.getTickets();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading tickets from file: " + this.filePath, e);
            return Collections.emptyList();
        }
    }
}