package org.nowhatwhy.ahut.vo;

import lombok.Data;

@Data
public class UserBindingVO {
    private Long bindingId;
    private String buildingName;
    private String roomNo;
    private String electricityType;
    private Double balance;
}