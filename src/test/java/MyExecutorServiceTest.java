import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import work_provider.WorkQueueIsFullException;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MyExecutorServiceTest {

    @Test(expected = WorkQueueIsFullException.class)
    public void fullWorkingQueueExceptionTest() {
        int poolSize = 15;
        int workQueueSize = 15;
        MyExecutorService myExecutorService = MyExecutors.newFixedThreadPool(poolSize, workQueueSize);

        for (int i = 0; i < workQueueSize + 1; i++) {
            myExecutorService.execute(() -> {
                while (true) ;
            });
        }
    }

    @Test
    public void asyncStackPushingExecutionTest() throws InterruptedException {
        int poolSize = 2;
        int workQueueSize = 2;
        MyExecutorService myExecutorService = MyExecutors.newFixedThreadPool(poolSize, workQueueSize);

        Stack<Integer> stack = new Stack<>();
        AtomicBoolean hasResult = new AtomicBoolean(false);
        myExecutorService.execute(() -> {
            try {
                synchronized (stack) {
                    stack.wait();
                }
                stack.push(1);
                synchronized (hasResult) {
                    hasResult.set(true);
                    hasResult.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        myExecutorService.execute(() -> {
            stack.push(2);
            synchronized (stack) {
                stack.notifyAll();
            }
        });
        synchronized (hasResult) {
            hasResult.wait();
        }
        assertTrue(hasResult.get());
        assertEquals(1, stack.pop().intValue());
        assertEquals(2, stack.pop().intValue());
    }

    @Test
    public void workQueueSizeOneExecutionTest() throws InterruptedException {
        int poolSize = 3;
        int workQueueSize = 1;
        MyExecutorService myExecutorService = MyExecutors.newFixedThreadPool(poolSize, workQueueSize);

        Stack<Integer> stack = new Stack<>();
        myExecutorService.execute(() -> stack.push(1));
        Thread.sleep(1000);
        assertFalse(stack.empty());
    }

    @Test
    public void poolSizeOneExecutionTest() throws InterruptedException {
        int poolSize = 1;
        int workQueueSize = 20;
        MyExecutorService myExecutorService = MyExecutors.newFixedThreadPool(poolSize, workQueueSize);

        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < 20; i++) {
            myExecutorService.execute(() -> stack.push(1));
        }
        Thread.sleep(1000);
        assertEquals(20, stack.size());
    }

    @Test
    public void correctInterruptionTest() {
        int poolSize = 15;
        int workQueueSize = 15;
        MyExecutorService myExecutorService = MyExecutors.newFixedThreadPool(poolSize, workQueueSize);
        for (int i = 0; i < workQueueSize; i++) {
            if (i % 2 == 0) {
                myExecutorService.execute(() -> {
                    while (true) ;
                });
            } else {
                myExecutorService.execute(() -> {
                });
            }
        }

        myExecutorService.shutdownNow();
        assert true; // no exceptions
    }

    @Test
    public void workAfterFullQueueExceptionTest() {
        int poolSize = 2;
        int workQueueSize = 2;
        MyExecutorService myExecutorService = MyExecutors.newFixedThreadPool(poolSize, workQueueSize);

        AtomicLong atomicLong = new AtomicLong(0);
        try {
            for (int i = 0; i < workQueueSize + 1; i++) {
                myExecutorService.execute(() -> {
                    while (true) {
                        atomicLong.getAndAdd(1);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            assert false;
                        }
                    }
                });
            }
        } catch (WorkQueueIsFullException e) {
            // no action
        }

        long beforeDelay = atomicLong.get();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e1) {
            assert false;
        }
        long afterDelay = atomicLong.get();
        assertNotEquals(beforeDelay, afterDelay);
    }
}