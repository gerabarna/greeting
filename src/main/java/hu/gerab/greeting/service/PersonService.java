package hu.gerab.greeting.service;

import hu.gerab.greeting.domain.Gender;
import hu.gerab.greeting.domain.Person;
import jakarta.annotation.Nonnull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PersonService {

  @Nonnull
  List<Person> listPersons();

  long createPerson(String name, LocalDate birthDate, Gender gender, Set<String> interests);

  void updatePerson(
      long id, String name, LocalDate birthDate, Gender gender, Set<String> interests);

  void deletePerson(long id);
}
