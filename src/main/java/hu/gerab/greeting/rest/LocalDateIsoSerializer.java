package hu.gerab.greeting.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateIsoSerializer extends JsonSerializer<LocalDate> {

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        if (Objects.isNull(value)) {
            gen.writeNull();
        } else {
            gen.writeString(DateTimeFormatter.ISO_DATE.format(value));
        }
    }

    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }
}
