package de.htw.ai.traffic.bikelintrafficservice.config;

import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.Incidents;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DataSourceConfig {

    /**
     * this Bean should hold the most recent traffic data from TomTom
     *
     */
    @Bean("trafficIncidentsTomTom")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.Incidents trafficIncidents(){
        return new de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.Incidents();
    }

    /**
     * this Bean should hold the most recent traffic data from VIZ
     *
     */
    @Bean("trafficObstructionsVizBerlin")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Incidents trafficObstructions() {
        return new Incidents();
    }
}

