package tn.esprit.tpfoyer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EtudiantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEtudiantNotFoundException(EtudiantNotFoundException ex) {
        return ex.getMessage();
    }

}
