### 快速开始

1、执行sql文件，可以在源码的docs下载sql文件

```sql
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
```

2、引入依赖 [查看最新的依赖](https://search.maven.org/artifact/club.bigtian/notice-spring-boot-starter)

```xml
<dependency>
  <groupId>club.bigtian</groupId>
  <artifactId>notice-spring-boot-starter</artifactId>
  <version>最新的版本号</version>
</dependency>
```

3、配置spring boot的yaml文件

```yaml
notice:
  dingtalk: #如果是配置钉钉消息推送，则需要配置加密和token
    secret:  #目前只支持群机器人为加密方式
    token:  #机器人token
  envs: #配置该项是为了在钉钉群中区分异常属于那个环境
    dev: 开发环境 #默认第一个为开发环境
    test: 测试环境
    prod: 正式环境
  project-name: 演示系统 #项目名称
  enterprise-we-chat: #如果配置企业微信发送
    key: #企业微信webhook key
  headers: #需要存放到数据库中的请求头数据
    - authorization
  managers: DaiJunXiong #配置需要发送的userId，钉钉和企业微信有区别，获取方式请往下看
  developers:
    bigtian: DaiJunXiong #后台开发者别名与钉钉/企业微信userId关联起来，方便单独艾特
  exclude-exception:  #需要排除的异常，排除的异常不再进行消息推送
  excludePacket: #需要排除的异常包路径，属于改包下面的所有异常都不会再进行异常消息推送
  title: 异常信息提醒 #异常提醒的标题，默认是（异常信息提醒）
  successTitle: 异常处理提醒 #异常被处理的标题，默认是（异常处理提醒）
  atAll: false #是否艾特全体成员（只有发送消息为钉钉的时候有效），默认为false，配置为true后，产生异常和处理异常都是艾特全体成员
  timeout: 5 #默认同一个接口5s内不会再次发送相同的消息通知
```

