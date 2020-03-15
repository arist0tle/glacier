package com.geektcp.alpha.glacier.server.annotation;

import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.*;

/**
 * @author tanghaiyang on 2020/1/2 1:18.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Value("${" + RpcPort.propertyName + "}")
public @interface RpcPort {
    String propertyName = "grpc.server.port";
}
