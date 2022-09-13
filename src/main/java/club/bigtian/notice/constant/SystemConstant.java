package club.bigtian.notice.constant;

/**
 * @author bigtian
 * @Description: 常量类
 * @date 2022/9/1210:43
 */
public class SystemConstant {
    /**
     * 未处理
     */
    public static final String STATUS_N = "N";

    /**
     * 已处理
     */
    public static final String STATUS_Y = "Y";

    /**
     * 详情url
     */
    public static final String URL = "/exception/selectOne?id=";

    /**
     * 需要监听的包路径,默认监听RestController、Controller标注的类
     */
    public static final String PACKET = "@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)";

    /**
     * 组数
     */
    public static final String GROUP_COUNT = "groupCount";
    /**
     * 数
     */
    public static final String COUNT = "count";
    /**
     * 限制
     */
    public static final String LIMIT = "limit";
    /**
     * 页面
     */
    public static final String PAGE = "page";
    /**
     * 处理
     */
    public static final String HANDLED = "handled";
    /**
     * 列表
     */
    public static final String LIST = "list";
    /**
     * 数据
     */
    public static final String DATA = "data";
    /**
     * 返回code
     */
    public static final String CODE = "code";
    /**
     * 错信息
     */
    public static final String ERR_INFO = "errInfo";
    /**
     * id
     */
    public static final String ID = "id";
    /**
     * 状态
     */
    public static final String STATUS = "status";
    /**
     * 页面：错误信息
     */
    public static final String ERROR_INFO = "errorInfo";
    /**
     * 页面：错误列表
     */
    public static final String ERROR_LIST = "errorList";
}
