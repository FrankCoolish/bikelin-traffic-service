package de.htw.ai.traffic.bikelintrafficservice.provider.tom.model;

import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.Incidents;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for Incidents(TomTom). Testing some conditions that are necessary for the Logic
 *
 * @author kulisch
 */

public class IncidentsTest {

    @Test
    void onCreate_getStatus_ShouldReturn_unknown(){
        Incidents list = new Incidents();
        String actual = list.getStatus();
        String expected = "unknown";
        assertEquals(expected,actual);
    }

    @Test
    void onCreate_IncidentsList_isNotNUll(){
        Incidents list = new Incidents();
        assertNotNull(list.getIncidents());
    }

    @Test
    void onCreate_IncidentsList_isEmpty(){
        Incidents list = new Incidents();
        assertTrue(list.getIncidents().isEmpty());
    }



}

