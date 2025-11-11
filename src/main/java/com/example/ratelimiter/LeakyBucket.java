package com.example.ratelimiter;

/**
 * Leaky Bucket Rate Limiter (Queue + Fixed Processing Rate)
 * The algorithm models a queue of pending requests that are processed at a fixed, constant rate.
 * If too many requests accumulate, new requests are rejected.
 * - queuedRequests: how many requests are currently waiting to be processed.
 * - capacity: maximum number of requests allowed to wait in the queue.
 * - processRatePerSecond: how many requests the system can process per second.
 * - lastProcessTimestamp: the last time we processed pending requests.
 * How it works:
 * 1. When a new request arrives, we first calculate how much time has passed
 *    since we last processed pending requests.
 * 2. Based on the elapsed time, we determine how many queued requests would
 *    have been processed naturally in that period and remove them from the
 *    queue.
 * 3. After processing pending requests, if the queue still has capacity,
 *    the new request is accepted. Otherwise, it is rejected.
 * This ensures that requests are allowed in bursts but are always processed
 * at a smooth, predictable rate—preventing downstream overload.
 */
public class LeakyBucket {

    private final long capacity;                // Maximum pending requests
    private final long processRatePerSecond;    // Requests processed per second

    private long queuedRequests = 0;            // Current pending requests
    private long lastProcessTimestamp;          // Last time we processed pending requests

    public LeakyBucket(long capacity, long processRatePerSecond) {
        this.capacity = capacity;
        this.processRatePerSecond = processRatePerSecond;
        this.lastProcessTimestamp = System.currentTimeMillis();
    }

    /**
     * Removes requests from the queue based on how much time has passed.
     * This simulates the system processing pending requests at a fixed rate.
     */
    private void processPendingRequests() {
        long now = System.currentTimeMillis();
        long elapsedMs = now - lastProcessTimestamp;

        long processed = (elapsedMs * processRatePerSecond) / 1000;

        if (processed > 0) {
            queuedRequests = Math.max(0, queuedRequests - processed);
            lastProcessTimestamp = now;
        }
    }

    /**
     * Attempts to add a new request to the queue.
     *
     * @return true if the request is accepted, false if rate-limited.
     */
    public synchronized boolean tryAdd() {
        processPendingRequests();  // Update queue before deciding

        if (queuedRequests < capacity) {
            queuedRequests++;
            return true;
        }
        return false;  // Queue full → reject
    }
}
