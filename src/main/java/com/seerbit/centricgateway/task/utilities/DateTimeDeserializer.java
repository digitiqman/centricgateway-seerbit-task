package com.seerbit.centricgateway.task.utilities;


import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import java.io.IOException;

public class DateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    protected DateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
            throws JsonProcessingException, IOException {
        return LocalDateTime.parse(parser.readValueAs(String.class), ISO_DATE_TIME);
    }

}