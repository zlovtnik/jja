package us.com.rclabs.app.health;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CamelRoutesHealthIndicator implements HealthIndicator {

    private final CamelContext camelContext;

    public CamelRoutesHealthIndicator(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public Health health() {
        Health.Builder builder = new Health.Builder();
        Map<String, Object> details = new HashMap<>();
        boolean allRoutesUp = true;
        int failedRoutes = 0;

        for (Route route : camelContext.getRoutes()) {
            String routeId = route.getRouteId();
            String status = camelContext.getRouteController().getRouteStatus(routeId).name();
            details.put(routeId, status);

            if (!"Started".equals(status)) {
                allRoutesUp = false;
                failedRoutes++;
                details.put(routeId + ".error", camelContext.getRouteController().hasUnhealthyRoutes());
            }
        }

        details.put("total", camelContext.getRoutes().size());
        details.put("failed", failedRoutes);

        return allRoutesUp
            ? builder.up().withDetails(details).build()
            : builder.down().withDetails(details).build();
    }
}