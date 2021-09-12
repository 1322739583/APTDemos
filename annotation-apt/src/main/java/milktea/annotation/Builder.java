package milktea.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Builder设计模式
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Builder {
   String name() default "";
}
