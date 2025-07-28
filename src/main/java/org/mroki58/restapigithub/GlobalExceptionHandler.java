package org.mroki58.restapigithub;

import org.mroki58.restapigithub.exception.MissingUsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(MissingUsernameException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public Map<String, String> handleMissingUsername() {
//        Map<String, String> error = new HashMap<>();
//        error.put("status", "422");
//        error.put("message", "Missing required query parameter: username");
//        return error;
//    }
}
