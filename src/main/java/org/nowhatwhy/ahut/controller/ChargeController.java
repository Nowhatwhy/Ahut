package org.nowhatwhy.ahut.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.dto.ChargeDTO;
import org.nowhatwhy.ahut.enitity.Building;
import org.nowhatwhy.ahut.enitity.Charge;
import org.nowhatwhy.ahut.service.ChargeService;
import org.springframework.http.ResponseEntity;
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
    private final ChargeService chargeService;
    @GetMapping("/charge")
    public ResponseEntity<Charge> queryCharge(ChargeDTO chargeDTO) {
        return ResponseEntity.ok(chargeService.queryCharge(chargeDTO));
    }
    @GetMapping("/buildings/{name}")
    public ResponseEntity<List<Building>> queryBuildings(@PathVariable String name) {
        return ResponseEntity.ok(chargeService.queryBuildings(name));
    }
}
