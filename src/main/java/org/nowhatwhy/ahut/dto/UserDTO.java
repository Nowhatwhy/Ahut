package org.nowhatwhy.ahut.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;

    private String password;

    private String campus;

    private String buildingName;

    private String buildingId;

    private String roomNo;
}
