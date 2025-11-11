package com.example.ratelimiter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenBucketTest {

    @Test
    void testTokenBucketRefillAndConsumption() throws Exception {
        // capacity = 3, refillRatePerSecond = 1 token per second
        TokenBucket bucket = new TokenBucket(3, 1);

        // Bucket starts full → 3 tokens available
        assertTrue(bucket.tryConsume(), "1st token should be consumed");
        assertTrue(bucket.tryConsume(), "2nd token should be consumed");
        assertTrue(bucket.tryConsume(), "3rd token should be consumed");

        // Now bucket is empty → consuming should fail
        assertFalse(bucket.tryConsume(), "No tokens left, request should be rejected");

        // Wait > 1 second to allow 1 token to be refilled
        Thread.sleep(1100);

        // Should now be able to consume 1 new token
        assertTrue(bucket.tryConsume(), "Token should be available after refill");

        // After consuming again, bucket should become empty again
        assertFalse(bucket.tryConsume(), "Bucket should be empty again after consuming refilled token");
    }
}
