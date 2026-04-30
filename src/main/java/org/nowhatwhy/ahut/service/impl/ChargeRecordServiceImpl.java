package org.nowhatwhy.ahut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.entity.ChargeRecord;
import org.nowhatwhy.ahut.mapper.ChargeRecordMapper;
import org.nowhatwhy.ahut.service.IChargeRecordService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChargeRecordServiceImpl extends ServiceImpl<ChargeRecordMapper, ChargeRecord> implements IChargeRecordService {

}
