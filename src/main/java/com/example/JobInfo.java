package com.example;

import java.time.Duration;
import java.time.Instant;

public class JobInfo {
    public String getJobId() {
        return "1";
    }

    public Instant getExpirationTime() {
        return Instant.now();
    }

    public Duration getPollingDelay() {
        return Duration.ofSeconds(1L);
    }
}
