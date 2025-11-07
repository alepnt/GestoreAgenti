package com.example.GestoreAgenti.fx.data.remote; // Esegue: package com.example.GestoreAgenti.fx.data.remote;

import java.util.Objects; // Esegue: import java.util.Objects;
import java.util.Set; // Esegue: import java.util.Set;
import java.util.concurrent.CancellationException; // Esegue: import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture; // Esegue: import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap; // Esegue: import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore; // Esegue: import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean; // Esegue: import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier; // Esegue: import java.util.function.Supplier;

/**
 * Semplice scheduler che limita il numero di operazioni remote concorrenti
 * tramite un {@link Semaphore} e permette di interrompere le richieste in modo
 * cooperativo.
 */
public final class RemoteTaskScheduler { // Esegue: public final class RemoteTaskScheduler {

    private final Semaphore semaphore; // Esegue: private final Semaphore semaphore;
    private final AtomicBoolean stopped = new AtomicBoolean(false); // Esegue: private final AtomicBoolean stopped = new AtomicBoolean(false);
    private final Set<CompletableFuture<?>> inFlight = ConcurrentHashMap.newKeySet(); // Esegue: private final Set<CompletableFuture<?>> inFlight = ConcurrentHashMap.newKeySet();

    public RemoteTaskScheduler(int maxConcurrentOperations) { // Esegue: public RemoteTaskScheduler(int maxConcurrentOperations) {
        if (maxConcurrentOperations <= 0) { // Esegue: if (maxConcurrentOperations <= 0) {
            throw new IllegalArgumentException("Il numero massimo di operazioni deve essere positivo"); // Esegue: throw new IllegalArgumentException("Il numero massimo di operazioni deve essere positivo");
        } // Esegue: }
        this.semaphore = new Semaphore(maxConcurrentOperations); // Esegue: this.semaphore = new Semaphore(maxConcurrentOperations);
    } // Esegue: }

    public <T> CompletableFuture<T> schedule(Supplier<CompletableFuture<T>> taskSupplier) { // Esegue: public <T> CompletableFuture<T> schedule(Supplier<CompletableFuture<T>> taskSupplier) {
        Objects.requireNonNull(taskSupplier, "taskSupplier"); // Esegue: Objects.requireNonNull(taskSupplier, "taskSupplier");
        if (stopped.get()) { // Esegue: if (stopped.get()) {
            return CompletableFuture.failedFuture(new CancellationException("Scheduler fermato")); // Esegue: return CompletableFuture.failedFuture(new CancellationException("Scheduler fermato"));
        } // Esegue: }
        if (!semaphore.tryAcquire()) { // Esegue: if (!semaphore.tryAcquire()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Troppe operazioni remote simultanee")); // Esegue: return CompletableFuture.failedFuture(new IllegalStateException("Troppe operazioni remote simultanee"));
        } // Esegue: }
        CompletableFuture<T> future; // Esegue: CompletableFuture<T> future;
        try { // Esegue: try {
            future = taskSupplier.get(); // Esegue: future = taskSupplier.get();
        } catch (Throwable error) { // Esegue: } catch (Throwable error) {
            semaphore.release(); // Esegue: semaphore.release();
            return CompletableFuture.failedFuture(error); // Esegue: return CompletableFuture.failedFuture(error);
        } // Esegue: }
        if (future == null) { // Esegue: if (future == null) {
            semaphore.release(); // Esegue: semaphore.release();
            return CompletableFuture.failedFuture(new IllegalStateException("Il task fornito non può essere nullo")); // Esegue: return CompletableFuture.failedFuture(new IllegalStateException("Il task fornito non può essere nullo"));
        } // Esegue: }
        inFlight.add(future); // Esegue: inFlight.add(future);
        return future.whenComplete((result, error) -> { // Esegue: return future.whenComplete((result, error) -> {
            inFlight.remove(future); // Esegue: inFlight.remove(future);
            semaphore.release(); // Esegue: semaphore.release();
        }); // Esegue: });
    } // Esegue: }

    public void stop() { // Esegue: public void stop() {
        if (stopped.compareAndSet(false, true)) { // Esegue: if (stopped.compareAndSet(false, true)) {
            inFlight.forEach(future -> future.cancel(true)); // Esegue: inFlight.forEach(future -> future.cancel(true));
            inFlight.clear(); // Esegue: inFlight.clear();
        } // Esegue: }
    } // Esegue: }

    public boolean isStopped() { // Esegue: public boolean isStopped() {
        return stopped.get(); // Esegue: return stopped.get();
    } // Esegue: }
} // Esegue: }
