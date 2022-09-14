package club.bigtian.notice.service.impl;

import club.bigtian.notice.config.ExceptionNoticeConfig;
import club.bigtian.notice.constant.SystemConstant;
import club.bigtian.notice.domain.TExceptionInfo;
import club.bigtian.notice.service.INoticeService;
import club.bigtian.notice.utils.RequestUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.taobao.api.ApiException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author bigtian
 * @Description: 钉钉消息发送类
 * @date 2022/9/1210:38
 */
@Service
@Primary
@ConditionalOnProperty(value = "notice.dingtalk.secret")
public class DingTalkServiceImpl implements INoticeService {
    @Autowired
    private ExceptionNoticeConfig config;

    @Autowired
    private ApplicationContext context;
    private static String activeProfile;

    private static String urlPrefix;

    @Override
    public void sendMessage(TExceptionInfo info, List<String> list) {
        sendErrorMd(info.getId(), list);
    }


    @PostConstruct
    public void init() {
        ExceptionNoticeConfig.DingTalkConfig dingtalk = config.getDingtalk();
        Assert.notNull(dingtalk, "请配置钉钉相关配置");
        Assert.notBlank(dingtalk.getToken(), "请配置机器人token");
        Assert.notNull(dingtalk.getSecret(), "请配置机器人密钥");
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
     * 加密
     *
     * @param timestamp 时间戳
     * @return void
     * @author bigtian
     * @since 6.0
     */

    public String signature(Long timestamp) {
        try {
            String secret = config.getDingtalk().getSecret();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            return sign;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送md消息
     *
     * @author bigtian
     * @since 6.0
     */

    public void sendErrorMd(Long id, List<String> list) {
        try {
            DingTalkClient client = getDingTalkClient();
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            OapiRobotSendRequest.At at = setAtList(list);
            request.setAt(at);
            String users = assembleUsers(at);
            String title = config.getTitle();
            request.setMsgtype("markdown");
            OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
            markdown.setTitle(title);
            String contents = StrUtil.format("## <font style='color:red'>{}</font>\n\n" +
                            "> 项目名称：{}\n\n " +
                            "> 处理人：{}\n\n " +
                            "> ###### 产生时间：{} \n\n" +
                            "> 环境：<font color='red'> {} </font> \n\n" +
                            "> 异常：[查看详情]({}) \n\n"
                    , title, config.getProjectName(), users, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"), config.getEnvs().get(activeProfile), urlPrefix + id);
            markdown.setText(contents);
            request.setMarkdown(markdown);
            client.execute(request);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private DingTalkClient getDingTalkClient() {
        if (StrUtil.isBlank(urlPrefix)) {
            HttpServletRequest request = RequestUtils.getRequest();
            urlPrefix = request.getRequestURL().toString().replace(request.getRequestURI(), "") + SystemConstant.URL;
        }
        Long timestamp = System.currentTimeMillis();
        String token = config.getDingtalk().getToken();
        String signature = signature(timestamp);
        DingTalkClient client = new DefaultDingTalkClient(StrUtil.format(SystemConstant.DING_TALK_URL, token, timestamp, signature));
        return client;
    }


    @Override
    public void sendHandledMessage(TExceptionInfo info) {
        try {
            DingTalkClient client = getDingTalkClient();
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            OapiRobotSendRequest.At at = setAtList();
            request.setAt(at);
            String users = assembleUsers(at);
            String title = config.getSuccessTitle();
            request.setMsgtype("markdown");
            OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
            markdown.setTitle(title);
            String contents = StrUtil.format("## <font style='color:green'>{}</font>\n\n" +
                            "> 项目名称：{}\n\n " +
                            "> 异常编号：{}\n\n " +
                            "> 处理人：{}\n\n " +
                            "> 审查人：{}\n\n " +
                            "> 修复时间：{} \n\n" +
                            "> 环境：<font color='green'> {} </font> \n\n" +
                            "> 导致原因：\r\n{}\n\n" +
                            "> 异常堆栈：[查看详情]({}) \n\n"
                    , title, config.getProjectName(), info.getId(), info.getHandledMan(), users, DateUtil.format(info.getHandledTime(), "yyyy-MM-dd HH:mm:ss"), config.getEnvs().get(activeProfile), info.getCause(), urlPrefix + info.getId());
            markdown.setText(contents);
            request.setMarkdown(markdown);
            client.execute(request);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 异常处理时，要么艾特项目管理，要么就是at全体
     *
     * @return
     * @author bigtian
     * @since 6.0
     */

    private OapiRobotSendRequest.At setAtList() {
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        String[] managers = Optional.ofNullable(config.getManagers())
                .map(el -> el.split(","))
                .orElse(new String[0]);
        Set<String> userIds = new HashSet<>();
        //项目管理
        if (ArrayUtil.isEmpty(managers) || config.isAtAll()) {
            at.setIsAtAll(true);
            return at;
        }
        userIds.addAll(Arrays.asList(managers));
        at.setAtUserIds(CollUtil.newArrayList(userIds));
        return at;
    }

    /**
     * 异常产生时设置艾特的对象
     *
     * @return
     * @author bigtian
     * @since 6.0
     */
    private OapiRobotSendRequest.At setAtList(List<String> authorList) {
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        String[] managers = Optional.ofNullable(config.getManagers())
                .map(el -> el.split(","))
                .orElse(new String[0]);
        Map<String, String> developers = config.getDevelopers();
        //如果配置中是at全体成员或者是开发列表为空和项目管理者为空，默认为at所有人
        if (config.isAtAll()
                || ArrayUtil.isEmpty(managers) && CollUtil.isEmpty(developers)
                || CollUtil.isEmpty(authorList)) {
            at.setIsAtAll(true);
            return at;
        }
        Set<String> userIds = new HashSet<>();
        //项目管理
        if (ArrayUtil.isNotEmpty(managers)) {
            userIds.addAll(Arrays.asList(managers));
        }
        if (CollUtil.isNotEmpty(authorList) && CollUtil.isNotEmpty(developers)) {
            for (String develAlias : authorList) {
                String author = developers.get(develAlias);
                if (StrUtil.isNotBlank(author)) {
                    userIds.add(author);
                }
            }
        }
        at.setAtUserIds(CollUtil.newArrayList(userIds));
        return at;
    }

    /**
     * 组装at人员数据
     *
     * @param at
     * @return
     * @author bigtian
     * @since 6.0
     */
    public String assembleUsers(OapiRobotSendRequest.At at) {
        Boolean isAtAll = Optional.ofNullable(at.getIsAtAll()).orElse(false);
        if (isAtAll) {
            return "@全体人员";
        }
        List<String> atUserIds = at.getAtUserIds();
        StringBuilder builder = new StringBuilder();
        if (CollUtil.isNotEmpty(atUserIds)) {
            for (String atUserId : atUserIds) {
                builder.append("@");
                builder.append(atUserId);
            }
        }
        return builder.toString();
    }


}
