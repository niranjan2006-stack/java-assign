package com.example.devops;

public class DevOpsAppMain {
    public static void main(String[] args) {
        Service authSvc = new Service("auth-service", "git@repo/auth.git", "1.2.0", "TeamAuth");
        Service orderSvc = new Service("order-service", "git@repo/order.git", "0.9.5", "TeamOrders");

        Environment dev = new Environment("Dev", "http://dev.local", 2, EnvironmentStatus.UP);
        Environment qa = new Environment("QA", "http://qa.local", 4, EnvironmentStatus.UP);
        Environment prod = new Environment("Prod", "https://api.example.com", 12, EnvironmentStatus.UP);

        DevOpsManager manager = new DevOpsManager();

        manager.build(authSvc, "1.3.0");
        manager.build(orderSvc, "1.0.0");

        CanaryDeployment canary = new CanaryDeployment(authSvc, prod, "1.3.0", 10, 30);
        manager.deploy(canary);

        for (int i = 0; i < 4; i++) {
            if (!canary.promoteStep()) break;
        }

        BlueGreenDeployment bg = new BlueGreenDeployment(orderSvc, qa, "1.0.0");
        manager.deploy(bg);
        if (bg.healthCheck()) bg.swap();
        else bg.rollback();

        manager.runHealthChecks();

        System.out.println("\n--- Release History ---");
        manager.printReleaseHistory();

        System.out.println("\n--- Dashboards ---");
        manager.printEnvironmentDashboard(dev);
        manager.printEnvironmentDashboard(qa);
        manager.printEnvironmentDashboard(prod);
    }
}
