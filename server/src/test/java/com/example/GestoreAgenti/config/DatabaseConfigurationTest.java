package com.example.GestoreAgenti.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class DatabaseConfigurationTest {

    public static void main(String[] args) throws Exception {
        new DatabaseConfigurationTest().applicationPropertiesAreConfiguredForSqlServerWithSqlAuthentication();
    }

    void applicationPropertiesAreConfiguredForSqlServerWithSqlAuthentication() throws Exception {
        Properties properties = loadProperties("application.properties");

        String url = properties.getProperty("spring.datasource.url");
        requireNonNull(url, "La URL del datasource deve essere configurata");
        require(url.startsWith("jdbc:sqlserver://"),
                "La URL del datasource deve puntare a SQL Server utilizzando le credenziali applicative");
        require(url.contains("databaseName=GestoreAgenti"),
                "La URL deve includere il nome del database GestoreAgenti");
        require(url.contains("encrypt=true"), "La URL deve richiedere la connessione cifrata");
        require(url.contains("trustServerCertificate=true"),
                "La URL deve consentire l'uso di certificati auto-firmati in sviluppo");

        requireEquals("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                properties.getProperty("spring.datasource.driver-class-name"),
                "Il driver JDBC deve essere quello di Microsoft SQL Server");

        requireEquals("${DB_USERNAME:gestore_app}", properties.getProperty("spring.datasource.username"),
                "Il nome utente SQL Server deve avere un valore di default sensato");

        requireEquals("${DB_PASSWORD:CambiaSubito!}", properties.getProperty("spring.datasource.password"),
                "La password SQL Server deve avere un valore di default sensato");

        requireEquals("org.hibernate.dialect.SQLServerDialect",
                properties.getProperty("spring.jpa.properties.hibernate.dialect"),
                "Il dialetto Hibernate deve essere configurato per SQL Server");
    }

    private Properties loadProperties(String name) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(name)) {
            requireNonNull(inputStream, "Impossibile trovare il file di configurazione " + name);
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void requireEquals(String expected, String actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " (atteso: " + expected + ", trovato: " + actual + ")");
        }
    }

    private static void requireNonNull(Object value, String message) {
        if (value == null) {
            throw new AssertionError(message);
        }
    }
}
