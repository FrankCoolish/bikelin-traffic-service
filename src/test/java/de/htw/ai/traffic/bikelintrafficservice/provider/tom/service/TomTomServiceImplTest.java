package de.htw.ai.traffic.bikelintrafficservice.provider.tom.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.Incidents;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.service.TomTomService;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.service.TomTomServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.web.reactive.function.client.WebClient;




import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class of the TomTomService
 */

public class TomTomServiceImplTest {

    private static TomTomService tomTomService;

    @BeforeAll
    static void init(){
        WebClient client = WebClient.builder()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs().maxInMemorySize(16*1024*1024))
                .build();
       tomTomService = new TomTomServiceImpl(client, new ObjectMapper());
    }

    @Test
    void getCurrentConstructionsSitesClosuresTest_Success(){
        Incidents list = tomTomService.getTrafficIncidentsTomtom();
        assertEquals(list.getClass(), Incidents.class);
        assertFalse(list.getIncidents().isEmpty());
    }

    @Test
    void getCurrentConstructionsSitesClosures_Given_DataBufferToSmall_Return_EmptyListWithErrorStatus(){
        WebClient client = WebClient.builder()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs().maxInMemorySize(1024))
                .build();

        TomTomService tomtom = new TomTomServiceImpl(client, new ObjectMapper());
        Incidents list = tomtom.getTrafficIncidentsTomtom();
        assertTrue(list.getStatus().contentEquals("error"));
        assertTrue(list.getIncidents().isEmpty());
    }

    @Test
    void getCurrentConstructionsSitesClosures_WrongUrl_Return_EmptyListWithErrorStatus(){
        WebClient client = WebClient.builder()
                .baseUrl("https://api.tomtom.com/traffic/services/5/incidentDetails2")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs().maxInMemorySize(16*1024*1024))
                .build();

        TomTomServiceImpl tomtom = new TomTomServiceImpl(client, new ObjectMapper());
        tomtom.setWebClient(client);
        Incidents list = tomtom.getTrafficIncidentsTomtom();
        assertTrue(list.getStatus().contentEquals("error"));
        assertTrue(list.getIncidents().isEmpty());
    }

}
