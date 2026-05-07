package org.nowhatwhy.ahut.dto;

import lombok.Data;

@Data
public class ChargeDTO {
    private Long dormId;

    private String campus;

    private String buildingName;

    private String buildingId;

    private String roomNo;

    private String electricityType;
}