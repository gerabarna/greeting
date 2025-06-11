package hu.gerab.greeting.rest;

import static java.util.stream.Collectors.*;

import hu.gerab.greeting.domain.Gender;
import hu.gerab.greeting.domain.Person;
import hu.gerab.greeting.service.PersonService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController implements PersonAPI {

  public static final Comparator<PersonDTO> PERSON_DTO_ID_COMPARATOR =
      Comparator.comparing(PersonDTO::getId);
  private final PersonService personService;

  @Autowired
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @Override
  public Set<PersonDTO> listPersons() {
    List<Person> persons = personService.listPersons();
    return persons.stream()
        .map(
            p ->
                PersonDTO.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .birthDate(p.getBirthDate())
                    .gender(p.getGender().name().toLowerCase())
                    .interests(new TreeSet<>(p.getInterests()))
                    .build())
        .collect(toCollection(() -> new TreeSet<>(PERSON_DTO_ID_COMPARATOR)));
  }

  @Override
  public long createPerson(String name, LocalDate birthDate, String gender, String interests) {
    long id = personService.createPerson(name, birthDate, mapGender(gender), parseInterests(interests));
    return id;
  }

  private static Set<String> parseInterests(String interests) {
    if (interests == null || interests.isBlank()){
      return null;
    }
    return Arrays.stream(interests.split(","))
            .map(String::trim).map(String::toLowerCase).collect(toSet());
  }

  private static Gender mapGender(String gender) {
    if (gender == null){
      return null;
    }
      return Gender.valueOf(gender.trim().toUpperCase());
  }

  @Override
  public void updatePerson(
      long id, String name, LocalDate birthDate, String gender, String interests) {
    personService.updatePerson(id, name, birthDate, mapGender(gender), parseInterests(interests));
  }

  @Override
  public void deletePerson(long id) {
    personService.deletePerson(id);
  }

  @Override
  public String greetPerson(long id) {
    return null;
  }
}
