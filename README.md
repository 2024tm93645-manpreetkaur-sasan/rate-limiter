# rate-limiter
A lightweight Java rate-limiting library implementing Token Bucket and Leaky Bucket algorithms with simple, fast, and thread-safe execution.

---

## Token Bucket — Overview

**How it works:**

* Tokens are added to the bucket at a fixed rate.
* Each incoming request consumes one token.
* If no tokens are available, the request is rejected.
* The bucket can store tokens up to a maximum capacity.

**Key characteristics:**

* Allows short bursts of traffic up to the token capacity.
* Enforces a long-term average request rate.

---

## Leaky Bucket — Overview

**How it works:**

* Incoming requests are added to a queue with a fixed capacity.
* The queue drains at a constant, fixed rate.
* If the queue is full, incoming requests are rejected.

**Key characteristics:**

* Produces a smooth, constant outflow of processed requests.
* Prevents bursts from overwhelming downstream systems.

---

## Comparison Table

| Aspect                | Token Bucket                                                | Leaky Bucket                                                   |
| --------------------- | ----------------------------------------------------------- | -------------------------------------------------------------- |
| **Behavior**          | Allows bursts up to token capacity; rate enforced over time | Smooth, constant processing rate; bursts are flattened         |
| **Reject Condition**  | No tokens available                                         | Queue is full                                                  |
| **Output Pattern**    | Can be bursty                                               | Always steady and predictable                                  |
| **Best Use Cases**    | APIs that allow brief spikes; user action limiting          | Protecting downstream systems from spikes; stable pipelines    |
| **Example Scenarios** | Login requests, API client calls, mobile SDK events         | Payment systems, DB write queues, ML inference, job schedulers |

---
