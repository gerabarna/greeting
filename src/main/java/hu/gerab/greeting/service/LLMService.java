package hu.gerab.greeting.service;

import hu.gerab.greeting.domain.Person;

public interface LLMService {
    String generateBirthdayGreeting(Person person);
}
