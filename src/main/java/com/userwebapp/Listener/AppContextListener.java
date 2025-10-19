package com.userwebapp.Listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Cleans up JDBC drivers and MySQL cleanup threads on webapp shutdown
 * to prevent Tomcat memory-leak warnings.
 */
// @WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("AppContextListener initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("AppContextListener destroying context—cleaning up JDBC drivers and threads");

        // 1) Deregister only drivers this webapp loaded
        ClassLoader webappClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            // Only deregister drivers registered by this webapp’s ClassLoader
            if (driver.getClass().getClassLoader() == webappClassLoader) {
                try {
                    System.out.println("Deregistering JDBC driver: " + driver);
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                    System.err.println("Error deregistering driver " + driver + ": " + e);
                }
            } else {
                System.out.println("Skipping JDBC driver (not from this webapp): " + driver);
            }
        }

        // 2) Shut down MySQL's AbandonedConnectionCleanupThread if present
        try {
            Class<?> cleanupCls = Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread");
            // try the modern checkedShutdown()
            try {
                Method m = cleanupCls.getMethod("checkedShutdown");
                System.out.println("Invoking AbandonedConnectionCleanupThread.checkedShutdown()");
                m.invoke(null);
            } catch (NoSuchMethodException nsme) {
                // fallback to no-arg shutdown()
                try {
                    Method m2 = cleanupCls.getMethod("shutdown");
                    System.out.println("Invoking AbandonedConnectionCleanupThread.shutdown()");
                    m2.invoke(null);
                } catch (NoSuchMethodException e) {
                    System.out.println("No shutdown methods found on AbandonedConnectionCleanupThread");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL AbandonedConnectionCleanupThread class not found (skipping)");
        } catch (Exception e) {
            System.err.println("Error shutting down MySQL cleanup thread: " + e);
        }
    }
}
