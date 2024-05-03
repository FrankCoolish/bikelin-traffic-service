package de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.controller;

import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.Incidents;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.service.TomTomService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

/**
 * Rest Controller which is responsible to fetch traffic data from the different providers
 *
 * @author kulisch
 */

@Controller
public class TomTomTrafficDataFetchController {

    private final TomTomService tomTomService;

    @Getter
    @Qualifier("trafficIncidentsTomTom")
    private Incidents trafficIncidents;

    public TomTomTrafficDataFetchController (TomTomService tomTomService, Incidents trafficIncidents) {
        this.tomTomService = tomTomService;
        this.trafficIncidents = trafficIncidents;
    }




    /**
     * Method to get the traffic data from TomTom every minute
     */
    @Scheduled(fixedRate = 60*1000 )
    public void getTrafficDataFromTomtom(){
        Incidents temp = tomTomService.getTrafficIncidentsTomtom();
        if(!temp.getStatus().contentEquals("okay")){
            trafficIncidents.setStatus(temp.getStatus());
        } else {
            trafficIncidents.setStatus(temp.getStatus());
            trafficIncidents.setLastModified(temp.getLastModified());
            trafficIncidents.setIncidents(temp.getIncidents());
        }
    }

}
