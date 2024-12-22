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
