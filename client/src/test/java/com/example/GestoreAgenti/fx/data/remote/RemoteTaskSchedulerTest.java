package com.example.GestoreAgenti.fx.data.remote; // Esegue: package com.example.GestoreAgenti.fx.data.remote;

import org.junit.jupiter.api.Test; // Esegue: import org.junit.jupiter.api.Test;

import java.util.concurrent.CancellationException; // Esegue: import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture; // Esegue: import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException; // Esegue: import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*; // Esegue: import static org.junit.jupiter.api.Assertions.*;

class RemoteTaskSchedulerTest { // Esegue: class RemoteTaskSchedulerTest {

    @Test // Esegue: @Test
    void constructorRejectsNonPositiveConcurrency() { // Esegue: void constructorRejectsNonPositiveConcurrency() {
        assertThrows(IllegalArgumentException.class, () -> new RemoteTaskScheduler(0)); // Esegue: assertThrows(IllegalArgumentException.class, () -> new RemoteTaskScheduler(0));
        assertThrows(IllegalArgumentException.class, () -> new RemoteTaskScheduler(-2)); // Esegue: assertThrows(IllegalArgumentException.class, () -> new RemoteTaskScheduler(-2));
    } // Esegue: }

    @Test // Esegue: @Test
    void scheduleFailsWhenSemaphoreLimitReached() { // Esegue: void scheduleFailsWhenSemaphoreLimitReached() {
        RemoteTaskScheduler scheduler = new RemoteTaskScheduler(1); // Esegue: RemoteTaskScheduler scheduler = new RemoteTaskScheduler(1);

        CompletableFuture<String> firstPending = new CompletableFuture<>(); // Esegue: CompletableFuture<String> firstPending = new CompletableFuture<>();
        CompletableFuture<String> firstScheduled = scheduler.schedule(() -> firstPending); // Esegue: CompletableFuture<String> firstScheduled = scheduler.schedule(() -> firstPending);
        assertNotSame(firstPending, firstScheduled); // Esegue: assertNotSame(firstPending, firstScheduled);
        assertFalse(firstPending.isDone()); // Esegue: assertFalse(firstPending.isDone());
        assertFalse(firstScheduled.isDone()); // Esegue: assertFalse(firstScheduled.isDone());

        CompletableFuture<String> secondAttempt = scheduler.schedule(CompletableFuture::new); // Esegue: CompletableFuture<String> secondAttempt = scheduler.schedule(CompletableFuture::new);
        assertTrue(secondAttempt.isCompletedExceptionally()); // Esegue: assertTrue(secondAttempt.isCompletedExceptionally());
        CompletionException overflow = assertThrows(CompletionException.class, secondAttempt::join); // Esegue: CompletionException overflow = assertThrows(CompletionException.class, secondAttempt::join);
        assertTrue(overflow.getCause() instanceof IllegalStateException); // Esegue: assertTrue(overflow.getCause() instanceof IllegalStateException);

        firstPending.complete("done"); // Esegue: firstPending.complete("done");
        assertEquals("done", firstScheduled.join()); // Esegue: assertEquals("done", firstScheduled.join());

        CompletableFuture<String> third = scheduler.schedule(() -> CompletableFuture.completedFuture("ok")); // Esegue: CompletableFuture<String> third = scheduler.schedule(() -> CompletableFuture.completedFuture("ok"));
        assertEquals("ok", third.join()); // Esegue: assertEquals("ok", third.join());
    } // Esegue: }

