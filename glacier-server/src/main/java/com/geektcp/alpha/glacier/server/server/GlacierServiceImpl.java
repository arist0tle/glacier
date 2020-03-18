package com.geektcp.alpha.glacier.server.server;

import com.geektcp.alpha.common.base.rpc.GlacierData;
import com.geektcp.alpha.common.base.rpc.GlacierResponse;
import com.geektcp.alpha.common.base.rpc.GlacierServiceGrpc;
import com.geektcp.alpha.glacier.server.annotation.RpcService;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

/**
 * @author tanghaiyang on 2020/3/15 15:50.
 */
@Slf4j
@RpcService
public class GlacierServiceImpl extends GlacierServiceGrpc.GlacierServiceImplBase {

    private static final String SAVE_PATH = "data";
    private static FileChannel fileChannel;

    @Override
    public void send(GlacierData request, StreamObserver<GlacierResponse> responseObserver) {
        String fileName = request.getFileName();
        int status = request.getStatus();

        String message = "response";
        GlacierResponse response = GlacierResponse.newBuilder().setMsg(message).build();
        log.info("server responded {}", response);

        try {
            FileChannel dstFileChannel = getFileChannel(fileName, status);
            if (Objects.isNull(dstFileChannel)) {
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
            ByteString data = request.getData();
            if(Objects.isNull(data)){
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
            ByteBuffer buffer = ByteBuffer.wrap(data.toByteArray());
            dstFileChannel.write(buffer);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.info("fileName: {} | status:{}", fileName, status, e);
        }

    }

    //////////////////////
    private static FileChannel getFileChannel(String fileName, int status) throws Exception {
        if (status == 0 || Objects.isNull(fileChannel)) {
            try {
                File dstFile = new File(SAVE_PATH + fileName);
                FileOutputStream dstFos = new FileOutputStream(dstFile,true);
                fileChannel = dstFos.getChannel();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        if (status == 2 && Objects.nonNull(fileChannel)) {
            fileChannel.close();
            return null;
        }
        return fileChannel;
    }
}
