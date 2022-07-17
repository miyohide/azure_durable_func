package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Duration;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown=true)
public class JobInfo {
    private String jobId = "1";

    public String getJobId() {
        return jobId;
    }

    public Instant getExpirationTime() {
        return Instant.now().plusSeconds(3_600L);
    }

    public Duration getPollingDelay() {
        return Duration.ofSeconds(10L);
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
