package hu.gerab.greeting.rest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hu.gerab.greeting.domain.Gender;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class PersonDTO {
  private Long id;
  private String name;

  @JsonSerialize(using = LocalDateIsoSerializer.class)
  @JsonDeserialize(using = LocalDateIsoDeserializer.class)
  private LocalDate birthDate;

  private String gender;
  private Set<String> interests;
}
