package de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class Incidents {
    private String type;
    private String name;
    private String status = "unknown";
    private String lastModified;

    @JsonAlias("features")
    private List<Incident> Incidents = new ArrayList<>();

}
