create table t_exception_info
(
    id           bigint auto_increment
        primary key,
    url          varchar(100)            not null comment '请求url',
    content      longtext                not null comment '异常信息堆栈',
    create_time  datetime                null comment '创建时间',
    handled      varchar(10) default 'N' null comment '是否已处理（N:未处理，Y已处理）',
    handled_time datetime                null comment '处理时间',
    handled_man  varchar(20)             null comment '处理人',
    cause        text                    null comment '导致原因',
    params       longtext                null comment '请求参数',
    headers      text                    null comment '请求头'
)
    comment '异常信息';
