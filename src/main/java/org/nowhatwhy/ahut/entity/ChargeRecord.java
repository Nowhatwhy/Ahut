package org.nowhatwhy.ahut.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@TableName("charge_record")
public class ChargeRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long dormId;

    private BigDecimal balance;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDate recordDate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
