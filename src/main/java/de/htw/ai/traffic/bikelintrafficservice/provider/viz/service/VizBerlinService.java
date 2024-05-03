package de.htw.ai.traffic.bikelintrafficservice.provider.viz.service;

import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.Incidents;


/**
 * Interface for the VIZBerlin Service
 *
 * @author kulisch
 */
public interface VizBerlinService {

    /**
     * Gets the life traffic date from the TomTom api
     *
     * @return all Incidents from the TomTom live traffic Service
     */
    Incidents getCurrentConstructionsSitesClosures();
}
