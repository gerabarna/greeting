package hu.gerab.greeting.rest;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

import hu.gerab.greeting.domain.Person;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("person")
@Tag(name = "Person")
// @SecurityRequirement() TBD
public interface PersonAPI {

  /**
   * For simple invocation please send a POST request to:
   * localhost:8080/payment/trasnfer?senderId=2&receiverId=4&amount=10&currency=USD
   */
  @Operation(
      summary = "Lists all persons in the system",
      responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = Set.class)))})
  @GetMapping("list")
  Set<PersonDTO> listPersons();

  @PostMapping("create")
  @Operation(
      summary = "Creates a new person in the system. Returns with the id of the created person",
      responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = Long.class)))})
  long createPerson(
      @Parameter(description = "The full name of the person", in = QUERY, required = true)
          @QueryParam("name")
          String name,
      @Parameter(description = "date of birth", in = QUERY, required = true)
          @QueryParam("dateOfBirth")
          LocalDate birthDate,
      @Parameter(
              description = "The gender of the person.",
              in = QUERY,
              required = false,
              schema =
                  @Schema(
                      type = "String",
                      allowableValues = {"MALE", "FEMALE", "OTHER"}))
          @QueryParam("gender")
          @RequestParam(required = false)
          String gender,
      @Parameter(
              description = "Comma separated list of hobbies and interests of the person.",
              in = QUERY)
          @QueryParam("interests")
          @RequestParam(required = false)
          String interests);

  @PatchMapping("update")
  @Operation(
      summary =
          "Updates an existing person in the system. The operation will fail if the user cannot "
              + "be located. Fields without supplied values will not be updated.")
  void updatePerson(
      @Parameter(description = "The id of the person", in = QUERY, required = true)
          @QueryParam("id")
          long id,
      @Parameter(description = "The full name of the person", in = QUERY)
          @QueryParam("name")
          @RequestParam(required = false)
          String name,
      @Parameter(description = "date of birth", in = QUERY)
          @QueryParam("dateOfBirth")
          @RequestParam(required = false)
          LocalDate birthDate,
      @Parameter(
              description = "The gender of the person.",
              in = QUERY,
              schema =
                  @Schema(
                      type = "String",
                      allowableValues = {"MALE", "FEMALE", "OTHER"}))
          @QueryParam("gender")
          @RequestParam(required = false)
          String gender,
      @Parameter(
              description =
                  "Comma separated hobbies and interests for the person. Updating this field is not additive:"
                      + "Please be aware that if any interests are supplied the new list will replace the old list, so "
                      + "interests that are not supplied will be lost. ",
              in = QUERY,
              required = false)
          @QueryParam("interests")
          @RequestParam(required = false)
          String interests);

  @DeleteMapping("delete")
  @Operation(summary = "Deletes a person from the system.")
  void deletePerson(
      @Parameter(description = "The id of the person", in = QUERY, required = true)
          @QueryParam("id")
          long id);

  @GetMapping("greet")
  @Operation(
      summary =
          "Generates a birthday greeting for the person."
              + "The greeting will be generated based on the persons age, gender and interests.",
      responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)))
      })
  String greetPerson(
      @Parameter(description = "The id of the person", in = QUERY, required = true)
          @QueryParam("id")
          long id);
}
