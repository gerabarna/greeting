package hu.gerab.greeting.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hu.gerab.greeting.config.TestDatabaseConfig;
import hu.gerab.greeting.domain.Gender;
import hu.gerab.greeting.domain.Person;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("test")
@ContextConfiguration(classes = TestDatabaseConfig.class)
@DataJpaTest
class PersonRepositoryTest {

  @Autowired private PersonRepository personRepository;

  @AfterEach
  void cleanup() {
    personRepository.deleteAll();
  }

  @Test
  public void whenNewPersonPersistedAndRead_thenGivenFieldsEqualAndGeneratedFieldsAreFilled() {
    assertEquals(0, personRepository.count());
    Person person =
        Person.builder()
            .name("Leo")
            .birthDate(LocalDate.of(2003, 1, 1))
            .gender(Gender.MALE)
            .interests(Set.of("swords", "ninjas"))
            .build();
    personRepository.save(person);
    personRepository.flush();
    List<Person> people = personRepository.findAll();
    assertEquals(1, people.size());
    final Person persisted = people.get(0);
    assertNotNull(persisted.getId());
    assertEquals(person.getName(), persisted.getName());
    assertEquals(person.getGender(), persisted.getGender());
    assertEquals(person.getBirthDate(), persisted.getBirthDate());
    assertContainsInAnyOrder(person.getInterests(), persisted.getInterests());
    assertEquals(person.getName(), persisted.getName());
  }

  private <T> void assertContainsInAnyOrder(Set<T> expected, Set<T> actual) {
    assertEquals(expected.size(), actual.size());
    assertTrue(expected.containsAll(actual));
    assertTrue(actual.containsAll(expected));
  }
}
