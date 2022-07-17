package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.microsoft.durabletask.DataConverter;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown=true)
public class JobInfo implements DataConverter {
    private String jobId;

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

    @javax.annotation.Nullable
    @Override
    public String serialize(@javax.annotation.Nullable Object value) {
        return null;
    }

    @javax.annotation.Nullable
    @Override
    public <T> T deserialize(@javax.annotation.Nullable String data, Class<T> target) {
        T obj = null;
        try {
            obj = target.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
}
