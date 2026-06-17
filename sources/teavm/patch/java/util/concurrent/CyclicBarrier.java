package java.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TeaVM stub for java.util.concurrent.CyclicBarrier.
 * Browser is single-threaded; all parties arrive synchronously.
 */
public class CyclicBarrier {

    private final int parties;
    private final Runnable barrierCommand;
    private int count;
    private boolean broken;
    private int generation;

    public CyclicBarrier(int parties) {
        this(parties, null);
    }

    public CyclicBarrier(int parties, Runnable barrierAction) {
        if (parties <= 0) throw new IllegalArgumentException("parties <= 0");
        this.parties = parties;
        this.barrierCommand = barrierAction;
        this.count = parties;
    }

    public int getParties() {
        return parties;
    }

    public int await() throws InterruptedException, BrokenBarrierException {
        // In single-threaded browser, we just decrement and return
        int index = --count;
        if (index == 0) {
            // Last party - trigger barrier command and reset
            if (barrierCommand != null) {
                barrierCommand.run();
            }
            count = parties;
            generation++;
            broken = false;
        }
        return index;
    }

    public int await(long timeout, TimeUnit unit) throws InterruptedException, BrokenBarrierException, TimeoutException {
        return await();
    }

    public boolean isBroken() {
        return broken;
    }

    public void reset() {
        broken = false;
        count = parties;
        generation++;
    }

    public int getNumberWaiting() {
        return parties - count;
    }
}
