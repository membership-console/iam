package cc.rits.membership.console.iam.annotation;

import java.lang.annotation.*;

/**
 * Swaggerで非表示するパラメータ
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SwaggerHiddenParameter {
}
