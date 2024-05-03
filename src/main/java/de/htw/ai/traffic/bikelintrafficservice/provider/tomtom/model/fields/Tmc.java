package de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.fields;

import lombok.Data;

import java.util.List;


@Data
public class Tmc {
    private String countryCode;
    private String tableNumber;
    private String tableVersion;
    private String direction;
    private List<Points> points;
}
