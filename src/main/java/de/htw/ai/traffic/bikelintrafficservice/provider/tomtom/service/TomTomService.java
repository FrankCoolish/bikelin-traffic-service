package de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.service;

import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.Incidents;


/**
 * Interface for the TomTomService
 *
 * @author kulisch
 */
public interface TomTomService {

    /**
     * Gets the life traffic date from the TomTom api
     *
     * @return all Incidents from the TomTom live traffic Service
     */
    Incidents getTrafficIncidentsTomtom();
}
