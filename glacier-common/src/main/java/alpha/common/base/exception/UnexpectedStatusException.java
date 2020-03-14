package alpha.common.base.exception;


import alpha.common.base.constant.Status;

/**
 * @author tanghaiyang on 2018/5/16.
 */
public class UnexpectedStatusException extends BaseException {

    private Status status;

    public UnexpectedStatusException(Status status) {
        this(status, new String[]{});
    }


    public UnexpectedStatusException(Status status, Object... args) {
        this(status, null, args);
    }

    public UnexpectedStatusException(Status status, Throwable cause) {
        super(status.getCode(), status.getDesc(), cause);
        this.status = status;
    }

    public UnexpectedStatusException(Status status, Throwable cause, Object... args) {
        super(status.getCode(), status.getDesc(), cause, args);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
