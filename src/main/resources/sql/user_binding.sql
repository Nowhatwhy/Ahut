CREATE TABLE user_binding (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',

                              user_id BIGINT NOT NULL COMMENT '用户ID',
                              dorm_id BIGINT NOT NULL COMMENT '宿舍ID',

                              create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                              UNIQUE KEY uk_user (user_id),
                              INDEX idx_dorm (dorm_id)
) COMMENT='用户绑定宿舍';