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
            log.info("tips: {} | onRemoval thread id: {}", tips, Thread.currentThread().getId());
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
    private static final String KEY_FINISHED = "finished";

    @Override
    public void send(GlacierData request, StreamObserver<GlacierResponse> responseObserver) {
        String fileName = request.getName();
        Long isFinished = cacheRpc.getIfPresent(KEY_FINISHED);
        String message = "response from server!";
        GlacierResponse.Builder builder = GlacierResponse.newBuilder();
        if (Objects.nonNull(isFinished) && isFinished == 1L) {
            message = "文件已经上传完毕！";
            log.info(message);
            builder.setMsg(message);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
            return;
        }
        long status = request.getStatus();
        Long position = cacheRpc.getIfPresent(KEY_POSITION);
        if (Objects.isNull(position)) {
            position = 0L;
        }
        builder.setMsg(message);
        String resourcePath = System.getProperty("user.dir");
        String filePath = resourcePath + SAVE_PATH + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            log.info("文件不存在");
            cacheRpc.invalidate(KEY_FINISHED);
        }

        try {
            FileChannel dstFileChannel = getFileChannel(file, status);
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
            log.info("position: {}", position);
            dstFileChannel.write(buffer, position);
            long writePosition = position + buffer.position();
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
        if (Objects.nonNull(position)) {
            builder.setPosition(position);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    //////////////////////
    private static FileChannel getFileChannel(File file, long status) throws Exception {
        try {
            if (status == 0 || Objects.isNull(fileChannel)) {
                FileOutputStream dstFos = new FileOutputStream(file, true);
                fileChannel = dstFos.getChannel();
            }
            if (status == 2) {
                fileChannel.close();
                cacheRpc.put(KEY_FINISHED, 1L);
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return fileChannel;
    }
}
