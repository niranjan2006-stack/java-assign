package com.example.devops;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

enum DeploymentState { PENDING, DEPLOYING, HEALTHY, ROLLED_BACK, FAILED, PROMOTED }

public abstract class Deployment {
    private static final AtomicInteger ID_GEN = new AtomicInteger(5000);

    protected final int deployId;
    protected final Service service;
    protected final Environment environment;
    protected String version;
    protected DeploymentState state;
    protected final LocalDateTime timestamp;
    protected List<String> notes = new ArrayList<>();

    public Deployment(Service service, Environment environment, String version) {
        this.deployId = ID_GEN.getAndIncrement();
        this.service = service;
        this.environment = environment;
        this.version = version;
        this.state = DeploymentState.PENDING;
        this.timestamp = LocalDateTime.now();
    }

    public int getDeployId() { return deployId; }
    public Service getService() { return service; }
    public Environment getEnvironment() { return environment; }
    public String getVersion() { return version; }
    public DeploymentState getState() { return state; }
    public LocalDateTime getTimestamp() { return timestamp; }

    protected void setState(DeploymentState newState) {
        this.state = newState;
    }

    public void deploy(String version) {
        System.out.printf("Deployment[%d]: deploying %s to %s%n", deployId, version, environment.getEnvName());
        this.version = version;
        setState(DeploymentState.DEPLOYING);
        environment.addDeployedVersion(version);
        notes.add("Deployed version " + version + " at " + LocalDateTime.now());
    }

    public void deploy(String version, int trafficPercent) {
        System.out.printf("Deployment[%d]: %s to %s with %d%% traffic%n", deployId, version, environment.getEnvName(), trafficPercent);
        this.version = version;
        setState(DeploymentState.DEPLOYING);
        notes.add("Deployed with traffic=" + trafficPercent + "%");
    }

    public void rollback() {
        setState(DeploymentState.ROLLED_BACK);
        notes.add("Rolled back at " + LocalDateTime.now());
    }

    public boolean healthCheck() {
        boolean ok = new Random().nextInt(100) > 10;
        setState(ok ? DeploymentState.HEALTHY : DeploymentState.FAILED);
        return ok;
    }

    public String releaseNotes() {
        return String.format("Deployment[%d] service=%s env=%s version=%s state=%s",
                deployId, service.getName(), environment.getEnvName(), version, state);
    }

    public void promote() {
        setState(DeploymentState.PROMOTED);
        environment.addDeployedVersion(version);
        service.setCurrentVersion(version);
    }
}
