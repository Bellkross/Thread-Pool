package work_provider;

public interface WorkProvider {

    void addJob(Runnable runnable) throws WorkQueueIsFullException;
    Runnable getJob();
    void removeAllJobs();
    boolean isFull();

}
