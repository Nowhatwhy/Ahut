-- auto-generated definition
create table user
(
    id          bigint auto_increment comment '主键'
        primary key,
    qq          varchar(32)                        not null comment 'QQ号',
    username    varchar(64)                        null comment '用户名',
    password    varchar(255)                       null comment '密码',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_qq
        unique (qq)
)
    comment '用户表';

