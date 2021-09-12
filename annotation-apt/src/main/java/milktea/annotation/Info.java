package milktea.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于测试Processor所有方法的复杂例子，见FlexModel类
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Info {
}
