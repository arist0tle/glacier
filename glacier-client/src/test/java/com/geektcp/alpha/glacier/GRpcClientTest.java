package com.geektcp.alpha.glacier;

import com.geektcp.alpha.common.base.rpc.GlacierData;
import com.geektcp.alpha.common.base.rpc.GlacierResponse;
import com.geektcp.alpha.common.base.rpc.GlacierServiceGrpc;
import com.geektcp.alpha.glacier.client.ClientApp;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author tanghaiyang on 2020/1/2 1:18.
 */
@Slf4j
public class GRpcClientTest {

    @Test
    public void startClientForFile() throws Exception {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 10000)
                .usePlaintext()
                .build();
        String srcFilePath = "/data/client/test.zip";
        String resourcePath = System.getProperty("user.dir");
        String filePath = resourcePath + "/.."+ srcFilePath;
        File srcFile = new File(filePath);
        FileInputStream srcFis = new FileInputStream(srcFile);
        FileChannel srcFileChannel = srcFis.getChannel();
        GlacierServiceGrpc.GlacierServiceBlockingStub stub = GlacierServiceGrpc.newBlockingStub(channel);

        int len = 200000;
        int size = 0;
        ByteBuffer buffer = ByteBuffer.allocateDirect(len);
        GlacierData.Builder builder = GlacierData.newBuilder();
        builder.setName("test.zip");
        builder.setStatus(0);
        while (true) {
            size = srcFileChannel.read(buffer);
            if (size == -1) {
                builder.setStatus(2);
                GlacierResponse response = stub.send(builder.build());
                log.info("client send finished: {}", response.getMsg());
                break;
            }
            buffer.flip();
            builder.setData(ByteString.copyFrom(buffer));
            GlacierData fileData = builder.build();
            GlacierResponse glacierResponse = stub.send(fileData);
            log.info("client received {}", glacierResponse.getMsg());
            buffer.clear();
            builder.setStatus(1);
        }
        log.info("finished!");
        channel.shutdown();
        Assert.assertTrue(true);
    }

}
