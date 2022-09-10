package club.bigtian.notice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: ${description}
 * @author bigtian
 * @date 2022/9/215:22
 */

/**
 * 异常信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TExceptionInfo implements Serializable {
    private Long id;

    /**
     * 请求url
     */
    private String url;

    /**
     * 异常信息堆栈
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否已处理（N:未处理，Y已处理）
     */
    private String handled;

    /**
     * 处理时间
     */
    private Date handledTime;

    /**
     * 处理人
     */
    private String handledMan;

    /**
     * 导致原因
     */
    private String cause;

    private static final long serialVersionUID = 1L;
}