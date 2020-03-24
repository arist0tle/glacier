package com.geektcp.alpha.glacier.server.server;

import com.geektcp.alpha.common.base.rpc.GlacierData;
import com.geektcp.alpha.common.base.rpc.GlacierResponse;
import com.geektcp.alpha.common.base.rpc.GlacierServiceGrpc;
import com.geektcp.alpha.glacier.server.annotation.RpcService;
import com.geektcp.alpha.glacier.server.autoconfig.RpcProperties;
import com.google.common.cache.*;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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
            if (notification.getCause().equals(RemovalCause.EXPIRED) && notification.getValue() != null) {
                log.info("Remove {} when EXPIRED", notification.getKey());
            }
        }
    };

    private static LoadingCache<String, Long> cacheRpc = CacheBuilder.newBuilder()
            .refreshAfterWrite(30, TimeUnit.DAYS)
            .expireAfterWrite(30, TimeUnit.DAYS)
            .removalListener(myRemovalListener)
            .build(new CacheLoader<String, Long>() {
                @Override
                public Long load(@Nullable String key) throws Exception {
                    return 0L;
                }
            });

    private RpcProperties rpcProperties;
    private static FileChannel fileChannel;

    @Autowired
    public GlacierServiceImpl(RpcProperties rpcProperties) {
        this.rpcProperties = rpcProperties;
    }

    @Override
    public void send(GlacierData request, StreamObserver<GlacierResponse> responseObserver) {
        String fileName = request.getName();
        String message = "response from server!";
        GlacierResponse.Builder builder = GlacierResponse.newBuilder();

        String absolutePath = rpcProperties.getFileDir() + RpcProperties.SLASH + fileName;
        File file = new File(absolutePath);
        checkFileExist(absolutePath);
        Long isFinished = cacheGet(RpcProperties.KEY_FINISHED);
        if (isFinished == 1L) {
            log.info("file upload finished: {}", absolutePath);
            builder.setMsg("文件已经上传完毕！");
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
            return;
        }
        long status = request.getStatus();
        Long position = cacheGet(RpcProperties.KEY_POSITION);
        builder.setMsg(message);
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
            if (log.isDebugEnabled()) {
                log.debug("position: {} MB", position / 1024 / 1024);
            }
            dstFileChannel.write(buffer, position);
            long writePosition = position + buffer.position();
            builder.setPosition(writePosition);
            cacheRpc.put(RpcProperties.KEY_POSITION, writePosition);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.info("fileName: {} | status:{}", fileName, status, e);
        }

    }

    @Override
    public void locate(GlacierData request, StreamObserver<GlacierResponse> responseObserver) {
        String fileName = request.getName();
        String absolutePath = rpcProperties.getFileDir() + RpcProperties.SLASH + fileName;
        checkFileExist(absolutePath);
        Long position = cacheRpc.getIfPresent(RpcProperties.KEY_POSITION);
        GlacierResponse.Builder builder = GlacierResponse.newBuilder();
        if (Objects.nonNull(position)) {
            builder.setPosition(position);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    //////////////////////
    private static FileChannel getFileChannel(File file, long status) {
        try {
            FileOutputStream dstFos = new FileOutputStream(file, true);
            if (status == 0 || Objects.isNull(fileChannel)) {
                fileChannel = dstFos.getChannel();
            }
            if (status == 2) {
                fileChannel.close();
                dstFos.close();
                cacheRpc.put(RpcProperties.KEY_FINISHED, 1L);
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return fileChannel;
    }


    private void checkFileExist(String absolutePath) {
        File file = new File(absolutePath);
        if (!file.exists()) {
            log.info("文件不存在: {}", absolutePath);
            cacheRpc.invalidate(RpcProperties.KEY_FINISHED);
            cacheRpc.invalidate(RpcProperties.KEY_POSITION);
        }
    }

    private Long cacheGet(String key) {
        Long position = cacheRpc.getIfPresent(key);
        if (Objects.isNull(position)) {
            return 0L;
        }
        return position;
    }
}
