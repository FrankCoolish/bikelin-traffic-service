package de.htw.ai.traffic.bikelintrafficservice.provider.tom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.controller.TomTomTrafficApiController;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.Incidents;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.io.ClassPathResource;

import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebFluxTest(TomTomTrafficApiControllerTest.class)
public class TomTomTrafficApiControllerTest {


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    private Incidents trafficIncidents;

    private static File resource;

    @BeforeAll
    static void init() throws IOException {
        resource = new ClassPathResource("testData/tomtom.json").getFile();

    }

    @Test
    void getAllTraffic_Success_Return200() throws IOException {
        trafficIncidents = objectMapper.readValue(resource, Incidents.class);
        trafficIncidents.setStatus("okay");

        webTestClient = WebTestClient.bindToController(new TomTomTrafficApiController(new ObjectMapper(),trafficIncidents))
                .build();

        webTestClient.get()
                .uri("/traffic/tomtom/all-traffic")
                .exchange()
                .expectStatus().is2xxSuccessful();

    }

    @Test
    void getAllTraffic_DataBaseError_Return_ServerError() throws IOException {
        trafficIncidents = objectMapper.readValue(resource, Incidents.class);
        trafficIncidents.setStatus("error");

        webTestClient = WebTestClient.bindToController(new TomTomTrafficApiController(new ObjectMapper(),trafficIncidents))
                .build();

        webTestClient.get()
                .uri("/traffic/tomtom/all-traffic")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void getAllTraffic_WrongURL_Return404() throws IOException {
        trafficIncidents = objectMapper.readValue(resource, Incidents.class);
        trafficIncidents.setStatus("okay");

        webTestClient = WebTestClient.bindToController(new TomTomTrafficApiController(new ObjectMapper(),trafficIncidents))
                .build();

        webTestClient.get()
                .uri("/traffic/tomtom/all-traffic2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllTraffic_StatusIsUnknown_Return_ServerError() throws IOException {
        trafficIncidents = objectMapper.readValue(resource, Incidents.class);
        trafficIncidents.setStatus("unknown");

        webTestClient = WebTestClient.bindToController(new TomTomTrafficApiController(new ObjectMapper(),trafficIncidents))
                .build();

        webTestClient.get()
                .uri("/traffic/tomtom/all-traffic")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void getAllTraffic_Success_Returns_correctJson() throws IOException{
        trafficIncidents = objectMapper.readValue(resource, Incidents.class);
        trafficIncidents.setStatus("okay");

        String expectedFirst = "13.0905760436";
        String expectedLast = "52.4791547849";

        webTestClient = WebTestClient
                .bindToController(new TomTomTrafficApiController(new ObjectMapper(),trafficIncidents))
                .build()
                .mutate()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        String result = webTestClient.get()
                .uri("/traffic/tomtom/all-traffic")
                .exchange()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertNotNull(result);
        assertTrue(result.contains(expectedFirst)); //checking for first info
        assertTrue(result.contains(expectedLast)); //checking for first info
    }


    @Test
    void getAllJams_Success_Return200() throws IOException {
        trafficIncidents = objectMapper.readValue(resource, Incidents.class);
        trafficIncidents.setStatus("okay");

        webTestClient = WebTestClient.bindToController(new TomTomTrafficApiController(new ObjectMapper(),trafficIncidents))
                .build();

        webTestClient.get()
                .uri("/traffic/tomtom/jams")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void getAllJams_WrongURL_Return404() throws IOException {
        trafficIncidents = objectMapper.readValue(resource, Incidents.class);
        trafficIncidents.setStatus("okay");

        webTestClient = WebTestClient.bindToController(new TomTomTrafficApiController(new ObjectMapper(),trafficIncidents))
                .build();

        webTestClient.get()
                .uri("/traffic/tomtom/jams2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllJams_DataBaseError_Return_ServerError() throws IOException {
        trafficIncidents = objectMapper.readValue(resource, Incidents.class);
        trafficIncidents.setStatus("error");

        webTestClient = WebTestClient.bindToController(new TomTomTrafficApiController(new ObjectMapper(),trafficIncidents))
                .build();

        webTestClient.get()
                .uri("/traffic/tomtom/jams")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void getAllJams_StatusIsUnknown_Return_ServerError() throws IOException {
        trafficIncidents = objectMapper.readValue(resource, Incidents.class);
        trafficIncidents.setStatus("unknown");

        webTestClient = WebTestClient.bindToController(new TomTomTrafficApiController(new ObjectMapper(),trafficIncidents))
                .build();

        webTestClient.get()
                .uri("/traffic/tomtom/jams")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
