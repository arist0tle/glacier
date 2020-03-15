package com.geektcp.alpha.glacier.server.context;

import io.grpc.Server;
import org.springframework.context.ApplicationEvent;

/**
 * @author tanghaiyang on 2020/1/2 1:18.
 */
public class GRpcInitializedEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public GRpcInitializedEvent(Server source) {
        super(source);
    }

    public Server getServer() {
        return (Server) getSource();
    }
}
