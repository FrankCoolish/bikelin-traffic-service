package de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.fields;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GeometryViz.Point.class, name  = "Point"),
        @JsonSubTypes.Type(value = GeometryViz.LineString.class, name = "LineString"),
        @JsonSubTypes.Type(value = GeometryViz.GeometryCollection.class, name = "GeometryCollection")
})
/*@JsonTypeInfo(use = DEDUCTION )
@JsonSubTypes({@JsonSubTypes.Type(GeometryViz.LineString.class),
                @JsonSubTypes.Type(GeometryViz.Point.class),
                @JsonSubTypes.Type(GeometryViz.GeometryCollection.class)})*/
public abstract class GeometryViz<T> {
    private String type;

    public abstract T getCoordinates();

    @Getter
    @Setter
    public static class LineString extends GeometryViz<List<String[]>>{
         List<String[]> coordinates;
    }


    @Getter
    @Setter
    public static class Point extends  GeometryViz<String[]>{

       public String[] coordinates;

    }


    @Getter
    @Setter
    public static class GeometryCollection extends GeometryViz<GeometryViz[]>{
        GeometryViz[] geometries;

        public GeometryViz[] getCoordinates(){
            return this.geometries;
        }
    }
}
