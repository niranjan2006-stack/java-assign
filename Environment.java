package com.example.devops;

import java.util.*;

enum EnvironmentStatus { UP, DEGRADED, DOWN }

public class Environment {
    private String envName;
    private String url;
    private int capacity;
    private EnvironmentStatus status;
    private final List<String> deployedVersions = new ArrayList<>();

    public Environment(String envName, String url, int capacity, EnvironmentStatus status) {
        this.envName = envName;
        this.url = url;
        this.capacity = capacity;
        this.status = status;
    }

    public String getEnvName() { return envName; }
    public void setEnvName(String envName) { this.envName = envName; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public EnvironmentStatus getStatus() { return status; }
    public void setStatus(EnvironmentStatus status) { this.status = status; }

    public void addDeployedVersion(String version) {
        deployedVersions.add(version);
    }

    public List<String> getDeployedVersions() {
        return Collections.unmodifiableList(deployedVersions);
    }

    public String dashboard() {
        return String.format("Env[%s] url=%s capacity=%d status=%s deployed=%s",
                envName, url, capacity, status, deployedVersions);
    }
}
