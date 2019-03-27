import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyExecutorServiceImpl implements MyExecutorService {

    private Queue<Runnable> commands;

    public MyExecutorServiceImpl(int poolSize) {
        this.commands = new ConcurrentLinkedQueue<Runnable>();
    }

    /**
     * Executes the given command in a thread from thread pool
     *
     * @param command the runnable task
     * @throws WorkQueueIsFullException if this task cannot be accepted for execution due
     *                                  to workQueue is full
     */
    public void execute(Runnable command) {

    }

    /**
     * Attempts to stop all actively executing tasks.
     */
    public void shutdownNow() {

    }
}
