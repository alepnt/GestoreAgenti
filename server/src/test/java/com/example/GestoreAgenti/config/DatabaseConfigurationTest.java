package com.example.GestoreAgenti.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.Test;

class DatabaseConfigurationTest {

    @Test
    void applicationPropertiesAreConfiguredForSqlServerWithSqlAuthentication() throws Exception {
        Properties properties = loadProperties("application.properties");

        String url = properties.getProperty("spring.datasource.url");
        assertNotNull(url, "La URL del datasource deve essere configurata");
        assertTrue(url.startsWith("jdbc:sqlserver://"),
                "La URL del datasource deve puntare a SQL Server utilizzando le credenziali applicative");
        assertTrue(url.contains("databaseName=GestoreAgenti"),
                "La URL deve includere il nome del database GestoreAgenti");
        assertTrue(url.contains("encrypt=true"), "La URL deve richiedere la connessione cifrata");
        assertTrue(url.contains("trustServerCertificate=true"),
                "La URL deve consentire l'uso di certificati auto-firmati in sviluppo");

        assertEquals("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                properties.getProperty("spring.datasource.driver-class-name"),
                "Il driver JDBC deve essere quello di Microsoft SQL Server");

        assertEquals("${DB_USERNAME:gestore_app}", properties.getProperty("spring.datasource.username"),
                "Il nome utente SQL Server deve avere un valore di default sensato");

        assertEquals("${DB_PASSWORD:CambiaSubito!}", properties.getProperty("spring.datasource.password"),
                "La password SQL Server deve avere un valore di default sensato");

        assertEquals("org.hibernate.dialect.SQLServerDialect",
                properties.getProperty("spring.jpa.properties.hibernate.dialect"),
                "Il dialetto Hibernate deve essere configurato per SQL Server");
    }

    private Properties loadProperties(String name) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(name)) {
            assertNotNull(inputStream, () -> "Impossibile trovare il file di configurazione " + name);
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        }
    }
}
