package de.htw.ai.traffic.bikelintrafficservice.provider.viz.controller;

import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.Incidents;
import de.htw.ai.traffic.bikelintrafficservice.provider.viz.service.VizBerlinService;
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
public class VizTrafficDataFetchController {

    private final VizBerlinService vizBerlinService;

    @Getter
    @Qualifier("trafficObstructionsVizBerlin")
    private Incidents trafficObstructionsViz;

    public VizTrafficDataFetchController (VizBerlinService vizBerlinService, Incidents trafficObstructions) {
        this.vizBerlinService = vizBerlinService;
        this.trafficObstructionsViz =trafficObstructions;
    }



    /**
     * Method to get the traffic data from VIZ every 60 minutes
     */
    @Scheduled(fixedRate = 60000 * 2)
    public void getTrafficDataFromViz() {
        Incidents temp = vizBerlinService.getCurrentConstructionsSitesClosures();
        if(!temp.getStatus().contentEquals("okay")) {
            trafficObstructionsViz.setStatus(temp.getStatus());
        } else {
            trafficObstructionsViz.setLastModified(temp.getLastModified());
            trafficObstructionsViz.setStatus(temp.getStatus());
            trafficObstructionsViz.setObstructions(temp.getObstructions());
        }
    }
}
