package hu.gerab.greeting.service;

import hu.gerab.greeting.domain.Gender;
import hu.gerab.greeting.domain.Person;
import hu.gerab.greeting.persistence.PersonRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

  private final PersonRepository personRepository;

  @Autowired
  public PersonServiceImpl(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @Override
  public long createPerson(String name, LocalDate birthDate, Gender gender, Set<String> interests) {
    Person person =
        Person.builder()
            .name(name)
            .birthDate(birthDate)
            .gender(gender)
            .interests(interests)
            .build();
    personRepository.saveAndFlush(person);
    return person.getId();
  }

  @Override
  public void updatePerson(
      long id, String name, LocalDate birthDate, Gender gender, Set<String> interests) {
    Optional<Person> byId = personRepository.findById(id);
    if (byId.isEmpty()) {
      throw new IllegalArgumentException("No person exists for id=" + id);
    }
    Person person = byId.get();
    if (name != null) {
      person.setName(name);
    }
    if (birthDate != null) {
      person.setBirthDate(birthDate);
    }
    if (gender != null) {
      person.setGender(gender);
    }
    if (interests != null) {
      person.setInterests(interests);
    }
    personRepository.saveAndFlush(person);
  }

  @Override
  public void deletePerson(long id) {
    if (!personRepository.existsById(id)) {
      throw new IllegalArgumentException("No record exists for id=" + id);
    }
    personRepository.deleteById(id);
  }

  @Override
  public List<Person> listPersons() {
    return personRepository.findAll();
  }

  @Override
  public Person getPerson(long id) {
    Optional<Person> byId = personRepository.findById(id);
    if (byId.isEmpty()) {
      throw new IllegalArgumentException("No person exists for id=" + id);
    }
    return byId.get();
  }
}
