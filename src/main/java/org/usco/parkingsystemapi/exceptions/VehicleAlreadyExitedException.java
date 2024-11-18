package org.usco.parkingsystemapi.exceptions;

public class VehicleAlreadyExitedException extends IllegalStateException {
    public VehicleAlreadyExitedException(String message) {
        super(message);
    }
}
