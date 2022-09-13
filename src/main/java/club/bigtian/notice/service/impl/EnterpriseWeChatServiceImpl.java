package club.bigtian.notice.service.impl;

import club.bigtian.notice.domain.TExceptionInfo;
import club.bigtian.notice.service.INoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bigtian
 * @Description: 企业微信消息发送
 * @date 2022/9/1210:39
 */
@Service
public class EnterpriseWeChatServiceImpl implements INoticeService {
    @Override
    public void sendMessage(TExceptionInfo info, List<String> list) {

    }

    @Override
    public void sendHandledMessage(TExceptionInfo info) {

    }
}
