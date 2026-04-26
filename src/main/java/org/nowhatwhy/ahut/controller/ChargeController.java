package org.nowhatwhy.ahut.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.dto.ChargeDTO;
import org.nowhatwhy.ahut.enitity.Building;
import org.nowhatwhy.ahut.enitity.Charge;
import org.nowhatwhy.ahut.enitity.Result;
import org.nowhatwhy.ahut.service.IChargeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/charge")
public class ChargeController {
    private final IChargeService IChargeService;
    @GetMapping("/charge")
    public Result<Charge> queryCharge(ChargeDTO chargeDTO) {
        return Result.ok(IChargeService.queryCharge(chargeDTO));
    }
    @GetMapping("/buildings/{name}")
    public Result<List<Building>> queryBuildings(@PathVariable String name) {
        return Result.ok(IChargeService.queryBuildings(name));
    }
}
