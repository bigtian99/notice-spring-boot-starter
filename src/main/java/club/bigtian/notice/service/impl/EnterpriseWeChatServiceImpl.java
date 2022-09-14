package club.bigtian.notice.service.impl;

import club.bigtian.notice.config.ExceptionNoticeConfig;
import club.bigtian.notice.constant.SystemConstant;
import club.bigtian.notice.domain.TExceptionInfo;
import club.bigtian.notice.domain.dto.EnterpriseWeChatDto;
import club.bigtian.notice.service.INoticeService;
import club.bigtian.notice.utils.RequestUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author bigtian
 * @Description: 企业微信消息发送
 * @date 2022/9/1210:39
 */
@Service
@Slf4j
public class EnterpriseWeChatServiceImpl implements INoticeService {
    @Autowired
    private ExceptionNoticeConfig config;
    @Autowired
    private ApplicationContext context;
    private static String activeProfile;

    private static String urlPrefix;

    @PostConstruct
    public void init() {
        ExceptionNoticeConfig.EnterpriseWeChat enterpriseWeChat = config.getEnterpriseWeChat();
        Assert.notNull(enterpriseWeChat, "请配置企业微信相关配置");
        Assert.notNull(config.getProjectName(), "请配置项目名");
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        if (ArrayUtil.isEmpty(activeProfiles)) {
            Map<String, String> envs = config.getEnvs();
            Assert.isTrue(CollUtil.isNotEmpty(envs), "请配置环境");
            for (String value : envs.keySet()) {
                //默认第一个为开发环境
                activeProfile = value;
                return;
            }
        }
        activeProfile = activeProfiles[0];
    }


    /**
     * 发送消息
     *
     * @param info 信息
     * @param list 开发者列表
     */
    @Override
    public void sendMessage(TExceptionInfo info, List<String> list) {
        if (StrUtil.isBlank(urlPrefix)) {
            HttpServletRequest request = RequestUtils.getRequest();
            urlPrefix = request.getRequestURL().toString().replace(request.getRequestURI(), "") + SystemConstant.URL;
        }
        list.addAll(getMangerList());
        String users = assembleUsers(list);
        String contents = StrUtil.format("## <font color=\"red\">{}</font>\n\n" +
                        "> 项目名称：{}\n\n " +
                        "> 处理人：{}\n\n " +
                        "> 产生时间：{} \n\n" +
                        "> 环境：<font color='red'> {} </font> \n\n" +
                        "> 异常：[查看详情]({}) \n\n"
                , config.getTitle(), config.getProjectName(), users, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"), config.getEnvs().get(activeProfile), urlPrefix + info.getId());

        sendNotice(contents);
    }


    /**
     * 发送消息
     *
     * @param contents
     * @author bigtian
     * @since 6.0
     */
    private void sendNotice(String contents) {
        EnterpriseWeChatDto chatDto = new EnterpriseWeChatDto();
        chatDto.setMsgtype(SystemConstant.MARKDOWN);
        EnterpriseWeChatDto.Markdown markdown = new EnterpriseWeChatDto.Markdown();
        markdown.setContent(contents);
        chatDto.setMarkdown(markdown);
        String url = StrUtil.format(SystemConstant.ENTERPRISE_WECHAT_URL, config.getEnterpriseWeChat().getKey());
        HttpResponse execute = HttpRequest.post(url)
                .body(JSON.toJSONString(chatDto))
                .execute();
        JSONObject result = JSON.parseObject(execute.body());
        if (result.getInteger(SystemConstant.ERR_CODE) == 0) {
            log.info("消息通知发送成功");
            return;
        }
        log.error("消息通知发送失败");
    }

    @Override
    public void sendHandledMessage(TExceptionInfo info) {
        if (StrUtil.isBlank(urlPrefix)) {
            HttpServletRequest request = RequestUtils.getRequest();
            urlPrefix = request.getRequestURL().toString().replace(request.getRequestURI(), "") + SystemConstant.URL;
        }
        String users = assembleUsers(getMangerList());
        String contents = StrUtil.format("## <font color=\"green\">{}</font>\n\n" +
                        "> 项目名称：{}\n\n " +
                        "> 异常编号：{}\n\n " +
                        "> 处理人：{}\n\n " +
                        "> 审查人：{}\n\n " +
                        "> 修复时间：{} \n\n" +
                        "> 导致原因：{} \n\n" +
                        "> 异常堆栈：[查看详情]({}) \n\n"
                , config.getSuccessTitle(), config.getProjectName(), info.getId(), info.getHandledMan(), users, DateUtil.format(info.getHandledTime(), "yyyy-MM-dd HH:mm:ss"), info.getCause(), urlPrefix + info.getId());
        sendNotice(contents);
    }

    /**
     * 组装at人员数据
     *
     * @return
     * @author bigtian
     * @since 6.0
     */
    public String assembleUsers(List<String> list) {
        Map<String, String> developers = config.getDevelopers();
        StringBuilder builder = new StringBuilder();
        if (CollUtil.isNotEmpty(list) && CollUtil.isNotEmpty(developers)) {
            for (String atUserId : new HashSet<>(list)) {
                builder.append("<@");
                builder.append(developers.getOrDefault(atUserId, atUserId));
                builder.append(">");
            }
        } else {
            //如果atList为空处理人则是全体人员
            if (CollUtil.isNotEmpty(developers)) {
                for (String userId : developers.values()) {
                    builder.append("<@");
                    builder.append(userId);
                    builder.append(">");
                }
            } else {
                builder.append("@全体人员");
            }
        }
        return builder.toString();
    }

    /**
     * 艾特项目管理
     *
     * @return
     * @author bigtian
     * @since 6.0
     */

    private List<String> getMangerList() {
        return Optional.ofNullable(config.getManagers())
                .map(el -> Arrays.asList(el.split(",")))
                .orElse(new ArrayList<>());
    }

}
