package com.geektcp.alpha.glacier.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * @author tanghaiyang on 2020/1/2 1:18.
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class GlacierServerApp {

    public static void main(String[] args) {
        SpringApplication.run(GlacierServerApp.class, args);
    }
}
