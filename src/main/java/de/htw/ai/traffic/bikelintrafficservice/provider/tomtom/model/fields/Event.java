package de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.fields;

import lombok.*;


@Data
@Getter
public class Event {
    private String description;
    private String code;
    private String iconCategory;
}
