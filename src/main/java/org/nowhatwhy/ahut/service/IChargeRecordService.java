package org.nowhatwhy.ahut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nowhatwhy.ahut.dto.ChargeDTO;
import org.nowhatwhy.ahut.entity.Charge;
import org.nowhatwhy.ahut.entity.ChargeRecord;

public interface IChargeRecordService extends IService<ChargeRecord> {
    Charge queryCharge(ChargeDTO chargeDTO);

}
