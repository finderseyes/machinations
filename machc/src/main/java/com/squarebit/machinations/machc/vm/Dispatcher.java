package com.squarebit.machinations.machc.vm;

import java.util.concurrent.*;

public final class Dispatcher {
    /**
     * The internal {@link ScheduledExecutorService}.
     */
    private ScheduledExecutorService executorService;

    /**
     * Initializes a new instance of {@link Dispatcher}.
     *
     * @param executorService the executor service this dispatcher uses.
     */
    public Dispatcher(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Creates a new single thread dispatcher.
     * @param threadName
     * @return
     */
    public static Dispatcher createSingleThreadDispatcher(String threadName) {
        Dispatcher dispatcher = new Dispatcher(Executors.newSingleThreadScheduledExecutor(r -> {
            Thread workerThread = new Thread(r);
            workerThread.setName(threadName);
            return workerThread;
        }));

        return dispatcher;
    }

    /**
     * Dispatch
     * @param runnable
     * @return
     */
    public CompletableFuture<Void> dispatch(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, this.executorService);
    }

    /**
     * Shuts down the {@link Dispatcher} gracefully.
     */
    public void shutdown() {
        this.shutdownAndAwaitTermination(this.executorService);
    }

    /**
     * Gracefully shut down an {@link ExecutorService}.
     * @param pool
     */
    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if currentYield thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
