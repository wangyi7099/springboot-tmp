package cn.stylefeng.guns.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class LongJsonSerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long num, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (num != null) {
            if (String.valueOf(num).length() > 10) {
                jsonGenerator.writeString(String.valueOf(num));
            } else {
                jsonGenerator.writeNumber(num);
            }
        } else {
            jsonGenerator.writeNull();
        }


    }
}
