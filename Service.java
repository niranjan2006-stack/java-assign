package com.example.devops;

import java.util.concurrent.atomic.AtomicInteger;

public class Service {
    private static final AtomicInteger ID_GEN = new AtomicInteger(1000);

    private final int serviceId;
    private String name;
    private String repo;
    private String currentVersion;
    private String ownerTeam;

    public Service(String name, String repo, String currentVersion, String ownerTeam) {
        this.serviceId = ID_GEN.getAndIncrement();
        this.name = name;
        this.repo = repo;
        this.currentVersion = currentVersion;
        this.ownerTeam = ownerTeam;
    }

    public int getServiceId() { return serviceId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRepo() { return repo; }
    public void setRepo(String repo) { this.repo = repo; }
    public String getCurrentVersion() { return currentVersion; }
    public void setCurrentVersion(String currentVersion) { this.currentVersion = currentVersion; }
    public String getOwnerTeam() { return ownerTeam; }
    public void setOwnerTeam(String ownerTeam) { this.ownerTeam = ownerTeam; }

    public String shortInfo() {
        return String.format("[%d] %s (ver=%s) owner=%s", serviceId, name, currentVersion, ownerTeam);
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId=" + serviceId +
                ", name='" + name + '\'' +
                ", repo='" + repo + '\'' +
                ", currentVersion='" + currentVersion + '\'' +
                ", ownerTeam='" + ownerTeam + '\'' +
                '}';
    }
}
