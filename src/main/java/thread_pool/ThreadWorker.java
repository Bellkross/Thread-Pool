package thread_pool;

import work_provider.WorkProvider;

import static java.util.Objects.nonNull;

public class ThreadWorker extends Thread {

    private final WorkProvider workProvider;

    public ThreadWorker(final WorkProvider workProvider) {
        this.workProvider = workProvider;
    }

    @Override
    public void run() {
        while (!isInterrupted() && nonNull(workProvider)) {
            workProvider.getJob().run();
        }
    }
}
