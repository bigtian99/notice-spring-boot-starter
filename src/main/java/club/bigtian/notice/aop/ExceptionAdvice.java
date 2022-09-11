package club.bigtian.notice.aop;

import club.bigtian.notice.anno.DingTalk;
import club.bigtian.notice.domain.TExceptionInfo;
import club.bigtian.notice.service.TExceptionInfoService;
import club.bigtian.notice.utils.DingTalkUtil;
import club.bigtian.notice.utils.ExceptionUtils;
import club.bigtian.notice.utils.RequestUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author bigtian
 */
@Component
public class ExceptionAdvice implements MethodInterceptor {
    @Autowired
    private TExceptionInfoService exceptionInfoService;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = null;
        try {
            result = invocation.proceed();
        } catch (Throwable e) {
            //发送钉钉信息
            saveExceptionInfo(e, invocation.getMethod());
            throw e;
        }
        return result;

    }

    /**
     * 保存错误信息到数据库
     *
     * @author bigtian
     * @since 6.0
     */
    private void saveExceptionInfo(Throwable e, Method method) {
        TExceptionInfo info = TExceptionInfo.builder()
                .url(RequestUtils.getRequest().getRequestURI())
                .createTime(new Date())
                .handled("N")
                .content(ExceptionUtils.getExceptionMsg(e))
                .build();
        exceptionInfoService.insert(info);
        sendDingTalk(info.getId(), getAuthorList(method));
    }

    /**
     * 获取作者列表
     *
     * @param method
     * @author bigtian
     * @since 6.0
     * @return List<String>
     */
    public static List<String> getAuthorList( Method method ) {
        List<String> list = new ArrayList<>();
        DingTalk dingTalk = method.getAnnotation(DingTalk.class);
        if (Objects.nonNull(dingTalk)) {
            list.addAll(Arrays.asList(dingTalk.author()));
        }
        return list;
    }

    /**
     * 发送钉钉消息
     *
     * @param
     * @author bigtian
     * @since 6.0
     */
    private static void sendDingTalk(Long id, List<String>list) {

        DingTalkUtil.sendErrorMd(id, list);
    }
}