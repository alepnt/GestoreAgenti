package com.example.GestoreAgenti.security; // Definisce il pacchetto com.example.GestoreAgenti.security a cui appartiene questa classe.

import java.nio.charset.StandardCharsets; // Importa StandardCharsets per convertire la chiave in byte sicuri.
import java.security.Key; // Importa Key per rappresentare la chiave segreta utilizzata nei token.
import java.security.MessageDigest; // Importa MessageDigest per rinforzare chiavi troppo corte.
import java.security.NoSuchAlgorithmException; // Importa NoSuchAlgorithmException per gestire gli algoritmi di hash non disponibili.
import java.util.Arrays; // Importa Arrays per eseguire operazioni di riempimento sui vettori di byte.
import java.util.Date; // Importa Date per impostare la scadenza temporale del token.
import java.util.Objects; // Importa Objects per validare l'input configurato.

import org.springframework.beans.factory.annotation.Value; // Importa Value per iniettare il valore della configurazione nel bean.
import org.springframework.stereotype.Component; // Importa Component per registrare la classe come componente Spring.

import io.jsonwebtoken.Claims; // Importa Claims per leggere le informazioni presenti nel token JWT.
import io.jsonwebtoken.Jwts; // Importa Jwts per costruire e interpretare i token JWT.
import io.jsonwebtoken.SignatureAlgorithm; // Importa SignatureAlgorithm per definire l'algoritmo di firma dei token JWT.
import io.jsonwebtoken.security.Keys; // Importa Keys per generare la chiave crittografica del token.
import io.jsonwebtoken.io.Decoders; // Importa Decoders per supportare chiavi codificate in Base64.

@Component // Applica l'annotazione @Component per configurare il componente.
public class JwtUtil { // Dichiara la classe JwtUtil che incapsula la logica del dominio.

    @Value("${jwt.secret}") // Applica l'annotazione @Value per configurare il componente.
    private String secret; // Memorizza la chiave segreta dell'entità.

    @Value("${jwt.expiration}") // Applica l'annotazione @Value per configurare il componente.
    private Long expirationMillis; // Memorizza il tempo di scadenza in millisecondi dell'entità.

    private Key getSigningKey() { // Restituisce la chiave crittografica utilizzata per firmare e verificare i token JWT.
        byte[] keyBytes = resolveKeyBytes(secret);
        return Keys.hmacShaKeyFor(keyBytes); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    private byte[] resolveKeyBytes(String configuredSecret) { // Converte il segreto configurato in una chiave sicura per HS256.
        String value = Objects.requireNonNull(configuredSecret, "JWT secret must be configured").trim();
        if (value.isEmpty()) {
            throw new IllegalStateException("JWT secret must not be empty");
        }

        try { // Gestisce il caso in cui il segreto venga fornito in Base64.
            byte[] decoded = Decoders.BASE64.decode(value);
            if (decoded.length >= 32) {
                return decoded;
            }
        } catch (IllegalArgumentException ignored) {
            // Il valore non è Base64 valido, prosegue con la rappresentazione UTF-8.
        }

        byte[] rawBytes = value.getBytes(StandardCharsets.UTF_8);
        if (rawBytes.length >= 32) {
            return rawBytes;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(rawBytes);
        } catch (NoSuchAlgorithmException ex) {
            byte[] padded = Arrays.copyOf(rawBytes, 32);
            for (int i = rawBytes.length; i < padded.length; i++) {
                padded[i] = (byte) i;
            }
            return padded;
        }
    }

    public String generateToken(String username) { // Genera un token JWT firmato per l'utente autenticato.
        return Jwts.builder() // Avvia il builder responsabile della creazione del token JWT.
                .setSubject(username) // Imposta lo username come soggetto del token JWT.
                .setIssuedAt(new Date()) // Registra la data di emissione del token JWT.
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) // Definisce la data di scadenza del token JWT.
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firma il token utilizzando la chiave segreta e l'algoritmo HS256.
                .compact(); // Esegue questa istruzione come parte della logica del metodo.
    } // Chiude il blocco di codice precedente.

    public String extractUsername(String token) { // Estrae il nome utente dal token JWT.
        Claims claims = Jwts.parserBuilder() // Inizia la costruzione del parser JWT per leggere il token.
                .setSigningKey(getSigningKey()) // Configura la chiave di firma usata per validare il token ricevuto.
                .build() // Finalizza il parser JWT configurato.
                .parseClaimsJws(token) // Analizza il token JWT per estrarre le relative claims.
                .getBody(); // Recupera il corpo delle claims ottenute dal token.
        return claims.getSubject(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
