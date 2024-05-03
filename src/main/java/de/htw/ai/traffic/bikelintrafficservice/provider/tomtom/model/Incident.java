package de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model;

import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.fields.GeometryTomtom;
import de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.fields.PropertiesTomtom;
import lombok.Data;

@Data
public class Incident {
    private String type;
    private GeometryTomtom geometry;
    private PropertiesTomtom properties;
}
