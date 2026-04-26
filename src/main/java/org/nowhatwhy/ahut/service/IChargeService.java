package org.nowhatwhy.ahut.service;

import org.nowhatwhy.ahut.dto.ChargeDTO;
import org.nowhatwhy.ahut.enitity.Building;
import org.nowhatwhy.ahut.enitity.Charge;

import java.util.List;

public interface IChargeService {
    Charge queryCharge(ChargeDTO chargeDTO);

    List<Building> queryBuildings(String name);
}
