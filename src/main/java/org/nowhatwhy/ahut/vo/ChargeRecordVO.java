package org.nowhatwhy.ahut.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ChargeRecordVO {
    private Long dormId;

    private BigDecimal balance;

    private String buildingName;

    private String campus;

    private String roomNo;

    private String electricityType;

    private LocalDateTime createTime;
}
