package org.usco.parkingsystemapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExitVehicleResponseDTO {
    private String entryTime;
    private String exitTime;
    private Long minutesParked;
    private Double feeToPay;
    private String formattedInfo;
}
