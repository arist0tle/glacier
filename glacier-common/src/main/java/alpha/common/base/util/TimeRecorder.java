package alpha.common.base.util;

/**
 * @author tanghaiyang on 2017/11/16.
 */
public class TimeRecorder {
    private long startTime = 0L;
    private long endTime = 0L;
    private String elapsedTimeStr;

    public TimeRecorder() {
    }

    public TimeRecorder(boolean start) {
        if (start) {
            this.start();
        }
    }

    public TimeRecorder start() {
        this.startTime = System.currentTimeMillis();
        return this;
    }

    public TimeRecorder close() {
        return close(false);
    }

    public TimeRecorder close(boolean pretty) {
        this.endTime = System.currentTimeMillis();
        if (pretty) {
            this.setElapsedTimeStr();
        }
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getElapsedTime() {
        if (endTime == 0) {
            this.close();
        }
        return endTime - startTime;
    }

    public String getElapsedTimeStr() {
        if (endTime == 0) {
            this.close();
        }
        return elapsedTimeStr;
    }

    private void setElapsedTimeStr() {
        long elapsed = endTime - startTime;
        long mins = elapsed / 60000;
        long sec = (elapsed - mins * 60000) / 1000;
        long ms = (elapsed - mins * 60000 - sec * 1000);
        this.elapsedTimeStr = new StringBuilder().append(mins).append(" mins, ").append(sec).append(" sec, ").append(ms)
                .append(" ms").toString();
    }

    public static void main(String[] args) {
        TimeRecorder tr = new TimeRecorder(true);
        System.out.println(tr.getElapsedTimeStr());
    }
}
