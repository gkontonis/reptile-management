package com.reptilemanagement.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
        return flyway;
    }

    /**
     * Makes the JPA entityManagerFactory wait for Flyway to finish before
     * it validates the schema. Without spring-boot-flyway autoconfiguration
     * (not available in this environment), this ordering must be set manually.
     */
    @Bean
    public static BeanFactoryPostProcessor flywayEntityManagerDependency() {
        return beanFactory -> {
            if (!beanFactory.containsBeanDefinition("entityManagerFactory")) {
                return;
            }
            BeanDefinition bd = beanFactory.getBeanDefinition("entityManagerFactory");
            String[] existing = bd.getDependsOn();
            String[] updated = existing == null
                    ? new String[]{"flyway"}
                    : Arrays.copyOf(existing, existing.length + 1);
            if (existing != null) updated[existing.length] = "flyway";
            bd.setDependsOn(updated);
        };
    }
}
