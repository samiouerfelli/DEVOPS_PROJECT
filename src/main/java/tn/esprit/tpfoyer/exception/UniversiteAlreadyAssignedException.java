package tn.esprit.tpfoyer.exception;

public class UniversiteAlreadyAssignedException extends RuntimeException {
    public UniversiteAlreadyAssignedException(String message) {
        super(message);
    }
}