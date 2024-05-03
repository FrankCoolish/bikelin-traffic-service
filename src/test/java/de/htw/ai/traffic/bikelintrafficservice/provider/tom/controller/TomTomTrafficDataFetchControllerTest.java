package de.htw.ai.traffic.bikelintrafficservice.provider.tom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.controller.TomTomTrafficDataFetchController;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.Incidents;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.service.TomTomService;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.service.TomTomServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TomTomTrafficDataFetchControllerTest {

    private static TomTomTrafficDataFetchController tomTomTrafficDataFetchController, tomTomTrafficDataFetchControllerFail;

    @BeforeAll
    static void init(){
        WebClient client = WebClient.builder()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs().maxInMemorySize(16*1024*1024))
                .build();

        TomTomService tomTomService = new TomTomServiceImpl(client, new ObjectMapper());
        Incidents trafficIncidents = new Incidents();
        tomTomTrafficDataFetchController = new TomTomTrafficDataFetchController(tomTomService, trafficIncidents);

        TomTomServiceImpl tomTomServiceFail = new TomTomServiceImpl(client, new ObjectMapper());
        tomTomServiceFail.setWebClient(client.mutate().baseUrl("https://api.tomtom.com/traffic/services/5/incidentDetails").build());
        Incidents trafficIncidentsFail = new Incidents();
        tomTomTrafficDataFetchControllerFail = new TomTomTrafficDataFetchController(tomTomServiceFail, trafficIncidentsFail);
    }

    @Test
    void getTrafficDataFromTomTom_Success_StatusIsOkay(){
        tomTomTrafficDataFetchController.getTrafficDataFromTomtom();
        String actual = tomTomTrafficDataFetchController.getTrafficIncidents().getStatus();
        assertEquals(actual,"okay");
    }

    @Test
    void getTrafficDataFromTomTom_onFailure_StatusIsError(){
        tomTomTrafficDataFetchControllerFail.getTrafficDataFromTomtom();
        String actual = tomTomTrafficDataFetchControllerFail.getTrafficIncidents().getStatus();
                assertEquals(actual,"error");
    }

}
