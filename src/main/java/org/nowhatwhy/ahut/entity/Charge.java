package org.nowhatwhy.ahut.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Charge {
    private Long dormId;

    private String roomId;

    private BigDecimal remainingBalance;

    private BigDecimal allBalance;

    private BigDecimal usedBalance;
}
