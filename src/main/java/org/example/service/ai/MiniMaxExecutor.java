package org.example.service.ai;

import org.example.model.Move;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class MiniMaxExecutor {

    private static final int NUM_THREADS = 4;
    private final ExecutorService executorService;

    public MiniMaxExecutor() {
        this.executorService = Executors.newFixedThreadPool(NUM_THREADS);
    }

    public List<Move> calculateMoves(List<Callable<Move>> tasks) {
        try {
            return executorService.invokeAll(tasks)
                    .stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutDown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