    @Test // Esegue: @Test
    void stopCancelsInflightOperationsAndRejectsNewOnes() { // Esegue: void stopCancelsInflightOperationsAndRejectsNewOnes() {
        RemoteTaskScheduler scheduler = new RemoteTaskScheduler(2); // Esegue: RemoteTaskScheduler scheduler = new RemoteTaskScheduler(2);

        CompletableFuture<String> pendingOne = new CompletableFuture<>(); // Esegue: CompletableFuture<String> pendingOne = new CompletableFuture<>();
        CompletableFuture<String> pendingTwo = new CompletableFuture<>(); // Esegue: CompletableFuture<String> pendingTwo = new CompletableFuture<>();
        CompletableFuture<String> first = scheduler.schedule(() -> pendingOne); // Esegue: CompletableFuture<String> first = scheduler.schedule(() -> pendingOne);
        CompletableFuture<String> second = scheduler.schedule(() -> pendingTwo); // Esegue: CompletableFuture<String> second = scheduler.schedule(() -> pendingTwo);

        scheduler.stop(); // Esegue: scheduler.stop();

        assertTrue(scheduler.isStopped()); // Esegue: assertTrue(scheduler.isStopped());
        assertTrue(pendingOne.isCancelled()); // Esegue: assertTrue(pendingOne.isCancelled());
        assertTrue(pendingTwo.isCancelled()); // Esegue: assertTrue(pendingTwo.isCancelled());
        assertTrue(first.isCancelled()); // Esegue: assertTrue(first.isCancelled());
        assertTrue(second.isCancelled()); // Esegue: assertTrue(second.isCancelled());

        CompletableFuture<String> rejected = scheduler.schedule(() -> CompletableFuture.completedFuture("late")); // Esegue: CompletableFuture<String> rejected = scheduler.schedule(() -> CompletableFuture.completedFuture("late"));
        assertTrue(rejected.isCompletedExceptionally()); // Esegue: assertTrue(rejected.isCompletedExceptionally());
        CompletionException cancellation = assertThrows(CompletionException.class, rejected::join); // Esegue: CompletionException cancellation = assertThrows(CompletionException.class, rejected::join);
        assertTrue(cancellation.getCause() instanceof CancellationException); // Esegue: assertTrue(cancellation.getCause() instanceof CancellationException);
    } // Esegue: }

    @Test // Esegue: @Test
    void supplierExceptionReleasesSemaphorePermit() { // Esegue: void supplierExceptionReleasesSemaphorePermit() {
        RemoteTaskScheduler scheduler = new RemoteTaskScheduler(1); // Esegue: RemoteTaskScheduler scheduler = new RemoteTaskScheduler(1);

        CompletableFuture<String> failing = scheduler.schedule(() -> { // Esegue: CompletableFuture<String> failing = scheduler.schedule(() -> {
            throw new IllegalStateException("boom"); // Esegue: throw new IllegalStateException("boom");
        }); // Esegue: });
        CompletionException failure = assertThrows(CompletionException.class, failing::join); // Esegue: CompletionException failure = assertThrows(CompletionException.class, failing::join);
        assertTrue(failure.getCause() instanceof IllegalStateException); // Esegue: assertTrue(failure.getCause() instanceof IllegalStateException);

        CompletableFuture<String> success = scheduler.schedule(() -> CompletableFuture.completedFuture("ok")); // Esegue: CompletableFuture<String> success = scheduler.schedule(() -> CompletableFuture.completedFuture("ok"));
        assertEquals("ok", success.join()); // Esegue: assertEquals("ok", success.join());
    } // Esegue: }

    @Test // Esegue: @Test
    void nullTaskSupplierIsRejected() { // Esegue: void nullTaskSupplierIsRejected() {
        RemoteTaskScheduler scheduler = new RemoteTaskScheduler(1); // Esegue: RemoteTaskScheduler scheduler = new RemoteTaskScheduler(1);
        assertThrows(NullPointerException.class, () -> scheduler.schedule(null)); // Esegue: assertThrows(NullPointerException.class, () -> scheduler.schedule(null));
    } // Esegue: }

    @Test // Esegue: @Test
    void nullFutureFromSupplierCancelsPermit() { // Esegue: void nullFutureFromSupplierCancelsPermit() {
        RemoteTaskScheduler scheduler = new RemoteTaskScheduler(1); // Esegue: RemoteTaskScheduler scheduler = new RemoteTaskScheduler(1);

        CompletableFuture<String> rejected = scheduler.schedule(() -> null); // Esegue: CompletableFuture<String> rejected = scheduler.schedule(() -> null);
        CompletionException failure = assertThrows(CompletionException.class, rejected::join); // Esegue: CompletionException failure = assertThrows(CompletionException.class, rejected::join);
        assertTrue(failure.getCause() instanceof IllegalStateException); // Esegue: assertTrue(failure.getCause() instanceof IllegalStateException);

        CompletableFuture<String> success = scheduler.schedule(() -> CompletableFuture.completedFuture("ok")); // Esegue: CompletableFuture<String> success = scheduler.schedule(() -> CompletableFuture.completedFuture("ok"));
        assertEquals("ok", success.join()); // Esegue: assertEquals("ok", success.join());
    } // Esegue: }
} // Esegue: }
