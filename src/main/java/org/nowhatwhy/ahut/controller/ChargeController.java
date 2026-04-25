package org.nowhatwhy.ahut.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.dto.ChargeDTO;
import org.nowhatwhy.ahut.enitity.Charge;
import org.nowhatwhy.ahut.service.ChargeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChargeController {
    private final ChargeService chargeService;
    @PostMapping("/charge")
    public ResponseEntity<Charge> queryCharge(@RequestBody ChargeDTO chargeDTO) {
        return ResponseEntity.ok(chargeService.queryCharge(chargeDTO));
    }
}
