package club.bigtian.notice.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author bigtian
 * @Description:
 * @date 2022/9/216:41
 */
public class ExceptionUtils {
    /**
     * 获取异常的堆栈信息
     *
     * @param e
     * @return
     * @author bigtian
     * @since 6.0
     */
    public static String getExceptionMsg(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
