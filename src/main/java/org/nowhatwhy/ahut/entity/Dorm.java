package org.nowhatwhy.ahut.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@TableName("dorm")
public class Dorm {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long buildingPk;

    private String roomNo;

    private String electricityType;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
