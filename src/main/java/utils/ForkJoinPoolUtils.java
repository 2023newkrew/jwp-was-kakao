package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinPoolUtils {

    private static final int DEFAULT_POOL_SIZE = 200;
    private static final ExecutorService executorService = new ForkJoinPool(DEFAULT_POOL_SIZE);

    public static void execute(Runnable task) {
        executorService.submit(task);
    }

}
