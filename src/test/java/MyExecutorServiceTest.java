import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import work_provider.WorkQueueIsFullException;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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


}