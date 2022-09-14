package club.bigtian.notice.anno;

import java.lang.annotation.*;

/**
 * @author bigtian
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoticeMessage {

    String[] author() default "";
}
