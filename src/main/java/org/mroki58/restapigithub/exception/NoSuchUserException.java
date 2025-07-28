package org.mroki58.restapigithub.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

import java.nio.charset.StandardCharsets;

public class NoSuchUserException extends RestClientResponseException {
    public NoSuchUserException(String username) {
        super("Not found user with username: " + username,
                HttpStatus.NOT_FOUND,
                "Not found",
                new HttpHeaders(),
                null,
                StandardCharsets.UTF_8);
    }
}
