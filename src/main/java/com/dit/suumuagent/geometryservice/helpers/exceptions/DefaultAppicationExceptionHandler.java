package com.dit.suumuagent.geometryservice.helpers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
class DefaultAppicationExceptionHandler {

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entity with given id was not found.")  // 404
    @ExceptionHandler(EntityNotFoundException.class)
    void handleEntityNotFounException(HttpServletRequest req, Exception ex) {
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid parameter.")  // 400
    @ExceptionHandler(IllegalArgumentException.class)
    void handleIllegalArgumentException(HttpServletRequest req, Exception ex) {
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Insufficient privileges.")  // 403
    @ExceptionHandler(AccessDeniedException.class)
    void handleIAccessDeniedException(HttpServletRequest req, Exception ex) {
    }
}