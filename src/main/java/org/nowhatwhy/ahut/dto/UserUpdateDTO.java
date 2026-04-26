package org.nowhatwhy.ahut.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;

    private String campus;

    private String buildingName;

    private String buildingId;

    private String roomNo;
}
