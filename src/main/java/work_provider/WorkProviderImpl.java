package work_provider;

import java.util.LinkedList;
import java.util.Queue;

public class WorkProviderImpl implements WorkProvider {

    private final Queue<Runnable> jobQueue;
    private int capacity;

    public WorkProviderImpl(final int capacity) {
        jobQueue = new LinkedList<>();
        this.capacity = capacity;
    }

    public void addJob(final Runnable runnable) throws WorkQueueIsFullException {
        if (isFull()) {
            throw new WorkQueueIsFullException();
        }
        synchronized (jobQueue) {
            jobQueue.add(runnable);
            jobQueue.notifyAll();
        }
    }

    public Runnable getJob() {
        synchronized (jobQueue) {
            while (jobQueue.isEmpty()) {
                try {
                    jobQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return jobQueue.poll();
        }
    }

    @Override
    public void removeAllJobs() {
        jobQueue.clear();
    }

    @Override
    public boolean isFull() {
        return jobQueue.size() >= capacity;
    }
}
