package org.usco.parkingsystemapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.usco.parkingsystemapi.model.VehicleRecords;

import java.util.List;
import java.util.Optional;

public interface VehicleRecordsRepository extends JpaRepository<VehicleRecords, Long> {

    Optional<VehicleRecords> findByVehiclePlateAndExitTimeIsNull(String vehiclePlate);

    List<VehicleRecords> findVehicleRecordsByExitTimeIsNull();

    Optional<VehicleRecords> findByVehiclePlateAndExitTimeIsNotNull(String vehiclePlate);

    Optional<VehicleRecords> findTopByVehiclePlateAndExitTimeIsNotNullOrderByExitTimeDesc(String vehiclePlate);
}
