package org.nowhatwhy.ahut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nowhatwhy.ahut.entity.ChargeRecord;
import org.nowhatwhy.ahut.vo.ChargeRecordVO;

import java.util.List;

public interface IChargeRecordService extends IService<ChargeRecord> {

    List<ChargeRecordVO> listCharges();
}
