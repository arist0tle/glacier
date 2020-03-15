package com.geektcp.alpha.glacier.server.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tanghaiyang on 2020/1/2 1:18.
 */
@Data
@ConfigurationProperties(prefix = "grpc.server")
public class RpcProperties {

    public static final int DEFAULT_SERVER_PORT = 50051;

    /**
     * fully-featured, high performance, useful in testing
     */
    public static final String SERVER_MODEL_IN_PROCESS = "inProcess";

    /**
     * plaintext without TLS.
     * While this is convenient for testing environments, users must be aware of the security risks of doing so for real production systems.
     */
    public static final String SERVER_MODEL_SIMPLE = "simple";

    /**
     * service with TLS, safely use to talk to external systems
     */
    public static final String SERVER_MODEL_TLS = "tls";

    /**
     * custom ServerBuilder
     */
    public static final String SERVER_MODEL_CUSTOM = "custom";


    /**
     * gRPC running model, default simple
     */
    private String model = SERVER_MODEL_SIMPLE;

    /**
     * gRPC server host
     */
    private String host;

    /**
     * gRPC server port
     */
    private int port = DEFAULT_SERVER_PORT;

    /**
     * In process server name.
     */
    private String inProcessServerName;

    /**
     * Enables server reflection using <a href="https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md">ProtoReflectionService</a>.
     * Available only from gRPC 1.3 or higher.
     */
    private boolean enableReflection = false;

    /**
     * gRPC tls server certChainFilePath
     */
    private String certChainFilePath;

    /**
     * gRPC tls server privateKeyFilePath
     */
    private String privateKeyFilePath;

    /**
     * gRPC tls server trustCertCollectionFilePath
     */
    private String trustCertCollectionFilePath;

}
