package de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.fields;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import java.util.List;


@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.fields.GeometryTomtom.Point.class, name  = "Point"),
        @JsonSubTypes.Type(value = de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.fields.GeometryTomtom.LineString.class, name = "LineString"),
})
public class GeometryTomtom {
    public String type;

    @JsonTypeName("LineString")
    public static class LineString extends de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.fields.GeometryTomtom {
        public List<String[]> coordinates;
    }

    @JsonTypeName("Point")
    public static class Point extends de.htw.ai.traffic.bikelintrafficservice.provider.tomtom.model.fields.GeometryTomtom {
        public String[] coordinates;
    }
}
