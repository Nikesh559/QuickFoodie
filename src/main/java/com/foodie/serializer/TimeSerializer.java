package com.foodie.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.DecimalFormat;

public class TimeSerializer extends StdSerializer<Integer> {
    private final String PREFIX_CURRENCY = "\\u20B9";

    protected TimeSerializer(Class<Integer> t) {
        super(t);
    }

    protected TimeSerializer() {
        this(null);
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.0");
    @Override
    public void serialize(Integer mins, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        int hr = 0, min = 0;
        while(mins > 60) {
            hr++;
            mins -= 60;
        }
        String time = "";
        time = hr > 0 ? hr +"hr " : "";
        time = time + mins + "mins";
        jsonGenerator.writeString(time);
    }
}
