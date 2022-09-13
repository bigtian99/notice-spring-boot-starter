package club.bigtian.notice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author bigtian
 */
@ConfigurationProperties(prefix = "notice.dingtalk")
@Configuration
@Data
public class DingTalkConfig {
    /**
     * 目前只支持加密方式
     */
    private String secret;

    /**
     * 钉钉机器人token
     */
    private String token;


    /**
     * 异常标题
     */
    private String title = "异常信息提醒";

    private String successTitle = "异常处理提醒";

    /**
     * 是否at全体
     */
    private boolean atAll = false;


    /**
     * 项目管理这的userId,如果配置则默认每次发送异常信息都会at,如果多个请求英文逗号分割
     */
    private String managers;

    /**
     * 环境配置
     */
    Map<String, String> envs;

    /**
     * 人员配置
     */
    Map<String, String> developers;


    /**
     * 项目名称，用于区分多个项目集成了，钉钉群是一个
     */
    private String projectName;

    /**
     * 排除异常路径（可以多个英文逗号隔开）
     */
    private String excludePacket;

    /**
     * 排除的异常（可以多个英文逗号隔开），可以和排除异常路径一起使用
     */
    private String excludeException;

    /**
     * 默认5s内同一个接口异常不会重复发送
     */
    private long timeout = 5;

}
