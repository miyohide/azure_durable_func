package com.example;

import java.time.Duration;
import java.time.Instant;

public class JobInfo {
    public String getJobId() {
        return "1";
    }

    public Instant getExpirationTime() {
        return Instant.now().plusSeconds(3_600L);
    }

    public Duration getPollingDelay() {
        return Duration.ofSeconds(1L);
    }
}
