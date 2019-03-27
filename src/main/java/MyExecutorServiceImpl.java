import thread_pool.ThreadPool;
import thread_pool.ThreadPoolImpl;
import work_provider.WorkProvider;
import work_provider.WorkProviderImpl;
import work_provider.WorkQueueIsFullException;

public class MyExecutorServiceImpl implements MyExecutorService {

    private final ThreadPool threadPool;
    private final WorkProvider workProvider;

    public MyExecutorServiceImpl(int poolSize, int workQueueSize) {
        workProvider = new WorkProviderImpl(workQueueSize);
        threadPool = new ThreadPoolImpl(poolSize, workProvider);
        threadPool.fill();
    }

    /**
     * Executes the given command in a thread from thread pool
     *
     * @param command the runnable task
     * @throws WorkQueueIsFullException if this task cannot be accepted for execution due
     *                                  to jobQueue is full
     */
    public void execute(Runnable command) throws WorkQueueIsFullException {
        workProvider.addJob(command);
    }

    /**
     * Attempts to stop all actively executing tasks.
     */
    public void shutdownNow() {
        threadPool.stopAllThreads();
        workProvider.removeAllJobs();
        threadPool.fill();
    }
}
