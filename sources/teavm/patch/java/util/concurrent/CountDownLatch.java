package java.util.concurrent;

/**
 * TeaVM stub for java.util.concurrent.CountDownLatch.
 * Browser is single-threaded; await() returns immediately, countDown() no-ops.
 */
public class CountDownLatch {

    private int count;

    public CountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.count = count;
    }

    public void await() throws InterruptedException {
        // In single-threaded browser, we can't actually wait.
        // The latch is either already at 0 or we just return.
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        // Cannot actually wait in single-threaded browser
        return count == 0;
    }

    public void countDown() {
        if (count > 0) {
            count--;
        }
    }

    public long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return super.toString() + "[Count = " + count + "]";
    }
}
