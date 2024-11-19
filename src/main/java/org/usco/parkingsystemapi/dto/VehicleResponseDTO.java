package org.usco.parkingsystemapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponseDTO {
    private Long id;
    private String vehiclePlate;
    private String entryTime;
    private String exitTime;
    private Double paidFee;
}
