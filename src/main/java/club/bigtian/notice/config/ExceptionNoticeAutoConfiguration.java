package club.bigtian.notice.config;

import club.bigtian.notice.service.ISystemCacheService;
import club.bigtian.notice.service.impl.ISystemCacheServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author bigtian
 */
@Configuration
@ComponentScan(basePackages = {"club.bigtian.notice.*"})
@MapperScan(basePackages = {"club.bigtian.notice.mapper"})
public class ExceptionNoticeAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(ISystemCacheService.class)
    public ISystemCacheServiceImpl systemCacheService() {
        return new ISystemCacheServiceImpl();
    }

}
