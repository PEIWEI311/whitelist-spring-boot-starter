# Whitelist Spring Boot Starter

A simple Spring Boot Starter for **method-level whitelist checks**. When a method is annotated with `@WhiteListCheck`, the aspect will extract a specified field from the method parameters and compare it against a configured whitelist. If the value is not in the list, a custom JSON result will be returned.

---

## Features

1. **Method-Level Annotation**  
   Only methods annotated with `@WhiteListCheck` are intercepted.

2. **Field Extraction**  
   Dynamically extracts a specified field (e.g., `userId`) from the method parameters.

3. **Conditional Response**  
   If the field value is not in the whitelist, returns a custom JSON string (defined in the annotation).

4. **Easy Configuration**  
   Define your whitelist in `application.yml` or `application.properties`.

5. **Auto-Configuration**  
   Simply include this starter in your project; it will be auto-configured through `spring.factories`.

---

## Getting Started

### 1. Add the Dependency

If you have deployed this starter to a Maven repository, add the following to your **Maven** `pom.xml`:

```xml
<dependency>
    <groupId>middleware</groupId>
    <artifactId>whitelist-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>

```

### 2. Configure the Whitelist

In your `application.yml` (or `application.properties`), define the comma-separated list of allowed identifiers:

```java
ritszg:
  whitelist:
    users: "chen,lee,wang"
```

You can add or remove user IDs as needed. The property prefix is **`bugstack.whitelist.users`**.

### 3. Annotate Your Method

Use the `@WhiteListCheck` annotation to indicate which parameter to check:

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/info")
    @WhiteListCheck(key = "userId", returnJson="{\"code\":403,\"msg\":\"Forbidden\"}")
    public UserInfo getUserInfo(String userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setNickname("Test User");
        return userInfo;
    }
}

```

- **`key`**: The field name to extract from the method parameters (e.g., `"userId"`).
- **`returnJson`**: The JSON returned if the extracted value is not found in the whitelist. Defaults to `{"code":403,"msg":"Access Denied"}` if not specified.

## How It Works

1. **Auto-Configuration**
   - `WhiteListAutoConfigure` is loaded by Spring Boot (via `META-INF/spring.factories`).
   - It registers two main beans:
     - **`whiteListConfig`**: Stores your comma-separated whitelist.
     - **`DoJoinPoint`**: The AOP aspect that enforces the whitelist logic.
2. **Aspect Interception**
   - Methods annotated with `@WhiteListCheck` trigger the aspect.
   - Before executing the target method, it reads the annotation’s `key`, extracts the parameter value using BeanUtils, and compares it to `whiteListConfig`.
   - If it matches, the original method proceeds. Otherwise, it returns the `returnJson` content.
3. **Result**
   - Clients calling whitelisted methods see normal results.
   - Non-whitelisted calls get the custom JSON (e.g., `{"code":403,"msg":"Forbidden"}`), effectively blocking unauthorized access.
