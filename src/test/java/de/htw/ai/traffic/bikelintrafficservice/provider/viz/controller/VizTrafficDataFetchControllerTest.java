package de.htw.ai.traffic.bikelintrafficservice.provider.viz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.Incidents;
import de.htw.ai.traffic.bikelintrafficservice.provider.viz.service.VizBerlinService;
import de.htw.ai.traffic.bikelintrafficservice.provider.viz.service.VizBerlinServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VizTrafficDataFetchControllerTest {
    private static VizTrafficDataFetchController vizTrafficDataFetchController;
    private static VizTrafficDataFetchController vizTrafficDataFetchControllerFail;


    @BeforeAll
    static void init(){
        WebClient client = WebClient.builder()
                .baseUrl("https://api.viz.berlin.de/daten/baustellen_sperrungen.json")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs().maxInMemorySize(16*1024*1024))
                .build();
        VizBerlinService vizBerlinService = new VizBerlinServiceImpl(client, new ObjectMapper());
        vizTrafficDataFetchController = new VizTrafficDataFetchController(vizBerlinService,new Incidents());


        WebClient clientFail = WebClient.builder()
                .baseUrl("https://api.viz.berlin.de/daten/baustellen_sperrungen2.json")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs().maxInMemorySize(16*1024*1024))
                .build();

        VizBerlinService vizBerlinServiceFail = new VizBerlinServiceImpl(clientFail, new ObjectMapper());
        vizTrafficDataFetchControllerFail = new VizTrafficDataFetchController(vizBerlinServiceFail,new Incidents());
    }


    @Test
    void getTrafficDataFromViz_Success_StatusIsOkay() {
        vizTrafficDataFetchController.getTrafficDataFromViz();
        String actual = vizTrafficDataFetchController.getTrafficObstructionsViz().getStatus();
        assertEquals("okay",actual);
    }

    @Test
    void getTrafficDataFromViz_withFailure_Status_ShouldBe_Error(){
        vizTrafficDataFetchControllerFail.getTrafficDataFromViz();
        String actual = vizTrafficDataFetchControllerFail.getTrafficObstructionsViz().getStatus();
        assertEquals("error",actual);
    }
}
