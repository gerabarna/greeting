package hu.gerab.greeting.service;

import hu.gerab.greeting.domain.Gender;
import hu.gerab.greeting.domain.Person;
import jakarta.annotation.Nonnull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public interface PersonService {

  /**
   * Fetches all person records.
   */
  @Nonnull
  List<Person> listPersons();

  /**
   * Fetches a person specified by the supplied id.
   *
   * @param id the id of the person to delete
   * @throws IllegalArgumentException if id is unknown
   */
  Person getPerson(long id);

  /**
   * Creates a new person record.
   * @param name the name of the person
   * @param birthDate date of birth of the person
   * @param gender gender of the person
   * @param interests the list of interests of the person
   * @return the DB generated identifier of the person.
   */
  long createPerson(String name, LocalDate birthDate, Gender gender, Set<String> interests);

  /**
   * Updates an existing person. Supplying null in place of any argument will leave the previously existing properties
   * in place on the persisted object.
   * @param id the id of the person
   * @param name the new name of the person or null if no update needed
   * @param birthDate the new Date of Birth of the person or null if no update needed
   * @param gender the new gender of the person or null if no update needed
   * @param interests the new list of interest for the person or null if no update needed. Updating this field is NOT
   *                  additive. Supplying any interests will remove all previous interests from the person.
   * @throws IllegalArgumentException if id is unknown
   */
  void updatePerson(
      long id, String name, LocalDate birthDate, Gender gender, Set<String> interests);

  /**
   * Deletes the person specified by the supplied id.
   *
   * @param id the id of the person to delete
   * @throws IllegalArgumentException if id is unknown
   */
  void deletePerson(long id);
}
