package hu.gerab.greeting.service;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hu.gerab.greeting.domain.Gender;
import hu.gerab.greeting.domain.Person;
import hu.gerab.greeting.service.OllamaLlama3Service.OllamaRequest;
import hu.gerab.greeting.service.OllamaLlama3Service.OllamaResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

class OllamaLlama3ServiceTest {

  public static final String MODEL_VERSION = "llamaVersion";
  private OllamaLlama3Service llama3Service;
  private WebClient webClient;

  @BeforeEach
  void setup() {
    webClient = spy(WebClient.class);
    llama3Service = new OllamaLlama3Service(webClient, MODEL_VERSION);
  }

  @Test
  void givenNoPerson_whenBirthdayGreetingInvoked_thanThrowException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> llama3Service.generateBirthdayGreeting(null));
  }

  @Test
  void givenExistingPerson_whenBirthdayGreetingInvoked_thanOllamaAPICalledWithAllPersonDetails() {
    // generally this test is not a great idea, as this amount of mocking requires VERY intimate knowledge about the
    // implementation, and basically this tests how the webClient works. This is more of a showcase of mockito and 
    // hamcrest usage than a great idea...
    RequestBodyUriSpec uriSpec = mock(RequestBodyUriSpec.class);
    when(webClient.post()).thenReturn(uriSpec);
    RequestBodySpec requestBodySpec = mock(RequestBodySpec.class);
    when(uriSpec.uri(anyString())).thenReturn(requestBodySpec);
    ArgumentCaptor<OllamaRequest> requestCaptor = ArgumentCaptor.forClass(OllamaRequest.class);
    RequestHeadersSpec headersSpec = mock(RequestHeadersSpec.class);
    when(requestBodySpec.bodyValue(requestCaptor.capture())).thenReturn(headersSpec);
    ResponseSpec responseSpec = mock(ResponseSpec.class);
    when(headersSpec.retrieve()).thenReturn(responseSpec);
    Mono mono = mock(Mono.class);
    when(responseSpec.bodyToMono(OllamaResponse.class)).thenReturn(mono);
    Mono mappedMono = mock(Mono.class);
    when(mono.map(any())).thenReturn(mappedMono);
    llama3Service.generateBirthdayGreeting(Person.builder().name("Will Robinson")
            .birthDate(LocalDate.of(2000,1,1))
            .gender(Gender.MALE)
            .interests(Set.of("robots","electronics","geology"))
            .build());
    verify(webClient).post();
    verifyNoMoreInteractions(webClient);
    verify(mappedMono).block();
    
    final OllamaRequest request = requestCaptor.getValue();
    assertEquals(MODEL_VERSION,request.model());
    MatcherAssert.assertThat(request.prompt(), containsString("Will Robinson"));
    long age = LocalDate.of(2000, 1, 1).until(LocalDate.now(), ChronoUnit.YEARS);
    MatcherAssert.assertThat(request.prompt(), containsString(Long.toString(age)));
    MatcherAssert.assertThat(request.prompt(), containsString("male"));
    MatcherAssert.assertThat(request.prompt(), containsString("robots"));
    MatcherAssert.assertThat(request.prompt(), containsString("electronics"));
    MatcherAssert.assertThat(request.prompt(), containsString("geology"));
  }
}
