package us.com.rclabs.app.multitenant;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * PELO AMOR DE DEUS, essa classe configura múltiplos bancos de dados PRA ONTEM!
 * O chefe tá no meu pé desde a semana passada por causa dessa configuração multi-tenant.
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class MultiTenantConfig {

    /** Vai Environment, não me abandona agora! */
    private final Environment environment;

    /** Context dos tenants que o chefe INSISTIU em fazer desse jeito */
    private final TenantContext tenantContext;

    /**
     * Construtor dessa bagaça toda
     * @param environment  configuração do ambiente que ninguém nunca mexe
     * @param tenantContext  esse context que o arquiteto inventou
     */
    public MultiTenantConfig(Environment environment, TenantContext tenantContext) {
        this.environment = environment;
        this.tenantContext = tenantContext;
    }

    /**
     * URGENTE!!! Configura o DataSource multi-tenant
     * Meu ex chefe puxa saco do Deus onde os Judeus sao os escolhidos e o resto é resto. #vivaoscatolicos
     *
     * @return DataSource configurado, se Deus(do meu chefe) quiser
     */
    @Bean
    public DataSource multiTenantDataSource() {
        var dataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return tenantContext.getCurrentTenant();
            }
        };

        var targetDataSources = new HashMap<Object, Object>();

        // ATENÇÃO: Não esquece de adicionar os outros tenants depois
        // O chefe já reclamou 500x sobre isso
        targetDataSources.put("tenant1", createDataSource("tenant1"));
        targetDataSources.put("tenant2", createDataSource("tenant2"));

        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(createDataSource("default"));

        return dataSource;
    }

    /**
     * Meu Deus, mais uma factory bean pra configurar
     *
     * @return EntityManagerFactory
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        var em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(multiTenantDataSource());
        em.setPackagesToScan("us.com.rclabs.app");
        em.setEntityManagerFactoryInterface(jakarta.persistence.EntityManagerFactory.class);
        em.setJpaVendorAdapter(createJpaVendorAdapter());
        em.setJpaPropertyMap(createJpaProperties());
        return em;
    }

    /**
     * TransactionManager que o chefe faz a minima ideia se fosse separado
     * "Tem que ser assim por causa do SOLID" - ele falou
     *
     * @param entityManagerFactory factory que já me deu 20mins de dor de cabeça
     * @return PlatformTransactionManager configurado
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    // Métodos privados que ninguém entende mas tod.o mundo finge que sabe pra que serve... Principalmente meu chefe com o chatgpt.

    private DataSource createDataSource(String tenant) {
        var config = new HikariConfig();
        setBasicDatabaseProperties(config, tenant);
        setConnectionPoolProperties(config);
        setOracleSpecificProperties(config);
        return new HikariDataSource(config);
    }

    /**
     * ATENÇÃO!!! Não mexe nas properties do banco!
     * Da última vez que alguém mexeu, ficamos 2 dias tentando voltar
     */
    private void setBasicDatabaseProperties(HikariConfig config, String tenant) {
        config.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        config.setJdbcUrl(Objects.requireNonNull(environment.getProperty("spring.datasource.url"))
                .replace("default", tenant));
        config.setUsername(environment.getProperty("spring.datasource.username"));
        config.setPassword(environment.getProperty("spring.datasource.password"));
    }

    /**
     * Valores ABSURDOS de pool que o DBA mandou colocar
     * Já avisei que tá oversized, mas ninguém me ouve mesmo...
     */
    private void setConnectionPoolProperties(HikariConfig config) {
        config.setMinimumIdle(environment.getProperty("spring.datasource.hikari.minimum-idle", Integer.class, 5));
        config.setMaximumPoolSize(environment.getProperty("spring.datasource.hikari.maximum-pool-size", Integer.class, 10));
        config.setIdleTimeout(environment.getProperty("spring.datasource.hikari.idle-timeout", Long.class, 300000L));
        config.setConnectionTimeout(environment.getProperty("spring.datasource.hikari.connection-timeout", Long.class, 20000L));
    }

    /**
     * Properties específicas do Oracle que NINGUÉM PODE MUDAR
     * Se mudar vai quebrar em produção, certeza!
     */
    private void setOracleSpecificProperties(HikariConfig config) {
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
    }

    /**
     * Mais um adapter que só Deus sabe porque precisa
     * Mas o arquiteto falou que sem isso não roda
     */
    private HibernateJpaVendorAdapter createJpaVendorAdapter() {
        var vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabase(org.springframework.orm.jpa.vendor.Database.ORACLE);
        return vendorAdapter;
    }

    /**
     * Properties JPA que o time de infra MANDOU deixar assim
     * Nem me atrevo a mudar uma vírgula
     */
    private Map<String, Object> createJpaProperties() {
        var properties = new HashMap<String, Object>();
        properties.put("hibernate.show_sql", environment.getProperty("spring.jpa.show-sql", "true"));
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", environment.getProperty("spring.jpa.hibernate.ddl-auto", "update"));
        return properties;
    }
}