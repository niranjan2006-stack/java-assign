package com.example.devops;

import java.time.LocalDateTime;
import java.util.*;

public class DevOpsManager {
    private final Map<Integer, Deployment> deployments = new LinkedHashMap<>();
    private final List<String> releaseHistory = new ArrayList<>();

    public String build(Service svc, String version) {
        String buildId = svc.getName() + "-" + new Random().nextInt(10000);
        String msg = "Built " + svc.getName() + ":" + version + " build=" + buildId;
        System.out.println(msg);
        return buildId;
    }

    public void deploy(Deployment d) {
        d.deploy(d.getVersion());
        deployments.put(d.getDeployId(), d);
    }

    public void rollback(int deployId) {
        Deployment d = deployments.get(deployId);
        if (d != null) d.rollback();
    }

    public void runHealthChecks() {
        for (Deployment d : deployments.values()) {
            boolean ok = d.healthCheck();
            if (!ok) d.rollback();
            releaseHistory.add(d.releaseNotes() + " at " + LocalDateTime.now());
        }
    }

    public void printReleaseHistory() {
        releaseHistory.forEach(System.out::println);
    }

    public void printEnvironmentDashboard(Environment env) {
        System.out.println(env.dashboard());
    }
}
