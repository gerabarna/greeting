package hu.gerab.greeting.service;

import hu.gerab.greeting.domain.Gender;
import hu.gerab.greeting.domain.Person;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class OllamaLlama3Service implements LLMService {
  private final WebClient webClient;
  private final String llama3ModelVersion;

  @Autowired
  public OllamaLlama3Service(
      WebClient webClient, @Value("${ollama.model.version}") String ollamaModelVersion) {
    this.webClient = webClient;
    this.llama3ModelVersion = ollamaModelVersion;
  }

  @Override
  public String generateBirthdayGreeting(Person person) {
    final String prompt = generateBirthdayPrompt(person);
    LOGGER.trace("Sending prompt='{}' to llama3", prompt);

    OllamaRequest request = OllamaRequest.create(llama3ModelVersion, prompt);

    final String greeting =
        webClient
            .post()
            .uri("/api/generate")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OllamaResponse.class)
            .map(OllamaResponse::response)
            .block();
    LOGGER.trace("Generated greeting='{}' for prompt={}", greeting, prompt);
    return greeting;
  }

  private static String generateBirthdayPrompt(Person person) {
    StringBuilder sb = new StringBuilder("Generate a birthday greeting for a ");
    if (person.getBirthDate() != null) {
      final LocalDate birthDate = person.getBirthDate();
      final long years = birthDate.until(LocalDate.now(), ChronoUnit.YEARS);
      sb.append(years).append(" years old ");
    }
    Gender gender = person.getGender();
    if (gender != null && gender != Gender.OTHER) {
      sb.append(gender.name().toLowerCase()).append(" ");
    } else {
      sb.append("person ");
    }
    if (person.getInterests() != null) {
      sb.append(" who is interested in: ").append(String.join(",", person.getInterests()));
    }
    sb.append(". Only respond with the generated message.");
    String prompt = sb.toString();
    return prompt;
  }

  public record OllamaRequest(
      String model, String prompt, boolean stream, Map<String, Object> options) {
    public static OllamaRequest create(String model, String prompt) {
      return new OllamaRequest(model, prompt, false, Map.of());
    }
  }

  public record OllamaResponse(String model, String response, boolean done) {}
}
