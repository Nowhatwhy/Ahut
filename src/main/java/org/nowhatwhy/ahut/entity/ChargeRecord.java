package org.nowhatwhy.ahut.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("charge_record")
public class ChargeRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String roomId;

    private Double balance;

    private LocalDate recordTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
}
