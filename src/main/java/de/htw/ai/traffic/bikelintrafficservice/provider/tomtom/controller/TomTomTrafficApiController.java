package de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.Incident;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.Incidents;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This Rest-Controller is responsible for the API methods which are using the TomTom service.
 *
 * @author kulisch
 */

@RestController
@RequestMapping("/traffic/tomtom")
public class TomTomTrafficApiController {

    private final ObjectMapper objectMapper;

    @Qualifier("trafficIncidentsTomTom")
    private Incidents trafficIncidents;

    public TomTomTrafficApiController(ObjectMapper objectMapper, Incidents trafficIncidents){
        this.objectMapper = objectMapper;
        this.trafficIncidents = trafficIncidents;
    }

    private static final Logger logger = Logger.getLogger(TomTomTrafficApiController.class.getName());

    private static final String NEW_COMMAND = "-------------------------------";

    private static final String ERROR_JSON_SERVICE_UNAVAILABLE = "{\"error\":{\"code\":\"503\",\"description\":\"Service Unavailable\"}}";
    private static final String ERROR_JSON_INTERNAL_SERVER_ERROR = "{\"error\":{\"code\":\"500\",\"description\":\"internal server error\"}}";

    /**
     * GET Request to get all Incidents in Berlin from TomTom
     * no filters
     *
     * @return JSON String with TrafficIncident Data
     */

    @GetMapping(path = "/all-traffic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<String>> getAllTraffic() {
        logger.info(NEW_COMMAND);
        logger.info("Received request for tomtom: getAllTraffic");
        if(!isServiceAvailable(trafficIncidents)){
            return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ERROR_JSON_SERVICE_UNAVAILABLE));
        }
        List<Incident> copyIncidents = new ArrayList<>(trafficIncidents.getIncidents());
        logger.info(copyIncidents.size()+" Incidents in total received from TomTom for Berlin");
        try {
            String bodyResponse = objectMapper.writeValueAsString(copyIncidents);
            return Mono.just(ResponseEntity.ok()
                    .headers(addHeaders())
                    .body(bodyResponse));
        } catch (JsonProcessingException e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ERROR_JSON_INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping(path = "/jams", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<String>> getJams() {
        logger.info(NEW_COMMAND);
        logger.info("Received request for tomtom: getJams");
        if(!isServiceAvailable(trafficIncidents)){
            return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ERROR_JSON_SERVICE_UNAVAILABLE));
        }
        List<Incident> copyIncidents = new ArrayList<>(trafficIncidents.getIncidents());
        List<Incident> jamList = copyIncidents.stream().filter(incident -> incident.getProperties().getIconCategory().contentEquals("6")).toList();
        logger.info(jamList.size()+" jams received from TomTom for Berlin");
        try {
            String bodyResponse = objectMapper.writeValueAsString(jamList);
            return Mono.just(ResponseEntity.ok()
                    .headers(addHeaders())
                    .body(bodyResponse));
        } catch (JsonProcessingException e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ERROR_JSON_INTERNAL_SERVER_ERROR));
        }
    }


    private boolean isServiceAvailable(Incidents trafficList){
        return !trafficList.getStatus().equals("error") && !trafficList.getStatus().equals("unknown");

    }

    private HttpHeaders addHeaders(){
        HttpHeaders headers = new HttpHeaders();
        if(trafficIncidents.getLastModified() != null){
            String lastModified = trafficIncidents.getLastModified();
            headers.setLastModified( Long.parseLong(lastModified));
        }
        return headers;
    }


}
