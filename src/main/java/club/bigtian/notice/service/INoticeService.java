package club.bigtian.notice.service;

import club.bigtian.notice.domain.TExceptionInfo;

import java.util.List;

/**
 * @author bigtian
 * @Description:
 * @date 2022/9/1210:38
 */
public interface INoticeService {

    /**
     * 发送系统异常消息
     *
     * @param info
     * @param list
     * @author bigtian
     * @since 6.0
     */
    void sendMessage(TExceptionInfo info, List<String> list);

    /**
     * 发送异常处理消息
     *
     * @param info
     * @author bigtian
     * @since 6.0
     */
    void sendHandledMessage(TExceptionInfo info);
}
