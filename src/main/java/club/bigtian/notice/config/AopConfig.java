package club.bigtian.notice.config;

import club.bigtian.notice.aop.ExceptionAdvice;
import cn.hutool.core.lang.Assert;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bigtian
 * @Description:
 * @date 2022/9/412:36
 */
@Configuration
public class AopConfig {
    @Autowired
    private DingTalkConfig config;
    @Autowired
    private ExceptionAdvice exceptionAdvice;

    @Bean
    public AspectJExpressionPointcutAdvisor configurabledvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        String packet = config.getPacket();
        Assert.notBlank(packet, "请配置需要切入的aop表达式");
        advisor.setExpression(packet);
        advisor.setAdvice(exceptionAdvice);
        return advisor;

    }
}
