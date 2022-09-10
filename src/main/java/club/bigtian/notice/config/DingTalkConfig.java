package club.bigtian.notice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

/**
 * @author bigtian
 * @Description:
 * @date 2022/9/214:11
 */
@ConfigurationProperties(prefix = "bigtian")
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
     * 需要监听的包路径
     */
    private String packet;

    /**
     * 项目名称，用于区分多个项目集成了，钉钉群是一个
     */
    private String projectName;
}
