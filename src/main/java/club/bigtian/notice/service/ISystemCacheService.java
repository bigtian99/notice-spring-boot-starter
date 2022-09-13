package club.bigtian.notice.service;


/**
 * isystem缓存服务
 * @author bigtian
 * @date 2022/09/12
 */
public interface ISystemCacheService {


    /**
     * 是否在配置的时间内
     * @return
     * @author bigtian
     * @since 6.0
     */
    boolean expirationTime();

    /**
     * 加入到缓存
     * @author bigtian
     * @since 6.0
     */
    String  putVal();
}
