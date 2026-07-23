package com.scalecanvas.evaluation;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EvaluationNotFoundException extends RuntimeException {
    public EvaluationNotFoundException(UUID id) {
        super("Evaluation not found: " + id);
    }
}
