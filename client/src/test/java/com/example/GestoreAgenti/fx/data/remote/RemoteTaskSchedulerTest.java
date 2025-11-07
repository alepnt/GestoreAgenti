package com.example.GestoreAgenti.fx.data.remote;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*;

class RemoteTaskSchedulerTest {

    @Test
    void constructorRejectsNonPositiveConcurrency() {
        assertThrows(IllegalArgumentException.class, () -> new RemoteTaskScheduler(0));
        assertThrows(IllegalArgumentException.class, () -> new RemoteTaskScheduler(-2));
    }

    @Test
    void scheduleFailsWhenSemaphoreLimitReached() {
        RemoteTaskScheduler scheduler = new RemoteTaskScheduler(1);

        CompletableFuture<String> firstPending = new CompletableFuture<>();
        CompletableFuture<String> firstScheduled = scheduler.schedule(() -> firstPending);
        assertNotSame(firstPending, firstScheduled);
        assertFalse(firstPending.isDone());
        assertFalse(firstScheduled.isDone());

        CompletableFuture<String> secondAttempt = scheduler.schedule(CompletableFuture::new);
        assertTrue(secondAttempt.isCompletedExceptionally());
        CompletionException overflow = assertThrows(CompletionException.class, secondAttempt::join);
        assertTrue(overflow.getCause() instanceof IllegalStateException);

        firstPending.complete("done");
        assertEquals("done", firstScheduled.join());

        CompletableFuture<String> third = scheduler.schedule(() -> CompletableFuture.completedFuture("ok"));
        assertEquals("ok", third.join());
    }

    @Test
    void stopCancelsInflightOperationsAndRejectsNewOnes() {
        RemoteTaskScheduler scheduler = new RemoteTaskScheduler(2);

        CompletableFuture<String> pendingOne = new CompletableFuture<>();
        CompletableFuture<String> pendingTwo = new CompletableFuture<>();
        CompletableFuture<String> first = scheduler.schedule(() -> pendingOne);
        CompletableFuture<String> second = scheduler.schedule(() -> pendingTwo);

        scheduler.stop();

        assertTrue(scheduler.isStopped());
        assertTrue(pendingOne.isCancelled());
        assertTrue(pendingTwo.isCancelled());
        assertTrue(first.isCancelled());
        assertTrue(second.isCancelled());

        CompletableFuture<String> rejected = scheduler.schedule(() -> CompletableFuture.completedFuture("late"));
        assertTrue(rejected.isCompletedExceptionally());
        CompletionException cancellation = assertThrows(CompletionException.class, rejected::join);
        assertTrue(cancellation.getCause() instanceof CancellationException);
    }

    @Test
    void supplierExceptionReleasesSemaphorePermit() {
        RemoteTaskScheduler scheduler = new RemoteTaskScheduler(1);

        CompletableFuture<String> failing = scheduler.schedule(() -> {
            throw new IllegalStateException("boom");
        });
        CompletionException failure = assertThrows(CompletionException.class, failing::join);
        assertTrue(failure.getCause() instanceof IllegalStateException);

        CompletableFuture<String> success = scheduler.schedule(() -> CompletableFuture.completedFuture("ok"));
        assertEquals("ok", success.join());
    }

    @Test
    void nullTaskSupplierIsRejected() {
        RemoteTaskScheduler scheduler = new RemoteTaskScheduler(1);
        assertThrows(NullPointerException.class, () -> scheduler.schedule(null));
    }

    @Test
    void nullFutureFromSupplierCancelsPermit() {
        RemoteTaskScheduler scheduler = new RemoteTaskScheduler(1);

        CompletableFuture<String> rejected = scheduler.schedule(() -> null);
        CompletionException failure = assertThrows(CompletionException.class, rejected::join);
        assertTrue(failure.getCause() instanceof IllegalStateException);

        CompletableFuture<String> success = scheduler.schedule(() -> CompletableFuture.completedFuture("ok"));
        assertEquals("ok", success.join());
    }
}
