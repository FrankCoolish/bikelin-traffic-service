package de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.fields;

import lombok.Data;

@Data
public class PropertiesViz {
    private String id;
    private String tstore;
    private String objectState;
    private String subtype;
    private String severity;
    private Validity validity;
    private String direction;
    private String icon;
    private String is_future;
    private String street;
    private String section;
    private String content;
}
