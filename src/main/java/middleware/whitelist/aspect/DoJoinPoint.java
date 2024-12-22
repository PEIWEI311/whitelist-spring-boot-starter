package middleware.whitelist.aspect;

import middleware.whitelist.annotation.WhiteListCheck;
import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AOP aspect that intercepts methods annotated with @WhiteListCheck,
 * checks if a specified key is in the white-list, and returns either
 * the original method result or a custom JSON object if not in the list.
 */
@Aspect
@Component
public class DoJoinPoint {

    private final Logger logger = LoggerFactory.getLogger(DoJoinPoint.class);

    /**
     * "whiteListConfig" is a comma-separated string, e.g. "zhangsan,lisi,wangwu".
     * It's injected from WhiteListAutoConfigure.
     */
    @Resource
    private String whiteListConfig;

    /**
     * Defines the pointcut: intercept any method annotated with @WhiteListCheck.
     */
    @Pointcut("@annotation(middleware.whitelist.annotation.WhiteListCheck)")
    public void aopPoint() {}

    /**
     * Around advice: runs before and after the target method.
     * Here we perform the white-list check logic.
     */
    @Around("aopPoint()")
    public Object doRouter(ProceedingJoinPoint jp) throws Throwable {
        // 1. Retrieve the method and the @WhiteListCheck annotation
        Method method = getMethod(jp);
        WhiteListCheck whiteList = method.getAnnotation(WhiteListCheck.class);

        // 2. Extract the key value (e.g. userId) from method arguments
        String keyValue = getFieldValue(whiteList.key(), jp.getArgs());
        logger.info("[DoJoinPoint] method: {}, extracted keyValue: {}", method.getName(), keyValue);

        // 3. If there's no value found, let the method proceed
        if (keyValue == null || "".equals(keyValue)) {
            return jp.proceed();
        }

        // 4. Compare the key value against the white-list
        String[] whiteArray = whiteListConfig.split(",");
        for (String w : whiteArray) {
            if (keyValue.equals(w)) {
                // If matched, proceed with the original method
                return jp.proceed();
            }
        }

        // 5. If not in the white-list, return the custom JSON/object
        return returnObject(whiteList, method);
    }

    /**
     * Helper: get the target Method via reflection
     */
    private Method getMethod(ProceedingJoinPoint jp) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        return jp.getTarget().getClass()
                .getMethod(signature.getName(), signature.getParameterTypes());
    }

    /**
     * If not in the white-list, we either return a new instance of the return type
     * or parse the returnJson from the annotation.
     */
    private Object returnObject(WhiteListCheck whiteList, Method method)
            throws IllegalAccessException, InstantiationException {
        Class<?> returnType = method.getReturnType();
        String returnJson = whiteList.returnJson();

        // If there's no returnJson specified, create an empty instance
        if ("".equals(returnJson)) {
            return returnType.newInstance();
        }
        // Otherwise parse the JSON string into the return type
        return JSON.parseObject(returnJson, returnType);
    }

    /**
     * Try to get a specified field (by name) from the method arguments.
     */
    private String getFieldValue(String field, Object[] args) {
        if (field == null || "".equals(field.trim())) {
            return null;
        }
        String fieldValue = null;
        for (Object arg : args) {
            if (arg == null) continue;
            try {
                // Use BeanUtils to get the property
                fieldValue = BeanUtils.getProperty(arg, field);
                if (fieldValue != null && !"".equals(fieldValue)) {
                    break;
                }
            } catch (Exception e) {
                // If there's only one argument and it's not a bean, fallback to toString()
                if (args.length == 1) {
                    return String.valueOf(arg);
                }
            }
        }
        return fieldValue;
    }
}

