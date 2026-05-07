package org.nowhatwhy.ahut.controller;

import lombok.RequiredArgsConstructor;
import org.nowhatwhy.ahut.entity.Result;
import org.nowhatwhy.ahut.service.IChargeRecordService;
import org.nowhatwhy.ahut.vo.ChargeRecordVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 电费记录
 * @author nowhatwhy
 */
@RequestMapping("/charges")
@RequiredArgsConstructor
@RestController
public class ChargeRecordController {
    private final IChargeRecordService chargeRecordService;
    @GetMapping
    public Result<List<ChargeRecordVO>> listCharges() {
        return Result.ok(chargeRecordService.listCharges());
    }
}
