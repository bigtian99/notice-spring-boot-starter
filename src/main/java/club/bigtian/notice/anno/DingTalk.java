package club.bigtian.notice.anno;

import java.lang.annotation.*;

/**
 * @author bigtian
 * @Description:
 * @date 2022/9/322:05
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DingTalk {

    String[] author() default "";
}
