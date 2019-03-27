import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import work_provider.WorkQueueIsFullException;

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




}