package de.htw.ai.traffic.bikelintrafficservice.provider.viz.model;

import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.fields.GeometryViz;
import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.fields.PropertiesViz;
import lombok.Data;

@Data
public class Obstruction {
    private String type;
    private PropertiesViz properties;
    private GeometryViz geometry;
}
