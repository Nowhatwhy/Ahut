package org.nowhatwhy.ahut.entity;

import lombok.Data;

@Data
public class Charge {
    private String roomId;

    private Double remainingBalance;

    private Double allBalance;

    private Double usedBalance;
}
