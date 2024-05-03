package de.htw.ai.traffic.bikelintrafficservice.provider.viz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.Incidents;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;




import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class of the VizService ... TODO: Mock Third party API
 */


class VizBerlinServiceImplTest {

    private static VizBerlinService vizBerlinService;

    @BeforeAll
    static void init(){
        WebClient client = WebClient.builder()
                .baseUrl("https://api.viz.berlin.de/daten/baustellen_sperrungen.json")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs().maxInMemorySize(16*1024*1024))
                .build();
         vizBerlinService = new VizBerlinServiceImpl(client, new ObjectMapper());
    }

    @Test
    void getCurrentConstructionsSitesClosuresTest_Success(){
        Incidents list = vizBerlinService.getCurrentConstructionsSitesClosures();
        assertEquals(list.getClass(), Incidents.class);
        assertFalse(list.getObstructions().isEmpty());
    }

    @Test
    void getCurrentConstructionsSitesClosures_Given_DataBufferToSmall_Return_EmptyListWithErrorStatus(){
        WebClient client = WebClient.builder()
                .baseUrl("https://api.viz.berlin.de/daten/baustellen_sperrungen.json")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs().maxInMemorySize(1024))
                .build();

        VizBerlinService viz = new VizBerlinServiceImpl(client, new ObjectMapper());
        Incidents list = viz.getCurrentConstructionsSitesClosures();
        assertTrue(list.getStatus().contentEquals("error"));
        assertTrue(list.getObstructions().isEmpty());
    }

    @Test
    void getCurrentConstructionsSitesClosures_WrongUrl_Return_EmptyListWithErrorStatus(){
        WebClient client = WebClient.builder()
                .baseUrl("https://api.viz.berlin.de/daten/baustellen_sperrungen2.json")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs().maxInMemorySize(16*1024*1024))
                .build();

        VizBerlinService viz = new VizBerlinServiceImpl(client, new ObjectMapper());
        Incidents list = viz.getCurrentConstructionsSitesClosures();
        assertTrue(list.getStatus().contentEquals("error"));
        assertTrue(list.getObstructions().isEmpty());
    }
}