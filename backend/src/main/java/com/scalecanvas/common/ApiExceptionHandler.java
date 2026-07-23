package com.scalecanvas.common;

import com.scalecanvas.scenario.application.InvalidScenarioException;
import com.scalecanvas.scenario.application.ScenarioNotFoundException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(InvalidScenarioException.class)
    ProblemDetail invalidScenario(InvalidScenarioException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detail.setTitle("Invalid architecture scenario");
        detail.setType(URI.create("https://scalecanvas.local/problems/invalid-scenario"));
        return detail;
    }

    @ExceptionHandler(ScenarioNotFoundException.class)
    ProblemDetail notFound(ScenarioNotFoundException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        detail.setTitle("Scenario not found");
        return detail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validation(MethodArgumentNotValidException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request validation failed");
        detail.setTitle("Validation error");
        Map<String, String> violations = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                violations.putIfAbsent(error.getField(), error.getDefaultMessage()));
        detail.setProperty("violations", violations);
        return detail;
    }
}
