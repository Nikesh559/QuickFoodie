package com.foodie.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.DecimalFormat;

public class PriceSerializer extends StdSerializer<Double> {
    private final String PREFIX_CURRENCY = "\u20B9";

    protected PriceSerializer(Class<Double> t) {
        super(t);
    }

    protected PriceSerializer() {
        this(null);
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.0");
    @Override
    public void serialize(Double aDouble, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        double input = aDouble;
        jsonGenerator.writeString(PREFIX_CURRENCY + String.format("%.2f", input));
    }
}
