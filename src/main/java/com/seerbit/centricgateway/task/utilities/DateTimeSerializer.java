package com.seerbit.centricgateway.task.utilities;




import java.io.IOException;
import java.time.LocalDateTime;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DateTimeSerializer extends StdSerializer<LocalDateTime> {

    public DateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(
            LocalDateTime value,
            JsonGenerator gen,
            SerializerProvider arg2)
            throws IOException {
        gen.writeString(ISO_DATE_TIME.format(value));
    }
    
}