package org.nowhatwhy.ahut.service;

import org.nowhatwhy.ahut.dto.ChargeDTO;
import org.nowhatwhy.ahut.enitity.Charge;

public interface ChargeService {
    Charge queryCharge(ChargeDTO chargeDTO);
}
