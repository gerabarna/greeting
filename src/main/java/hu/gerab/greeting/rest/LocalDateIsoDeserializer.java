package hu.gerab.greeting.rest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class LocalDateIsoDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        String localDateString = jp.getText();
        return Objects.isNull(localDateString) ? null : LocalDate.parse(localDateString);
    }

}
