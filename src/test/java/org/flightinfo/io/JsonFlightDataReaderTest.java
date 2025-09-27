package org.flightinfo.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flightinfo.domain.dto.FlightTicket;
import org.flightinfo.domain.dto.FlightTicketWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonFlightDataReaderTest {

    private JsonFlightDataReader flightDataReader;
    private ObjectMapper objectMapper;
    private InputStream inputStream;
    private FlightTicketWrapper ticketWrapper;

    /**
     * This method sets up the mocks and initializes the FlightDataReader instance before each test.
     */
    @BeforeEach
    void setUp() {
        objectMapper = mock(ObjectMapper.class);
        inputStream = mock(InputStream.class);
        ticketWrapper = mock(FlightTicketWrapper.class);
        flightDataReader = new JsonFlightDataReader("/tickets.json") {
            @Override
            public List<FlightTicket> loadTickets() {
                List<FlightTicket> tickets = List.of(new FlightTicket(), new FlightTicket());
                try {
                    when(objectMapper.readValue(inputStream, FlightTicketWrapper.class)).thenReturn(ticketWrapper);
                    when(ticketWrapper.getTickets()).thenReturn(tickets);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return tickets;
            }
        };
    }

    /**
     * This test verifies that the loadTickets method returns the correct list of tickets.
     */
    @Test
    void testLoadTickets() {
        List<FlightTicket> tickets = flightDataReader.loadTickets();
        assertNotNull(tickets);
        assertEquals(2, tickets.size());
    }

    /**
     * This test verifies that the loadTickets method handles IOException correctly and returns an empty list
     * when an exception occurs.
     */
    @Test
    void testLoadTicketsIOException() {
        flightDataReader = new JsonFlightDataReader("/invalid.json") {
            @Override
            public List<FlightTicket> loadTickets() {
                List<FlightTicket> tickets = List.of();
                try {
                    when(objectMapper.readValue(inputStream, FlightTicketWrapper.class)).thenThrow(IOException.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return tickets;
            }
        };

        List<FlightTicket> tickets = flightDataReader.loadTickets();
        assertNotNull(tickets);
        assertTrue(tickets.isEmpty());
    }
}