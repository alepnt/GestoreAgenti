package com.example.GestoreAgenti.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

class DatabaseConfigurationTest {

    @Test
    void applicationPropertiesAreConfiguredForSqlServerWithIntegratedSecurity() throws Exception {
        Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));

        String url = properties.getProperty("spring.datasource.url");
        assertThat(url)
                .as("La URL del datasource deve puntare a SQL Server con autenticazione integrata")
                .startsWith("jdbc:sqlserver://")
                .contains("databaseName=GestoreAgenti")
                .contains("integratedSecurity=true");

        assertThat(properties.getProperty("spring.datasource.driver-class-name"))
                .as("Il driver JDBC deve essere quello di Microsoft SQL Server")
                .isEqualTo("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        assertThat(properties.getProperty("spring.jpa.properties.hibernate.dialect"))
                .as("Il dialetto Hibernate deve essere configurato per SQL Server")
                .isEqualTo("org.hibernate.dialect.SQLServerDialect");
    }
}
