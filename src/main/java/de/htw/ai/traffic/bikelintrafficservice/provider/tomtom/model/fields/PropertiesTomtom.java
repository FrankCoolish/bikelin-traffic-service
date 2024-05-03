package de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.fields;

import lombok.Data;

import java.util.List;

@Data
public class PropertiesTomtom {
    private String id;
    private String iconCategory;
    private String magnitudeOfDelay;
    private List<Event> events;
    private String startTime;
    private String endTime;
    private String from;
    private String to;
    private String length;
    private String delay;
    private List<String> roadNumbers;
    private String timeValidity;
    private String probabilityOfOccurrence;
    private String numberOfReports;
    private String lastReportTime;
    private Tmc tmc;
    //VIZ
    private String tstore;
    private String objectState;
    private String subtype;
    private String severity;
    private String validity;

}
