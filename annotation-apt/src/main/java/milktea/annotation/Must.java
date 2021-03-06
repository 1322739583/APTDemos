package milktea.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Builder是否必选属性
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Must {

}
