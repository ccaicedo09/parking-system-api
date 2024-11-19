package org.usco.parkingsystemapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.usco.parkingsystemapi.dto.ExitVehicleResponseDTO;
import org.usco.parkingsystemapi.dto.VehicleResponseDTO;
import org.usco.parkingsystemapi.exceptions.InvalidPlatePatternException;
import org.usco.parkingsystemapi.exceptions.VehicleAlreadyExitedException;
import org.usco.parkingsystemapi.exceptions.VehicleAlreadyParkedException;
import org.usco.parkingsystemapi.model.VehicleRecords;
import org.usco.parkingsystemapi.repository.VehicleRecordsRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class VehicleRecordsService {

    private final VehicleRecordsRepository vehicleRecordsRepository;
    private static final double MINUTE_RATE = 97;
    private static final Pattern VEHICLE_PLATE_PATTERN = Pattern.compile("^[A-Z]{3}\\d{3}$");

    public String createVehicleRecord(String vehiclePlate) {

        if (!VEHICLE_PLATE_PATTERN.matcher(vehiclePlate).matches()) {
            throw new InvalidPlatePatternException("");
        }

        Optional<VehicleRecords> checkExistingRecord = vehicleRecordsRepository
                .findByVehiclePlateAndExitTimeIsNull(vehiclePlate);

        if (checkExistingRecord.isPresent()) {
            throw new VehicleAlreadyParkedException("");
        } else {
            VehicleRecords vehicleRecords = new VehicleRecords();
            vehicleRecords.setVehiclePlate(vehiclePlate);
            vehicleRecords.setEntryTime(LocalDateTime.now());
            vehicleRecordsRepository.save(vehicleRecords);
            return "Vehiculo ingresado exitosamente";
        }
    }

    public ExitVehicleResponseDTO exitVehicle(String vehiclePlate) {

        if (!VEHICLE_PLATE_PATTERN.matcher(vehiclePlate).matches()) {
            throw new InvalidPlatePatternException("");
        }

        VehicleRecords vehicleRecord = vehicleRecordsRepository
                .findByVehiclePlateAndExitTimeIsNull(vehiclePlate)
                .orElseThrow(() -> new EntityNotFoundException(""));

            vehicleRecord.setExitTime(LocalDateTime.now());
            Duration duration = Duration.between(vehicleRecord.getEntryTime(), vehicleRecord.getExitTime());
            double feeToPay = calculateFeeToPay(duration);
            vehicleRecord.setPaidFee(feeToPay);

            ExitVehicleResponseDTO exitVehicleResponseDTO = new ExitVehicleResponseDTO();
            exitVehicleResponseDTO.setEntryTime(formatDateTime(vehicleRecord.getEntryTime()));
            exitVehicleResponseDTO.setExitTime(formatDateTime(vehicleRecord.getExitTime()));
            exitVehicleResponseDTO.setMinutesParked(duration.toMinutes());
            exitVehicleResponseDTO.setFeeToPay(feeToPay);

            vehicleRecordsRepository.save(vehicleRecord);
            return exitVehicleResponseDTO;
    }

    public String cancelVehicleExit(String vehiclePlate) {
        VehicleRecords vehicleRecord = vehicleRecordsRepository
                .findTopByVehiclePlateAndExitTimeIsNotNullOrderByExitTimeDesc(vehiclePlate)
                .orElseThrow(() -> new EntityNotFoundException(""));

        LocalDateTime exitTime = vehicleRecord.getExitTime();
        Duration durationSinceExit = Duration.between(exitTime, LocalDateTime.now());

        if (durationSinceExit.toMinutes() <= 5) {
            vehicleRecord.setExitTime(null);
            vehicleRecord.setPaidFee(null);
            vehicleRecordsRepository.save(vehicleRecord);
            return "Salida cancelada exitosamente";
        } else {
            throw new VehicleAlreadyExitedException("");
        }
    }

    public List<VehicleResponseDTO> getCurrentlyParkedVehicles() {
        List<VehicleRecords> vehicleRecords = vehicleRecordsRepository.findVehicleRecordsByExitTimeIsNull();
        return vehicleRecords.stream()
                .map(vehicleRecord -> {
                    VehicleResponseDTO vehicleResponseDTO = new VehicleResponseDTO();
                    vehicleResponseDTO.setId(vehicleRecord.getId());
                    vehicleResponseDTO.setVehiclePlate(vehicleRecord.getVehiclePlate());
                    vehicleResponseDTO.setEntryTime(formatDateTime(vehicleRecord.getEntryTime()));
                    return vehicleResponseDTO;
                })
                .toList();
    }

    public List<VehicleRecords> getVehicleRecords() {
        return vehicleRecordsRepository.findAll();
    }

    // Private methods

    private double calculateFeeToPay(Duration duration) {
        long minutesParked = duration.toMinutes();
        return minutesParked * MINUTE_RATE;
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDate = dateTime.format(dateFormatter);
        String formattedTime = dateTime.format(timeFormatter);
        return formattedDate + " a las " + formattedTime;
    }
}
