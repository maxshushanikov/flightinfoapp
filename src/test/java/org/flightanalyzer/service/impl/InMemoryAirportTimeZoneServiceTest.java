package org.flightanalyzer.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryAirportTimeZoneServiceTest {

    private InMemoryAirportTimeZoneService service;

    @BeforeEach
    void setUp() {
        service = new InMemoryAirportTimeZoneService("/airport-timezones.properties");
    }

    @Test
    void testGetTimeZone_KnownAirport() {
        Optional<ZoneId> result = service.getTimeZone("VVO");
        assertTrue(result.isPresent());
        assertEquals(ZoneId.of("Asia/Vladivostok"), result.get());
    }

    @Test
    void testGetTimeZone_UnknownAirport() {
        Optional<ZoneId> result = service.getTimeZone("UNKNOWN");
        assertFalse(result.isPresent());
    }

    @Test
    void testGetTimeZone_NullCode() {
        Optional<ZoneId> result = service.getTimeZone(null);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetTimeZone_EmptyCode() {
        Optional<ZoneId> result = service.getTimeZone("");
        assertFalse(result.isPresent());
    }

    @Test
    void testIsAirportKnown() {
        assertTrue(service.isAirportKnown("VVO"));
        assertTrue(service.isAirportKnown("vvo"));
        assertFalse(service.isAirportKnown("UNKNOWN"));
    }

    @Test
    void testGetLoadedAirportCount() {
        assertTrue(service.getLoadedAirportCount() > 0);
    }

    @Test
    void testAddOrUpdateTimeZone() {
        ZoneId testZone = ZoneId.of("UTC");
        service.addOrUpdateTimeZone("TEST", testZone);

        assertTrue(service.isAirportKnown("TEST"));
        assertEquals(testZone, service.getTimeZone("TEST").get());
    }
}
