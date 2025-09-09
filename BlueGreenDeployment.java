package com.example.devops;

import java.time.LocalDateTime;
import java.util.Random;

public class BlueGreenDeployment extends Deployment {
    private String blueVersion;
    private String greenVersion;
    private boolean swapped = false;

    public BlueGreenDeployment(Service service, Environment environment, String version) {
        super(service, environment, version);
        this.blueVersion = service.getCurrentVersion();
        this.greenVersion = version;
    }

    @Override
    public void deploy(String version) {
        this.greenVersion = version;
        setState(DeploymentState.DEPLOYING);
        notes.add("Staged green " + version);
    }

    @Override
    public boolean healthCheck() {
        boolean ok = new Random().nextInt(100) > 5;
        setState(ok ? DeploymentState.HEALTHY : DeploymentState.FAILED);
        return ok;
    }

    public void swap() {
        if (getState() == DeploymentState.HEALTHY) {
            swapped = true;
            promote();
            notes.add("Swapped traffic to green at " + LocalDateTime.now());
        }
    }

    @Override
    public void rollback() {
        setState(DeploymentState.ROLLED_BACK);
        if (swapped) {
            service.setCurrentVersion(blueVersion);
            swapped = false;
        }
        notes.add("Rolled back to blue " + blueVersion);
    }
}
