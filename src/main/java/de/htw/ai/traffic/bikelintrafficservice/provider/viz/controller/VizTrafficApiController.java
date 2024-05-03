package de.htw.ai.traffic.bikelintrafficservice.provider.viz.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.Obstruction;
import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.Incidents;

import de.htw.ai.traffic.bikelintrafficservice.provider.viz.util.VizDataSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/traffic/viz")
public class VizTrafficApiController {

    private final ObjectMapper objectMapper;

    @Qualifier("trafficObstructionsVizBerlin")
    private Incidents trafficObstructions;

    public VizTrafficApiController(ObjectMapper objectMapper, Incidents trafficObstructions) {
        this.objectMapper = objectMapper;
        this.trafficObstructions = trafficObstructions;
    }
    private static final Logger logger = Logger.getLogger(VizTrafficApiController.class.getName());
    private static final String ERROR_JSON_SERVICE_UNAVAILABLE = "{\"error\":{\"code\":\"503\",\"description\":\"Service Unavailable\"}}";
    private static final String ERROR_JSON_INTERNAL_SERVER_ERROR = "{\"error\":{\"code\":\"500\",\"description\":\"internal server error\"}}";

    private static final String NEW_REQUEST = "-------------------------------";
    //52.53304528976089, 13.197712956618997
    private static final String RESPONSE_STRING = "[{\"id\":\"1\",\"longitude\":\"52.53304528976089\",\"latitude\":\"13.197712956618997\"},{\"id\":\"2\",\"longitude\":\"52.53304528976589\",\"latitude\":\"13.197712956618597\"}]";

    @GetMapping(path = "/all-obstructions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<String>> getAllObstructions() {
        logger.info(NEW_REQUEST);
        logger.info("Received request for all Obstructions Data from Viz");
        if(!isServiceAvailable(trafficObstructions)){
            return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ERROR_JSON_SERVICE_UNAVAILABLE));
        }
        List<Obstruction> copyObstructions = new ArrayList<>(trafficObstructions.getObstructions());

        logger.info(copyObstructions.size()+" Incidents in total received from VIZ Berlin");
        try {
            String bodyResponse = objectMapper.writeValueAsString(copyObstructions);
            return Mono.just(ResponseEntity.ok()
                    .headers(addHeaders())
                    .body(bodyResponse));
        } catch (JsonProcessingException e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ERROR_JSON_INTERNAL_SERVER_ERROR));
        }
    }


    @GetMapping(path = "/all-warnings", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("*")
    public Mono<ResponseEntity<String>> getAllWarnings() {
        logger.info(NEW_REQUEST);
        logger.info("Received request for all warnings from Viz");
        if(!isServiceAvailable(trafficObstructions)){
            return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ERROR_JSON_SERVICE_UNAVAILABLE));
        }
        List<Obstruction> copyObstructions = new ArrayList<>(trafficObstructions.getObstructions());
        List<Obstruction> warningsList = new ArrayList<>(copyObstructions.stream().filter(obstruction ->
                                        obstruction.getProperties().getIcon().contentEquals("warnung")).toList());
        logger.info(warningsList.size()+" warnings received from VIZ Berlin");
        try {
            ObjectMapper mapper =new ObjectMapper();
            SimpleModule module = new SimpleModule("VizDataSerializer",
                                                    new Version(1,0,0, null ,null ,null));
            module.addSerializer(Obstruction.class, new VizDataSerializer());
            mapper.registerModule(module);
            String bodyResponse = mapper.writeValueAsString(warningsList);
            return Mono.just(ResponseEntity.ok()
                    .headers(addHeaders())
                   .body(bodyResponse));
                  //  .body(RESPONSE_STRING));
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ERROR_JSON_INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * Helper which determines whether the Service is available and up to date
     * @param trafficList list of incidents where the status will be checked
     * @return  true, if status is okay
     *          false, if status is error or unknown
     */
    private boolean isServiceAvailable(Incidents trafficList){
        return !trafficList.getStatus().equals("error") && !trafficList.getStatus().equals("unknown");

    }

    private HttpHeaders addHeaders(){
        HttpHeaders headers = new HttpHeaders();
        if(trafficObstructions.getLastModified() != null){
            String lastModified = trafficObstructions.getLastModified();
            headers.setLastModified( Long.parseLong(lastModified));
        }
        return headers;
    }
}
