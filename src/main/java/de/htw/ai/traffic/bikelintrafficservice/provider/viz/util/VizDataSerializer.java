package de.htw.ai.traffic.bikelintrafficservice.provider.viz.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.Obstruction;
import de.htw.ai.traffic.bikelintrafficservice.provider.viz.model.fields.GeometryViz;
import java.io.IOException;
import java.util.List;

public class VizDataSerializer extends StdSerializer<Obstruction> {

    public VizDataSerializer() {
        this(null);
    }

    public VizDataSerializer(Class<Obstruction> o){
        super(o);
    }

    @Override
    public void serialize(Obstruction obstruction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", obstruction.getProperties().getId());
        jsonGenerator.writeStringField("type", obstruction.getGeometry().getType());
        jsonGenerator.writeStringField("subtype", obstruction.getProperties().getSubtype());
        //if Geometry is a point get coordinates
        if(obstruction.getGeometry().getClass().equals(GeometryViz.Point.class)){
            String[] coordinates = (String[]) obstruction.getGeometry().getCoordinates();
            jsonGenerator.writeStringField("longitude", coordinates[0]);
            jsonGenerator.writeStringField("latitude", coordinates[1]);
        } else if(obstruction.getGeometry().getClass().equals(GeometryViz.GeometryCollection.class)) {
            GeometryViz[] geometry = (GeometryViz[])obstruction.getGeometry().getCoordinates();
            String[] coordinates = (String[]) geometry[0].getCoordinates();
            jsonGenerator.writeStringField("longitude",coordinates[0]);
            jsonGenerator.writeStringField("latitude", coordinates[1]);
        }
        jsonGenerator.writeEndObject();
    }

}
