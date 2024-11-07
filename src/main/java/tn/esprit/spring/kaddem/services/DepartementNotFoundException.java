package tn.esprit.spring.kaddem.services;

public class DepartementNotFoundException extends RuntimeException {
    public DepartementNotFoundException(String message) {
        super(message);
    }
}

