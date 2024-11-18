package org.usco.parkingsystemapi.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body("Vehículo no encontrado.");
    }

    @ExceptionHandler(InvalidPlatePatternException.class)
    public ResponseEntity<String> handleInvalidPlatePatternException(InvalidPlatePatternException e) {
        return ResponseEntity.badRequest().body("Formato de placa inválido.");
    }

    @ExceptionHandler(VehicleAlreadyExitedException.class)
    public ResponseEntity<String> handleVehicleAlreadyExitedException(VehicleAlreadyExitedException e) {
        return ResponseEntity.badRequest().body("No se puede cancelar la salida del vehículo, han pasado más de 5 minutos.");
    }

    @ExceptionHandler(VehicleAlreadyParkedException.class)
    public ResponseEntity<String> handleVehicleAlreadyExitedException(VehicleAlreadyParkedException e) {
        return ResponseEntity.badRequest().body("El vehículo ya se encuentra parqueado.");
    }
}
