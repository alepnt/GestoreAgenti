package com.example.GestoreAgenti.fx.data.remote;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * Semplice scheduler che limita il numero di operazioni remote concorrenti
 * tramite un {@link Semaphore} e permette di interrompere le richieste in modo
 * cooperativo.
 */
public final class RemoteTaskScheduler {

    private final Semaphore semaphore;
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private final Set<CompletableFuture<?>> inFlight = ConcurrentHashMap.newKeySet();

    public RemoteTaskScheduler(int maxConcurrentOperations) {
        if (maxConcurrentOperations <= 0) {
            throw new IllegalArgumentException("Il numero massimo di operazioni deve essere positivo");
        }
        this.semaphore = new Semaphore(maxConcurrentOperations);
    }

    public <T> CompletableFuture<T> schedule(Supplier<CompletableFuture<T>> taskSupplier) {
        Objects.requireNonNull(taskSupplier, "taskSupplier");
        if (stopped.get()) {
            return CompletableFuture.failedFuture(new CancellationException("Scheduler fermato"));
        }
        if (!semaphore.tryAcquire()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Troppe operazioni remote simultanee"));
        }
        CompletableFuture<T> future;
        try {
            future = taskSupplier.get();
        } catch (Throwable error) {
            semaphore.release();
            return CompletableFuture.failedFuture(error);
        }
        if (future == null) {
            semaphore.release();
            return CompletableFuture.failedFuture(new IllegalStateException("Il task fornito non può essere nullo"));
        }
        inFlight.add(future);
        return future.whenComplete((result, error) -> {
            inFlight.remove(future);
            semaphore.release();
        });
    }

    public void stop() {
        if (stopped.compareAndSet(false, true)) {
            inFlight.forEach(future -> future.cancel(true));
            inFlight.clear();
        }
    }

    public boolean isStopped() {
        return stopped.get();
    }
}
