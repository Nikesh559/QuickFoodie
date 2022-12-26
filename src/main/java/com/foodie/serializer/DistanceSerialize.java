package com.foodie.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.DecimalFormat;

@Slf4j
public class DistanceSerialize extends StdSerializer<Double> {


    protected DistanceSerialize(Class<Double> t) {
        super(t);
    }

    protected DistanceSerialize() {
        this(null);
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.0");
    @Override
    public void serialize(Double aDouble, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        double input = aDouble;
        jsonGenerator.writeString(String.format("%.2f", input)+"Km");
    }
}
