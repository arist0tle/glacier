package com.geektcp.alpha.glacier.client.runner;

import com.geektcp.alpha.common.base.rpc.GlacierData;
import com.geektcp.alpha.common.base.rpc.GlacierResponse;
import com.geektcp.alpha.common.base.rpc.GlacierServiceGrpc;
import com.geektcp.alpha.glacier.client.autoconfig.RpcProperties;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.services.HealthStatusManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author tanghaiyang on 2020/1/2 1:18.
 */
@Slf4j
public class RpcRunner implements CommandLineRunner, DisposableBean {

    @Autowired
    private RpcProperties rpcProperties;

    @Override
    public void run(String... args) {
        String host = rpcProperties.getHost();
        int port = rpcProperties.getPort();
        String fileDir = rpcProperties.getFileDir();
        log.info("Starting gRPC Server {}:{}",host,port);
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        String resourcePath = System.getProperty("user.dir");
        String filePath = resourcePath + fileDir;
        File srcFile = new File(filePath);
        try (FileInputStream srcFis = new FileInputStream(srcFile)) {
            FileChannel srcFileChannel = srcFis.getChannel();
            GlacierServiceGrpc.GlacierServiceBlockingStub stub = GlacierServiceGrpc.newBlockingStub(channel);
            GlacierData.Builder builder = GlacierData.newBuilder();
            builder.setName("test.zip");
            builder.setStatus(0);
            GlacierResponse locateResponse =  stub.locate(builder.build());
            long startPosition = locateResponse.getPosition();

            int size;
            int len = 20000;
            ByteBuffer buffer = ByteBuffer.allocate(len);

            while (true) {
                size = srcFileChannel.read(buffer,startPosition);
                long readPosition = srcFileChannel.position();
                log.info("startPosition: {}", startPosition);
                if (size == -1) {
                    builder.setStatus(2);
                    GlacierResponse response = stub.send(builder.build());
                    log.info("client send finished: {}", response.getMsg());
                    break;
                }
                buffer.flip();
                builder.setData(ByteString.copyFrom(buffer));
                builder.setPosition(readPosition);
                GlacierData fileData = builder.build();
                GlacierResponse glacierResponse = stub.send(fileData);
                log.info("client received {}", glacierResponse.getMsg());
                startPosition = glacierResponse.getPosition();
                buffer.clear();
                builder.setStatus(1);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("finished!");
        channel.shutdown();
    }

    @Override
    public void destroy() throws Exception {
        log.info("Shutting down gRPC server ...");
        log.info("gRPC server stopped.");
    }



}
