package de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.Incidents;
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

/**
 * Service
 */

@Service
public class TomTomServiceImpl implements TomTomService {


    private static final String TOMTOM_API_KEY = "RFs0LRYJqv1toGeuFAQmDEidmxNSEsaU";

    @Qualifier("webClient")
    private WebClient webClient;

    private final ObjectMapper objectMapper;

    public TomTomServiceImpl (WebClient webClientTomTom, ObjectMapper objectMapper) {
        this.webClient = webClientTomTom;
        this.objectMapper = objectMapper;

        this.webClient = webClient.mutate().baseUrl(urlBuilder()).build();
    }

    public void setWebClient(WebClient webClient){
        this.webClient = webClient;
    }



    private static final Logger logger = Logger.getLogger(TomTomServiceImpl.class.getName());

    private static final String BASE_URL = "https://api.tomtom.com/traffic/services/5/incidentDetails";
    private static final String BBOX_BERLIN = "13.0882097323,52.3418234221,13.7606105539,52.6697240587";
    private static final String LANGUAGE_EN_GB = "en-GB";

    private static final String UNKNOWN = "0";
    private static final String ACCIDENT = "1";
    private static final String FOG = "2";
    private static final String DANGEROUS_CONDITION = "3";
    private static final String RAIN = "4";
    private static final String ICE = "5";
    private static final String JAM = "6";
    private static final String LANE_CLOSED = "7";
    private static final String ROAD_CLOSED = "8";
    private static final String ROAD_WORKS = "9";
    private static final String WIND = "10";
    private static final String FLOODING = "11";
    private static final String BROKEN_DOWN_VEHICLE = "14";

    private static final String ALL_Incidents = "0,1,2,3,4,5,6,7,8,9,10,11,14";

    private static final String FIELDS_All = "{incidents{type,geometry{type,coordinates}," +
                                                        "properties{id,iconCategory,magnitudeOfDelay," +
                                                                    "events{description,code,iconCategory}," +
                                                                    "startTime,endTime,from,to,length,delay,roadNumbers," +
                                                                    "timeValidity,probabilityOfOccurrence,numberOfReports,lastReportTime," +
                                                                    "tmc{countryCode,tableNumber,tableVersion,direction,points{location,offset}}}}}";
    private static final String FIELDS_SELECTED = "{incidents{" +
                                                    "type," +
                                                    "geometry{" +
                                                                "type," +
                                                                "coordinates" +
                                                                "}," +
                                                    "properties{" +
                                                                "id," +
                                                                "iconCategory," +
                                                                "magnitudeOfDelay,"+
                                                                "events{" +
                                                                        "description," +
                                                                        "code," +
                                                                        "iconCategory"+
                                                                        "}" +
                                                                "}" +
                                                    "}" +
                                            "}";

    private static final String ERROR_PARSING = "Error parsing data from TomTom";
    private static final String ERROR_DATA_FETCH = "Error receiving data from TomTom, could not find resource";
    private static final String ERROR_DATA_BUFFER = "Error receiving data from TomTom, maybe adjust data buffer";
    private static final String ERROR_NULL_POINTER = "Error receiving data from TomTom, null pointer";


    private  String urlBuilder() {
        String url = BASE_URL;
        url += "?key=" + TOMTOM_API_KEY;
        url += "&bbox=" +BBOX_BERLIN;
        url += "&fields=" +FIELDS_All ;
        url += "&language=" +LANGUAGE_EN_GB;
        url += "&t=1111";
        url += "&categoryFilter=" +ALL_Incidents ;
        url += "&timeValidityFilter=present";
        return url;
    }

    @Override
    public Incidents getTrafficIncidentsTomtom() throws RuntimeException{
        try {
            ResponseEntity<String> response = webClient
                    .get()
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals,
                            clientResponse -> clientResponse.bodyToMono(String.class).map(ResourceAccessException::new))
                    .toEntity(String.class)
                    .block();

            HttpHeaders responseHeaders = response.getHeaders();
            Incidents incidents = objectMapper.readValue(response.getBody(), Incidents.class);
            incidents.setStatus("okay");
            incidents.setLastModified(String.valueOf(responseHeaders.getLastModified()));
            return incidents;
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
