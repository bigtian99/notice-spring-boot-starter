package club.bigtian.notice.config;

import club.bigtian.notice.aop.ExceptionAdvice;
import club.bigtian.notice.mapper.TExceptionInfoMapper;
import club.bigtian.notice.service.TExceptionInfoService;
import club.bigtian.notice.service.impl.TExceptionInfoServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author bigtian
 * @Description: 钉钉消息通知，自动配置类
 * @date 2022/9/1010:37
 */
@Configuration
@ComponentScan(basePackages = {"club.bigtian.notice.*","club.bigtian.notice.mapper"})
@MapperScan(basePackages = {"club.bigtian.notice.mapper"})
public class DingTalkAutoConfiguration {




}
