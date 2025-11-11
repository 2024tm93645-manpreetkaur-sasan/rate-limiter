package com.example.ratelimiter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LeakyBucketTest {

    @Test
    void testLeakyBucketProcessingAndCapacity() throws Exception {
        // capacity = 2, processRatePerSecond = 1
        LeakyBucket bucket = new LeakyBucket(2, 1);

        // First two should be accepted (queue size = 2)
        assertTrue(bucket.tryAdd(), "First request should be accepted");
        assertTrue(bucket.tryAdd(), "Second request should be accepted");

        // Third should be rejected because capacity is 2
        assertFalse(bucket.tryAdd(), "Third request should be rejected due to full queue");

        // Wait long enough for 1 queued request to be processed
        Thread.sleep(1100);

        // Now 1 slot should be free → request should be accepted
        assertTrue(bucket.tryAdd(), "A request should be accepted after processing frees space");

        // Optional: Queue should now be full again (2 total)
        // Add new request: should be rejected again
        assertFalse(bucket.tryAdd(), "Queue should be full again after refill");
    }
}
