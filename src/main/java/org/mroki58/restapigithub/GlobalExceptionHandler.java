package org.mroki58.restapigithub;

import org.mroki58.restapigithub.exception.MissingUsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingUsernameException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleMissingUsername() {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("status", "422");
        errorBody.put("message", "Missing required query parameter: username");
        return ResponseEntity.status(404)
                .body(errorBody);
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, String>> handleRestClientResponseException(RestClientResponseException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("status", String.valueOf(ex.getStatusCode().value()));
        errorBody.put("message", ex.getResponseBodyAsString());

        return ResponseEntity.status(404)
                .body(errorBody);
    }
}
