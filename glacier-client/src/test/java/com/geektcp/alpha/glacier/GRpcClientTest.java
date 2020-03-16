//package com.geektcp.alpha.glacier;
//
//import com.google.protobuf.ByteString;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.nio.ByteBuffer;
//import java.nio.channels.FileChannel;
//
///**
// * @author tanghaiyang on 2020/1/2 1:18.
// */
//@Slf4j
//public class GRpcClientTest {
//
//    @Test
//    public void startClientForFile() throws Exception {
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress("localhost", 10000)
//                .usePlaintext()
//                .build();
//        String srcFilePath = "F:\\down\\axureRP-8.1.zip";
//        File srcFile = new File(srcFilePath);
//        FileInputStream srcFis = new FileInputStream(srcFile);
//        FileChannel srcFileChannel = srcFis.getChannel();
//        GlacierServiceImpl stub = GlacierServiceImpl.newBlockingStub(channel);
//        int len = 200000;
//        int size = 0;
//        ByteBuffer buffer = ByteBuffer.allocateDirect(len);
//        FileData.Builder builder = FileData.newBuilder();
//        builder.setFileName("test.zip");
//        builder.setStatus(0);
//        while (true) {
//            size = srcFileChannel.read(buffer);
//            if (size == -1) {
//                builder.setStatus(2);
//                Response response = stub.send(builder.build());
//                log.info("client send finished: {}", response.getMsg());
//                break;
//            }
//            buffer.flip();
//            builder.setData(ByteString.copyFrom(buffer));
//            FileData fileData = builder.build();
//            Response response = stub.send(fileData);
//            log.info("client received {}", response.getMsg());
//            buffer.clear();
//            builder.setStatus(1);
//        }
//        log.info("finished!");
//        channel.shutdown();
//        Assert.assertTrue(true);
//    }
//
//    @Test
//    public void writeFileByteByChannel() {
//        try {
//            String srcFilePath = "/share/down/jdk-9+181_linux-x64_ri.zip";
//            String dstFilePath = "/share/down/file/test-1.zip";
//            File srcFile = new File(srcFilePath);
//            FileInputStream srcFis = new FileInputStream(srcFile);
//            FileChannel srcFileChannel = srcFis.getChannel();
//            File dstFile = new File(dstFilePath);
//            FileOutputStream dstFos = new FileOutputStream(dstFile);
//            FileChannel dstFileChannel = dstFos.getChannel();
//            int len = 2000;
//            ByteBuffer buffer = ByteBuffer.allocateDirect(len);
//            int size;
//            while (true) {
//                size = srcFileChannel.read(buffer);
//                if (size == -1) {
//                    break;
//                }
//                buffer.flip();
//                dstFileChannel.write(buffer);
//                buffer.clear();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Assert.assertTrue(true);
//    }
//}
