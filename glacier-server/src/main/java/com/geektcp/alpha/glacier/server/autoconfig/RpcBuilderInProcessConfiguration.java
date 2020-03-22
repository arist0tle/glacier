package com.geektcp.alpha.glacier.server.autoconfig;

import com.geektcp.alpha.glacier.server.annotation.RpcService;
import io.grpc.ServerBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author tanghaiyang on 2020/1/2 1:18.
 */
@Slf4j
@Configuration
@ConditionalOnBean(annotation = RpcService.class)
@EnableConfigurationProperties(RpcProperties.class)
public class RpcBuilderInProcessConfiguration {

    @Autowired
    private RpcProperties serverProperties;

    @Bean
    @ConditionalOnProperty(value = "rpc.server.model", havingValue = RpcProperties.SERVER_MODEL_IN_PROCESS)
    public ServerBuilder getServerBuilder() {
        ServerBuilder serverBuilder;

        String inProcessServerName = serverProperties.getInProcessServerName();

        if (!StringUtils.hasText(inProcessServerName)) {
            log.error("please config required property [inProcessServerName] for InProcess model");
            throw new RuntimeException("Failed to create inProcessServer");
        }

        log.warn("gRPC Server will run in InProcess model. Please only use in testing");
        serverBuilder = InProcessServerBuilder.forName(inProcessServerName);

        return serverBuilder;
    }

}
