package hu.gerab.greeting.rest;

import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response.Status;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GenericErrorHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleResourceNotFound(IllegalArgumentException ex) {
    return getErrorResponse(ex, Status.BAD_REQUEST);
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleResourceNotFound(UnsupportedOperationException ex) {
    return getErrorResponse(ex, Status.BAD_REQUEST);
  }

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleResourceNotFound(ValidationException ex) {
    return getErrorResponse(ex, Status.BAD_REQUEST);
  }

  @ExceptionHandler(NumberFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleResourceNotFound(NumberFormatException ex) {
    return getErrorResponse(ex, Status.BAD_REQUEST);
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleResourceNotFound(NullPointerException ex) {
    return getErrorResponse(ex, Status.INTERNAL_SERVER_ERROR);
  }

  private static ErrorResponseDto getErrorResponse(Exception ex, Status status) {
    return new ErrorResponseDto(status.getStatusCode(), status.getReasonPhrase(), ex.getMessage());
  }
}
