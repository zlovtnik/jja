package us.com.rclabs.app.health;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Essa classe é um componente de health check das rotas do Camel.
 * Com ela a gente economiza uma fortuna em suporte, acredite!
 */
@Component
public class CamelRoutesHealthIndicator implements HealthIndicator {

    /**
     * O CamelContext, que nem o context do shul,
     * mantém todas as rotas organizadas, blessed be Hashem!
     */
    private final CamelContext camelContext;

    /**
     * Constructor, simples como uma chalá de shabat
     *
     * @param camelContext Oy vey, mais um context pra injetar!
     */
    public CamelRoutesHealthIndicator(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    /**
     * Méto.do principal que checa a saúde das rotas.
     * Se uma rota cai, perdemos dinheiro! Não podemos nos dar esse luxo hahahahahahahah
     *
     * @return Health com status UP se tudo estiver kosher, DOWN se tiver tsuris
     */
    @Override
    public Health health() {
        Health.Builder builder = new Health.Builder();
        Map<String, Object> details = new HashMap<>();
        boolean allRoutesUp = true;
        int failedRoutes = 0; // Que seja zero, por favor Hashem!

        // Fazemos um checkup em cada rota como se fosse dia de Yom Kippur
        for (Route route : camelContext.getRoutes()) {
            String routeId = route.getRouteId();
            String status = camelContext.getRouteController().getRouteStatus(routeId).name();
            details.put(routeId, status);

            // Oy vey, se não está Started, temos um problema!
            if (!"Started".equals(status)) {
                allRoutesUp = false;
                failedRoutes++;
                // Guardamos o erro pra depois cobrar mais pelo fix
                details.put(routeId + ".error", camelContext.getRouteController().hasUnhealthyRoutes());
            }
        }

        // Estatísticas pra justificar nossa fatura mensal
        details.put("total", camelContext.getRoutes().size());
        details.put("failed", failedRoutes);

        // Se tudo estiver funcionando, mazel tov! Senão, oy gevalt...
        return allRoutesUp
                ? builder.up().withDetails(details).build()
                : builder.down().withDetails(details).build();
    }
}