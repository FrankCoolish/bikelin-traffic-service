package de.htw.ai.traffic.bikelintrafficservice.provider.viz.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for TrafficObstructions. Testing some conditions that are necessary for the Logic
 *
 * @author kulisch
 */

public class TrafficObstructionsTest {

    @Test
    void onCreate_getStatus_ShouldReturn_unknown(){
        Incidents list = new Incidents();
        String actual = list.getStatus();
        String expected = "unknown";
        assertEquals(expected,actual);
    }

    @Test
    void onCreate_ObstructionsList_isNotNUll(){
        Incidents list = new Incidents();
        assertNotNull(list.getObstructions());
    }

    @Test
    void onCreate_ObstructionsList_isEmpty(){
        Incidents list = new Incidents();
        assertTrue(list.getObstructions().isEmpty());
    }



}
