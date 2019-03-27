package thread_pool;

public interface ThreadPool {

    void fill();

    void stopAllThreads();

    void clearTerminatedThreads();

}
