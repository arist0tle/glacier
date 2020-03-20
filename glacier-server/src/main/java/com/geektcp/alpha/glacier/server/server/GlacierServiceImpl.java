package com.geektcp.alpha.glacier.server.server;

import com.geektcp.alpha.common.base.rpc.GlacierData;
import com.geektcp.alpha.common.base.rpc.GlacierResponse;
import com.geektcp.alpha.common.base.rpc.GlacierServiceGrpc;
import com.geektcp.alpha.glacier.server.annotation.RpcService;
import com.google.common.cache.*;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author tanghaiyang on 2020/3/15 15:50.
 */
@Slf4j
@RpcService
public class GlacierServiceImpl extends GlacierServiceGrpc.GlacierServiceImplBase {

    private static RemovalListener<String, Long> myRemovalListener = new RemovalListener<String, Long>() {
        @Override
        public void onRemoval(RemovalNotification<String, Long> notification) {
            String tips = String.format("key=%s,value=%s,reason=%s in myRemovalListener", notification.getKey(), notification.getValue(), notification.getCause());
            log.info("tips: {} | onRemoval thread id: {}",tips, Thread.currentThread().getId());
            if (notification.getCause().equals(RemovalCause.EXPIRED) && notification.getValue() != null) {
                log.info("Remove {} in cacheConnection", notification.getKey());
            }
        }
    };

    private static LoadingCache<String, Long> cacheRpc = CacheBuilder.newBuilder()
            .refreshAfterWrite(7, TimeUnit.HOURS)
            .expireAfterWrite(5, TimeUnit.HOURS)
            .removalListener(myRemovalListener)
            .build(new CacheLoader<String, Long>() {
                @Override
                public Long load(@Nullable String key) throws Exception {
                    return 0L;
                }
            });

    private static final String SAVE_PATH = "/data/server/";
    private static FileChannel fileChannel;
    private static final String KEY_POSITION = "position";

    @Override
    public void send(GlacierData request, StreamObserver<GlacierResponse> responseObserver) {
        String fileName = request.getName();
        long status = request.getStatus();
        long position = request.getPosition();
        String message = "response from server!";
        GlacierResponse.Builder builder = GlacierResponse.newBuilder().setMsg(message);
        try {
            FileChannel dstFileChannel = getFileChannel(fileName, status);
            if (Objects.isNull(dstFileChannel)) {
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }
            ByteString data = request.getData();
            if (Objects.isNull(data)) {
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }
            ByteBuffer buffer = ByteBuffer.wrap(data.toByteArray());
            dstFileChannel.write(buffer, position);
            long writePosition = dstFileChannel.position();
            builder.setPosition(writePosition);
            cacheRpc.put(KEY_POSITION, writePosition);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.info("fileName: {} | status:{}", fileName, status, e);
        }

    }

    @Override
    public void locate(GlacierData request, StreamObserver<GlacierResponse> responseObserver) {
        Long position = cacheRpc.getIfPresent(KEY_POSITION);
        GlacierResponse.Builder builder = GlacierResponse.newBuilder();
        if(Objects.nonNull(position)) {
            builder.setPosition(position);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    //////////////////////
    private static FileChannel getFileChannel(String fileName, long status) {
        String resourcePath = System.getProperty("user.dir");
        if (status == 0 || Objects.isNull(fileChannel)) {
            String filePath = resourcePath + SAVE_PATH + fileName;
            File dstFile = new File(filePath);
            try (FileOutputStream dstFos = new FileOutputStream(dstFile, true)) {
                fileChannel = dstFos.getChannel();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        if (status == 2 && Objects.nonNull(fileChannel)) {

            return null;
        }
        return fileChannel;
    }
}
