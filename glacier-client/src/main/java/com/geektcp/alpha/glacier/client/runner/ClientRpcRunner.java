package com.geektcp.alpha.glacier.client.runner;

import com.geektcp.alpha.common.base.rpc.GlacierData;
import com.geektcp.alpha.common.base.rpc.GlacierResponse;
import com.geektcp.alpha.common.base.rpc.GlacierServiceGrpc;
import com.geektcp.alpha.glacier.client.autoconfig.RpcProperties;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

/**
 * @author tanghaiyang on 2020/1/2 1:18.
 */
@Slf4j
public class ClientRpcRunner implements CommandLineRunner, DisposableBean {

    @Autowired
    private RpcProperties rpcProperties;

    @Override
    public void run(String... args) {
        log.info("start client");
        String host = rpcProperties.getServerHost();
        int port = rpcProperties.getServerPort();
        String fileDir = rpcProperties.getFileDir();
        log.info("Starting gRPC Server {}:{}", host, port);
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        String resourcePath = System.getProperty("user.dir");
        String clientPath = resourcePath + fileDir;
        File clientPathFile = new File(clientPath);
        File[] clientFiles = clientPathFile.listFiles();
        if(Objects.isNull(clientFiles)){
            return;
        }
        long startPosition;
        int size;
        int len = rpcProperties.getBlockSize();
        ByteBuffer buffer = ByteBuffer.allocate(len);
        for (File clientFile : clientFiles) {
            try (FileInputStream clientFileStream = new FileInputStream(clientFile)) {
                FileChannel srcFileChannel = clientFileStream.getChannel();
                GlacierServiceGrpc.GlacierServiceBlockingStub stub = GlacierServiceGrpc.newBlockingStub(channel);
                GlacierData.Builder builder = GlacierData.newBuilder();
                builder.setName(clientFile.getName());
                builder.setStatus(0);
                GlacierResponse locateResponse = stub.locate(builder.build());
                startPosition = locateResponse.getPosition();

                while (true) {
                    size = srcFileChannel.read(buffer, startPosition);
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
