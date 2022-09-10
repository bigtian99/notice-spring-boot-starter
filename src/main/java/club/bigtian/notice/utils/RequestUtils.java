package club.bigtian.notice.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author bigtian
 * @Description:
 * @date 2022/9/216:35
 */
public class RequestUtils {

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = getRequestAttr();
        return Optional.ofNullable(attributes).
                map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    /**
     * 获取请求属性
     *
     * @return
     */
    public static ServletRequestAttributes getRequestAttr() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }


}
