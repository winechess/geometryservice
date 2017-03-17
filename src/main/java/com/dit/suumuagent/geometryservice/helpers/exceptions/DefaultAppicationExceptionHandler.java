package com.dit.suumuagent.geometryservice.helpers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@ControllerAdvice
class DefaultAppicationExceptionHandler {

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entity with given id was not found.")  // 404
    @ExceptionHandler(EntityNotFoundException.class)
    void handleEntityNotFounException(HttpServletRequest req, Exception ex) {
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity handleIllegalArgumentException(HttpServletRequest req, Exception ex) {
        HashMap<String, String> body = new HashMap<>();
        body.put("message", ex.getLocalizedMessage());
        body.put("type", ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);    //400
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity handleIAccessDeniedException(HttpServletRequest req, Exception ex) {

        HashMap<String, String> body = new HashMap<>();
        body.put("message", ex.getLocalizedMessage());
        body.put("type", ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);    //403
    }
}