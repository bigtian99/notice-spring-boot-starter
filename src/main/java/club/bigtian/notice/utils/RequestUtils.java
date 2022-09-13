package club.bigtian.notice.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author bigtian
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

    /**
     * 获取请求的路径
     *
     * @return
     * @author bigtian
     * @since 6.0
     */
    public static String getRequestPath() {
        return getRequest().getRequestURI();
    }


}
