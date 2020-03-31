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
        log.info("Starting gRPC Server {}:{}", host, port);
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        File clientPathFile = new File(rpcProperties.getFileDir());
        if(checkFile(clientPathFile)) {
            doUploadJob(channel, clientPathFile);
        }
    }

    @Override
    public void destroy() throws Exception {
        log.info("Shutting down gRPC server ...");
        log.info("gRPC server stopped.");
    }

    ///////////////////////////////////
    private void doUploadJob(ManagedChannel channel, File clientPathFile){
        long startPosition;
        int size;
        int len = rpcProperties.getBlockSize();
        File[] clientFiles = clientPathFile.listFiles();
        if (Objects.isNull(clientFiles)) {
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(len);
        for (File clientFile : clientFiles) {
            if (!clientFile.exists()) {
                log.info("file is not exist: {}", clientFile.getAbsolutePath());
                continue;
            }
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
                    log.debug("position: {} MB", startPosition / 1024 / 1024);
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
                    if (log.isDebugEnabled()) {
                        log.info("client received {}", glacierResponse.getMsg());
                    }
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

    private boolean checkFile(File clientPathFile){
        if (!clientPathFile.exists()) {
            boolean createDir = clientPathFile.mkdir();
            if (!createDir) {
                log.error("create dir failed: {}", rpcProperties.getFileDir());
            }
            return false;
        }
        return true;
    }
}
