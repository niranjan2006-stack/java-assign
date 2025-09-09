package com.example.devops;

import java.time.LocalDateTime;
import java.util.Random;

public class CanaryDeployment extends Deployment {
    private int trafficPercent;
    private final int step;
    private boolean promotedToStable = false;

    public CanaryDeployment(Service service, Environment environment, String version, int initialTraffic, int step) {
        super(service, environment, version);
        this.trafficPercent = initialTraffic;
        this.step = step;
    }

    @Override
    public void deploy(String version, int trafficPercent) {
        this.version = version;
        this.trafficPercent = trafficPercent;
        setState(DeploymentState.DEPLOYING);
        notes.add("Canary deployed at " + trafficPercent + "% traffic");
    }

    @Override
    public boolean healthCheck() {
        boolean ok = new Random().nextInt(100) > 15;
        if (ok && trafficPercent >= 100) {
            setState(DeploymentState.HEALTHY);
        } else if (!ok) {
            setState(DeploymentState.FAILED);
        }
        return ok;
    }

    public boolean promoteStep() {
        if (getState() == DeploymentState.FAILED) return false;
        trafficPercent = Math.min(100, trafficPercent + step);
        if (trafficPercent >= 100 && !promotedToStable) {
            promote();
            promotedToStable = true;
        }
        return true;
    }

    @Override
    public void rollback() {
        setState(DeploymentState.ROLLED_BACK);
        notes.add("Canary rolled back at traffic=" + trafficPercent + "% on " + LocalDateTime.now());
    }
}
