package hu.gerab.greeting.service;

import static hu.gerab.greeting.domain.Gender.*;
import static org.junit.jupiter.api.Assertions.*;

import hu.gerab.greeting.domain.Gender;
import hu.gerab.greeting.domain.Person;
import hu.gerab.greeting.persistence.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

class PersonServiceImplTest {

  public static final String LEO = "Leo";
  public static final LocalDate BIRTH_DATE = LocalDate.of(2003, 1, 1);
  private PersonServiceImpl personService;

  @BeforeEach
  void setup() {
    personService = new PersonServiceImpl(new PersonRepositoryStub());
  }

  @Test
  public void whenPersonCreated_thanItCanBeFetched() {
    final long id = personService.createPerson(LEO, BIRTH_DATE, null, null);
    final Person person = personService.getPerson(id);
    assertPerson(person, id, LEO, BIRTH_DATE, null);
  }

  @Test
  public void givenExistingPerson_whenOnlyNameUpdated_thanOtherFieldsStaySame() {
    final long id = personService.createPerson(LEO, BIRTH_DATE, MALE, Set.of("swords", "ninjas"));
    personService.updatePerson(id, "Mickey", null, null, null);
    Person person = personService.getPerson(id);
    assertPerson(person, id, "Mickey", BIRTH_DATE, MALE, "swords", "ninjas");
  }

  @Test
  public void givenExistingPerson_whenOnlyBirthDateUpdated_thanOtherFieldsStaySame() {
    final long id = personService.createPerson(LEO, BIRTH_DATE, MALE, Set.of("swords", "ninjas"));
    personService.updatePerson(id, null, LocalDate.of(1987,1,1), null, null);
    Person person = personService.getPerson(id);
    assertPerson(person, id, LEO, LocalDate.of(1987,1,1), MALE, "swords", "ninjas");
  }

  @Test
  public void givenExistingPerson_whenOnlyGenderUpdated_thanOtherFieldsStaySame() {
    final long id = personService.createPerson(LEO, BIRTH_DATE, MALE, Set.of("swords", "ninjas"));
    personService.updatePerson(id, null, null, OTHER, null);
    Person person = personService.getPerson(id);
    assertPerson(person, id, LEO, BIRTH_DATE, OTHER, "swords", "ninjas");
  }

  @Test
  public void givenExistingPerson_whenOnlyInterestsUpdated_thanOtherFieldsStaySame() {
    final long id = personService.createPerson(LEO, BIRTH_DATE, MALE, Set.of("swords", "ninjas"));
    personService.updatePerson(id, null, null, null, Set.of("katanas","turtles"));
    Person person = personService.getPerson(id);
    assertPerson(person, id, LEO, BIRTH_DATE, MALE, "katanas", "turtles");
  }

  @Test
  public void givenTwoExistingPerson_whenOneDeleted_thanOnlyTheOtherRemains() {
    assertEquals(0, personService.listPersons().size());
    final long id1 = personService.createPerson(LEO, BIRTH_DATE, MALE, Set.of("swords", "ninjas"));
    final long id2 = personService.createPerson("Mickey", BIRTH_DATE, MALE, Set.of("nunchucks", "foolery"));
    personService.deletePerson(id2);
    final List<Person> persons = personService.listPersons();
    assertEquals(1, personService.listPersons().size());
    assertPerson(persons.get(0), id1, LEO, BIRTH_DATE, MALE, "swords", "ninjas");
  }

  private static void assertPerson(
      Person person,
      long id,
      String name,
      LocalDate birthDate,
      Gender gender,
      String... interests) {
    assertEquals(id, person.getId());
    assertEquals(name, person.getName());
    assertEquals(birthDate, person.getBirthDate());
    assertEquals(gender, person.getGender());
    Set<String> expectedInterests = null;
    if (interests.length > 0) {
      expectedInterests = new TreeSet<>(Arrays.asList(interests));
    }
    assertEquals(expectedInterests, person.getInterests());
  }

  private class PersonRepositoryStub implements PersonRepository {
    private Map<Long, Person> idToEntityMap = new ConcurrentHashMap<>();
    private long idGenerator = 1;

    @Override
    public void flush() {}

    @Override
    public <S extends Person> S saveAndFlush(S entity) {
      return save(entity);
    }

    @Override
    public <S extends Person> List<S> saveAllAndFlush(Iterable<S> entities) {
      return saveAll(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<Person> entities) {
      deleteAll(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
      deleteAllById(ids);
    }

    @Override
    public void deleteAllInBatch() {
      idToEntityMap.clear();
    }

    @Override
    public Person getOne(Long id) {
      return getById(id);
    }

    @Override
    public Person getById(Long id) {
      final Person person = idToEntityMap.get(id);
      if (person == null) {
        throw new EntityNotFoundException("Could not find entity for id=" + id);
      }
      return person;
    }

    @Override
    public Person getReferenceById(Long id) {
      return getById(id);
    }

    @Override
    public <S extends Person> Optional<S> findOne(Example<S> example) {
      throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Person> List<S> findAll(Example<S> example) {
      throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Person> List<S> findAll(Example<S> example, Sort sort) {
      throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Person> Page<S> findAll(Example<S> example, Pageable pageable) {
      throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Person> long count(Example<S> example) {
      throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Person> boolean exists(Example<S> example) {
      throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Person, R> R findBy(
        Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
      throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Person> S save(S entity) {
      if (entity.getId() == null) {
        entity.setId(idGenerator++);
      }
      idToEntityMap.put(entity.getId(), entity);
      return entity;
    }

    @Override
    public <S extends Person> List<S> saveAll(Iterable<S> entities) {
      List<S> saved = new ArrayList<>();
      for (S entity : entities) {
        save(entity);
        saved.add(entity);
      }
      return saved;
    }

    @Override
    public Optional<Person> findById(Long id) {
      return Optional.ofNullable(idToEntityMap.get(id));
    }

    @Override
    public boolean existsById(Long id) {
      return idToEntityMap.containsKey(id);
    }

    @Override
    public List<Person> findAll() {
      return new ArrayList<>(idToEntityMap.values());
    }

    @Override
    public List<Person> findAllById(Iterable<Long> ids) {
      List<Person> found = new ArrayList<>();
      for (Long id : ids) {
        final Person person = idToEntityMap.get(id);
        if (person != null) {
          found.add(person);
        }
      }
      return found;
    }

    @Override
    public long count() {
      return idToEntityMap.size();
    }

    @Override
    public void deleteById(Long id) {
      idToEntityMap.remove(id);
    }

    @Override
    public void delete(Person entity) {
      idToEntityMap.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
      for (Long id : ids) {
        deleteById(id);
      }
    }

    @Override
    public void deleteAll(Iterable<? extends Person> entities) {
      for (Person person : entities) {
        delete(person);
      }
    }

    @Override
    public void deleteAll() {
      idToEntityMap.clear();
    }

    @Override
    public List<Person> findAll(Sort sort) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Page<Person> findAll(Pageable pageable) {
      throw new UnsupportedOperationException();
    }
  }
}
