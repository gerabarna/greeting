package hu.gerab.greeting.persistence;

import hu.gerab.greeting.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
