package us.com.rclabs.app.multitenant;

import org.springframework.stereotype.Component;

/**
 * TenantContext class manages the current tenant information using ThreadLocal.
 * This class is used to set, get, and clear the current tenant for the current thread.
 */
@Component
public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    /**
     * Sets the current tenant for the current thread.
     *
     * @param tenant the tenant identifier to set
     */
    public void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    /**
     * Gets the current tenant for the current thread.
     *
     * @return the tenant identifier of the current thread
     */
    public String getCurrentTenant() {
        return currentTenant.get();
    }

    /**
     * Clears the current tenant for the current thread.
     */
    public void clear() {
        currentTenant.remove();
    }
}