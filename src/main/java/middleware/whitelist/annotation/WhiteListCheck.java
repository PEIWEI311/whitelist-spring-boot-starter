package middleware.whitelist.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used for marking methods that require white-list checking.
 * The value of the specified key (method parameter name) will be verified
 * against the configured white-list. If it is not in the list,
 * a specified JSON result will be returned.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WhiteListCheck {

    /**
     * The parameter name in the method signature to be used for white-list checking.
     * For example: userId, account, etc.
     */
    String key() default "";

    /**
     * If the parameter value is not in the white-list,
     * this JSON string will be returned to the caller.
     * For example: {"code":"403","msg":"Access Denied"}
     */
    String returnJson() default "{\"code\":\"403\",\"msg\":\"Access Denied\"}";
}

