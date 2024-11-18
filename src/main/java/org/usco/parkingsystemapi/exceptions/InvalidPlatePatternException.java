package org.usco.parkingsystemapi.exceptions;

public class InvalidPlatePatternException extends IllegalArgumentException {
    public InvalidPlatePatternException(String message) {
        super(message);
    }
}
