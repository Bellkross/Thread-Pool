package thread_pool;

import work_provider.WorkProvider;

import java.util.LinkedList;
import java.util.List;

public class ThreadPoolImpl implements ThreadPool {

    private final List<Thread> threads;
    private final WorkProvider workProvider;
    private int capacity;

    public ThreadPoolImpl(final int capacity, final WorkProvider workProvider) {
        this.capacity = capacity;
        this.threads = new LinkedList<>();
        this.workProvider = workProvider;
    }

    public void fill() {
        for (int i = 0; i < capacity; i++) {
            ThreadWorker worker = new ThreadWorker(workProvider);
            threads.add(worker);
            worker.start();
        }
    }

    public void stopAllThreads() {
        threads.forEach(Thread::interrupt);
    }

    public void clearTerminatedThreads() {
        threads.stream()
                .filter(this::isThreadTerminated)
                .forEach(threads::remove);
    }

    private boolean isThreadTerminated(Thread thread) {
        return thread.getState().equals(Thread.State.TERMINATED);
    }
}
