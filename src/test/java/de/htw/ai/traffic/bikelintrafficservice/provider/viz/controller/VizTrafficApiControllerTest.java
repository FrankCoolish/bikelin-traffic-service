package de.htw.ai.traffic.bikelintrafficservice.provider.viz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.traffic.bikelintrafficservice.config.DataSourceConfig;
import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.Incidents;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


@ContextConfiguration(classes = DataSourceConfig.class)
@WebFluxTest(controllers = VizTrafficApiController.class)
public class VizTrafficApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private Incidents trafficObstructions;

    @Autowired
    private ObjectMapper objectMapper;
    private static File resource;

    @BeforeAll
    static void init() throws IOException {
        resource = new ClassPathResource("testData/vizBerlin.json").getFile();
        //System.out.println(resource);
    }

    @Test
    void getAllObstructions_Success_Return200() throws IOException {
        trafficObstructions = objectMapper.readValue(resource, Incidents.class);
        trafficObstructions.setStatus("okay");

        webTestClient = WebTestClient.bindToController(new VizTrafficApiController(new ObjectMapper(),trafficObstructions))
                                    .build();

        webTestClient.get()
                .uri("/traffic/viz/all-obstructions")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void getAllObstructions_WrongURL_Return404() throws IOException {
        trafficObstructions = objectMapper.readValue(resource, Incidents.class);
        trafficObstructions.setStatus("okay");

        webTestClient = WebTestClient.bindToController(new VizTrafficApiController(new ObjectMapper(),trafficObstructions))
                .build();

        webTestClient.get()
                .uri("/traffic/viz/all-obstructions2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllObstructions_DataBaseError_Return_ServerError() throws IOException {
        trafficObstructions = objectMapper.readValue(resource, Incidents.class);
        trafficObstructions.setStatus("error");

        webTestClient = WebTestClient.bindToController(new VizTrafficApiController(new ObjectMapper(),trafficObstructions))
                .build();

        webTestClient.get()
                .uri("/traffic/viz/all-obstructions")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void getAllObstructions_StatusIsUnknown_Return_ServerError() throws IOException {
        trafficObstructions = objectMapper.readValue(resource, Incidents.class);
        trafficObstructions.setStatus("unknown");

        webTestClient = WebTestClient.bindToController(new VizTrafficApiController(new ObjectMapper(),trafficObstructions))
                .build();

        webTestClient.get()
                .uri("/traffic/viz/all-obstructions")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    //not pretty Test but enough for the prototyp... check for first and last info(incident)
    @Test
    void getAllObstructions_Success_Returns_correctJson() throws IOException{
        trafficObstructions = objectMapper.readValue(resource, Incidents.class);
        trafficObstructions.setStatus("okay");
        String expectedFirst = "Brückenschäden, Fahrbahn auf einen Fahrstreifen verengt, für Fahrzeuge über 3,5 t Gewicht sowie einer Fahrzeugbreite von >2 m gesperrt";
        String expectedLast = "in beiden Richtungen zwischen Europaplatz und  Rahel-Hirsch-Straße";

        webTestClient = WebTestClient
                .bindToController(new VizTrafficApiController(new ObjectMapper(),trafficObstructions))
                .build()
                .mutate()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                .defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        String eeResult =
            webTestClient.get()
                    .uri("/traffic/viz/all-obstructions")
                    .exchange()
                    .expectBody(String.class)
                    .returnResult().getResponseBody();

        assertNotNull(eeResult);
        assertTrue(eeResult.contains(expectedFirst)); //checking for first info
        assertTrue(eeResult.contains(expectedLast)); //checking for first info
    }


    @Test
    void getAllWarnings_Success_Return200() throws IOException {
        trafficObstructions = objectMapper.readValue(resource, Incidents.class);
        trafficObstructions.setStatus("okay");

        webTestClient = WebTestClient.bindToController(new VizTrafficApiController(new ObjectMapper(),trafficObstructions))
                .build();

        webTestClient.get()
                .uri("/traffic/viz/all-warnings")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void getAllWarnings_WrongURL_Return404() throws IOException {
        trafficObstructions = objectMapper.readValue(resource, Incidents.class);
        trafficObstructions.setStatus("okay");

        webTestClient = WebTestClient.bindToController(new VizTrafficApiController(new ObjectMapper(),trafficObstructions))
                .build();

        webTestClient.get()
                .uri("/traffic/viz/all-warnings2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllWarnings_DataBaseError_Return_ServerError() throws IOException {
        trafficObstructions = objectMapper.readValue(resource, Incidents.class);
        trafficObstructions.setStatus("error");

        webTestClient = WebTestClient.bindToController(new VizTrafficApiController(new ObjectMapper(),trafficObstructions))
                .build();

        webTestClient.get()
                .uri("/traffic/viz/all-warnings")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void getAllWarnings_StatusIsUnknown_Return_ServerError() throws IOException {
        trafficObstructions = objectMapper.readValue(resource, Incidents.class);
        trafficObstructions.setStatus("unknown");

        webTestClient = WebTestClient
                .bindToController(new VizTrafficApiController(new ObjectMapper(),trafficObstructions))
                .build();

        webTestClient.get()
                .uri("/traffic/viz/all-warnings")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
