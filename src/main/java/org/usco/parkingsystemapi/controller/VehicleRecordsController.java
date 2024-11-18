package org.usco.parkingsystemapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.usco.parkingsystemapi.dto.ExitVehicleResponseDTO;
import org.usco.parkingsystemapi.dto.VehiclePlateDTO;
import org.usco.parkingsystemapi.model.VehicleRecords;
import org.usco.parkingsystemapi.service.VehicleRecordsService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/parking-system")
public class VehicleRecordsController {
    private final VehicleRecordsService vehicleRecordsService;

    @GetMapping
    public String welcome() {
        return "Welcome to the parking system API";
    }

    @PostMapping("/create")
    public ResponseEntity<String> createVehicleRecord(@RequestBody VehiclePlateDTO vehiclePlateDTO) {
        String vehiclePlate = vehiclePlateDTO.getVehiclePlate().trim();
        vehicleRecordsService.createVehicleRecord(vehiclePlate);
        return ResponseEntity.status(HttpStatus.CREATED).body("Vehículo ingresado exitosamente");
    }

    @PostMapping("/exit")
    public ResponseEntity<ExitVehicleResponseDTO> exitVehicle(@RequestBody VehiclePlateDTO vehiclePlateDTO) {
        String vehiclePlate = vehiclePlateDTO.getVehiclePlate().trim();
        ExitVehicleResponseDTO dto = vehicleRecordsService.exitVehicle(vehiclePlate);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @DeleteMapping("/cancel-exit")
    public ResponseEntity<String> cancelVehicleExit(@RequestBody VehiclePlateDTO vehiclePlateDTO) {
        String vehiclePlate = vehiclePlateDTO.getVehiclePlate().trim();
        vehicleRecordsService.cancelVehicleExit(vehiclePlate);
        return ResponseEntity.status(HttpStatus.OK).body("Salida cancelada exitosamente");
    }

    @GetMapping("/currently-parked")
    public ResponseEntity<List<VehicleRecords>> getCurrentlyParkedVehicles() {
        return ResponseEntity.ok().body(vehicleRecordsService.getCurrentlyParkedVehicles());
    }

    @GetMapping("/all")
    public ResponseEntity<List<VehicleRecords>> getVehicleRecords() {
        return ResponseEntity.ok().body(vehicleRecordsService.getVehicleRecords());
    }
}
