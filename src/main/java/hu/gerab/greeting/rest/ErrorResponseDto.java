package hu.gerab.greeting.rest;

import lombok.Builder;
import lombok.Data;

@Builder
@Data

public class ErrorResponseDto {

    /**
     * Http status description
     */
    private int status;

    /**
     * Error message
     */
    private String error;

    /**
     * Details about error
     */
    private String message;

}
