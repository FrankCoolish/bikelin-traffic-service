package de.htw.ai.traffic.bikelintrafficservice.provider.viz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.Incidents;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VizBerlinServiceImpl implements VizBerlinService {

    @Qualifier("webClient")
    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    public VizBerlinServiceImpl(WebClient webClientViz,ObjectMapper objectMapper) {
        this.webClient = webClientViz;
        this.objectMapper = objectMapper;
    }

    private static final Logger logger = Logger.getLogger(VizBerlinServiceImpl.class.getName());

    private static final String URL ="https://api.viz.berlin.de/daten/baustellen_sperrungen.json";
    private static final String ERROR_PARSING = "Error parsing data from VIZBerlin";
    private static final String ERROR_DATA_FETCH = "Error receiving data from VIZBerlin, could not find resource";
    private static final String ERROR_DATA_BUFFER = "Error receiving data from VIZBerlin, adjust data buffer";
    private static final String ERROR_NULL_POINTER = "Error receiving data from VIZBerlin, null pointer";

    @Override
    public Incidents getCurrentConstructionsSitesClosures() {
        try {
            ResponseEntity<String> response = webClient
                    .get()
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals,
                            clientResponse -> clientResponse.bodyToMono(String.class).map(ResourceAccessException::new))
                    .toEntity(String.class)
                    .block();

            HttpHeaders responseHeaders = response.getHeaders();

            Incidents obstructions = objectMapper.readValue(response.getBody(), Incidents.class);
            obstructions.setStatus("okay");
            obstructions.setLastModified(String.valueOf(responseHeaders.getLastModified()));

            return obstructions;
        } catch (JsonProcessingException e1) {
            logger.log(Level.SEVERE, ERROR_PARSING, e1);
            Incidents errorList = new Incidents();
            errorList.setStatus("error");
            return errorList;
        } catch (ResourceAccessException e2) {
            logger.log(Level.SEVERE, ERROR_DATA_FETCH);
            Incidents errorList = new Incidents();
            errorList.setStatus("error");
            return errorList;
        } catch (WebClientResponseException e3) {
            logger.log(Level.SEVERE, ERROR_DATA_BUFFER, e3);
            Incidents errorList = new Incidents();
            errorList.setStatus("error");
            return errorList;
        } catch (NullPointerException e4) {
            logger.log(Level.SEVERE, ERROR_NULL_POINTER, e4);
            Incidents errorList = new Incidents();
            errorList.setStatus("error");
            return errorList;
        }
    }
}
