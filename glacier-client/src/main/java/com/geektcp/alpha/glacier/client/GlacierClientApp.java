package com.geektcp.alpha.glacier.client;

import com.geektcp.alpha.glacier.client.autoconfig.RpcProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author tanghaiyang on 2020/1/2 1:18.
 */
@EnableConfigurationProperties({
        RpcProperties.class
})
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class GlacierClientApp {

    public static void main(String[] args) {
        SpringApplication.run(GlacierClientApp.class, args);
    }
}
