package com.example.GestoreAgenti.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

class DatabaseConfigurationTest {

    @Test
    void applicationPropertiesAreConfiguredForSqlServerWithSqlAuthentication() throws Exception {
        Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));

        String url = properties.getProperty("spring.datasource.url");
        assertThat(url)
                .as("La URL del datasource deve puntare a SQL Server utilizzando le credenziali applicative")
                .startsWith("jdbc:sqlserver://")
                .contains("databaseName=GestoreAgenti")
                .contains("encrypt=true")
                .contains("trustServerCertificate=true");

        assertThat(properties.getProperty("spring.datasource.driver-class-name"))
                .as("Il driver JDBC deve essere quello di Microsoft SQL Server")
                .isEqualTo("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        assertThat(properties.getProperty("spring.datasource.username"))
                .as("Il nome utente SQL Server deve avere un valore di default sensato")
                .isEqualTo("${DB_USERNAME:gestore_app}");

        assertThat(properties.getProperty("spring.datasource.password"))
                .as("La password SQL Server deve avere un valore di default sensato")
                .isEqualTo("${DB_PASSWORD:CambiaSubito!}");

        assertThat(properties.getProperty("spring.jpa.properties.hibernate.dialect"))
                .as("Il dialetto Hibernate deve essere configurato per SQL Server")
                .isEqualTo("org.hibernate.dialect.SQLServerDialect");
    }
}
