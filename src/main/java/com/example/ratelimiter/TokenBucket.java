package com.example.ratelimiter;

/**
 * Token Bucket Rate Limiter
 *
 * This implementation uses the classic Token Bucket algorithm, where tokens
 * are added to the bucket at a fixed rate and consumed when requests arrive.
 * A request is allowed only if a token is available.
 *
 * Conceptually:
 * - availableTokens: how many tokens are currently in the bucket.
 * - capacity: maximum number of tokens the bucket can hold.
 * - refillRatePerSecond: how many tokens are added every second.
 * - lastRefillTimestamp: the last time tokens were added.
 *
 * How it works:
 * 1. Each time a request arrives (tryConsume()), we first calculate how many
 *    tokens should have been added since the last refill and update the bucket.
 * 2. If at least one token is available, we consume it and allow the request.
 * 3. If no tokens are available, the request is rejected (rate-limited).
 */
public class TokenBucket {

    private final long capacity;               // Maximum number of tokens
    private final long refillRatePerSecond;    // Tokens added per second

    private long availableTokens;              // Current number of tokens
    private long lastRefillTimestamp;          // When tokens were last refilled

    public TokenBucket(long capacity, long refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.availableTokens = capacity; // Start full
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    /**
     * Adds tokens to the bucket based on how much time has passed.
     */
    private void refillTokens() {
        long now = System.currentTimeMillis();
        long elapsedMs = now - lastRefillTimestamp;

        long tokensToAdd = (elapsedMs * refillRatePerSecond) / 1000;

        if (tokensToAdd > 0) {
            availableTokens = Math.min(capacity, availableTokens + tokensToAdd);
            lastRefillTimestamp = now;
        }
    }

    /**
     * Attempts to consume a token.
     *
     * @return true if a token was consumed (request allowed), false otherwise.
     */
    public synchronized boolean tryConsume() {
        refillTokens();

        if (availableTokens > 0) {
            availableTokens--;
            return true;
        }

        return false; // No tokens available → reject request
    }
}
