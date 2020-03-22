package com.geektcp.alpha.glacier.server.context;

import com.geektcp.alpha.glacier.server.annotation.RpcPort;
import com.geektcp.alpha.glacier.server.autoconfig.RpcProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.SocketUtils;

import java.util.Properties;

/**
 * @author tanghaiyang on 2020/1/2 1:18.
 */
public class RpcEnvironment implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources sources = environment.getPropertySources();
        Properties properties = new Properties();
        Integer configuredPort = environment.getProperty("rpc.server.port", Integer.class);

        if (null == configuredPort) {
            properties.put(RpcPort.propertyName, RpcProperties.DEFAULT_SERVER_PORT);
        } else if (0 == configuredPort) {
            properties.put(RpcPort.propertyName, SocketUtils.findAvailableTcpPort());
        } else {
            properties.put(RpcPort.propertyName, configuredPort);
        }

        sources.addLast(new PropertiesPropertySource("rpc", properties));
    }
}
