package org.nowhatwhy.ahut.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.dto.ChargeDTO;
import org.nowhatwhy.ahut.entity.Building;
import org.nowhatwhy.ahut.entity.Charge;
import org.nowhatwhy.ahut.entity.Result;
import org.nowhatwhy.ahut.service.IChargeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 电费
 * @author nowhatwhy
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/charge")
public class ChargeController {
    private final IChargeService chargeService;
    @GetMapping("/charge")
    public Result<Charge> queryCharge(@Validated ChargeDTO chargeDTO) {
        return Result.ok(chargeService.queryCharge(chargeDTO));
    }
    @GetMapping("/buildings/{name}")
    public Result<List<Building>> queryBuildings(@PathVariable String name) {
        return Result.ok(chargeService.queryBuildings(name));
    }
}
