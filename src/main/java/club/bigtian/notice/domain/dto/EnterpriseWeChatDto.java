package club.bigtian.notice.domain.dto;

import lombok.Data;

/**
 * @author bigtian
 * @Description: 企业微信数据传输
 * @date 2022/9/1321:11
 */
@Data
public class EnterpriseWeChatDto {
    private String msgtype;
    private Markdown markdown;

    @Data
    public static class Markdown {
        private String content;
    }
}
