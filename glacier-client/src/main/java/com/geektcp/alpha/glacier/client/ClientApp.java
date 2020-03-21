package com.geektcp.alpha.glacier.client;

import com.geektcp.alpha.common.base.rpc.GlacierData;
import com.geektcp.alpha.common.base.rpc.GlacierResponse;
import com.geektcp.alpha.common.base.rpc.GlacierServiceGrpc;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author haiyang on 3/14/20 12:53 PM.
 */
@Slf4j
public class ClientApp {

    private static final String SRC_FILE_PATH = "/data/client/test.zip";

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("43.247.68.115", 10000)
                .usePlaintext()
                .build();
        String resourcePath = System.getProperty("user.dir");
        String filePath = resourcePath + SRC_FILE_PATH;
        File srcFile = new File(filePath);
        try (FileInputStream srcFis = new FileInputStream(srcFile)) {
            FileChannel srcFileChannel = srcFis.getChannel();
            GlacierServiceGrpc.GlacierServiceBlockingStub stub = GlacierServiceGrpc.newBlockingStub(channel);
            int len = 20000;
            int size = 0;
            ByteBuffer buffer = ByteBuffer.allocate(len);
            GlacierData.Builder builder = GlacierData.newBuilder();
            builder.setName("test.zip");
            builder.setStatus(0);

            GlacierResponse locateResponse =  stub.locate(builder.build());
            long startPosition = locateResponse.getPosition();
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
}
