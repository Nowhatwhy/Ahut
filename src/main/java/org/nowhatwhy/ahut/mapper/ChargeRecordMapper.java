package org.nowhatwhy.ahut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nowhatwhy.ahut.dto.ChargeDTO;
import org.nowhatwhy.ahut.entity.ChargeRecord;
import org.nowhatwhy.ahut.vo.ChargeRecordVO;

import java.util.List;

@Mapper
public interface ChargeRecordMapper extends BaseMapper<ChargeRecord> {
    List<ChargeDTO> listChargeDTO();
    void saveChargeRecords(@Param("list") List<ChargeRecord> list);

    List<ChargeRecordVO> listChargeRecords(Long userId);
}
