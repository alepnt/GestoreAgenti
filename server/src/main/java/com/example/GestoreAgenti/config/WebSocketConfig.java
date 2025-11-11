package com.example.GestoreAgenti.config; // Definisce il pacchetto com.example.GestoreAgenti.config che contiene questa classe.

import com.example.GestoreAgenti.websocket.ChatWebSocketHandler; // Importa com.example.GestoreAgenti.websocket.ChatWebSocketHandler per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per abilitare le funzionalità utilizzate nel file.

import org.springframework.context.annotation.Configuration; // Importa org.springframework.context.annotation.Configuration per abilitare le funzionalità utilizzate nel file.
import org.springframework.lang.NonNull; // Importa org.springframework.lang.NonNull per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.socket.config.annotation.EnableWebSocket; // Importa org.springframework.web.socket.config.annotation.EnableWebSocket per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.socket.config.annotation.WebSocketConfigurer; // Importa org.springframework.web.socket.config.annotation.WebSocketConfigurer per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry; // Importa org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry per abilitare le funzionalità utilizzate nel file.

@Configuration // Applica l'annotazione @Configuration per configurare il componente.
@EnableWebSocket // Applica l'annotazione @EnableWebSocket per configurare il componente.
public class WebSocketConfig implements WebSocketConfigurer { // Definisce la classe WebSocketConfig che incapsula la logica applicativa.

    private final ChatWebSocketHandler chatWebSocketHandler; // Dichiara il campo chatWebSocketHandler dell'oggetto.

    public WebSocketConfig(@NonNull ChatWebSocketHandler chatWebSocketHandler) { // Costruttore della classe WebSocketConfig che inizializza le dipendenze necessarie.
        this.chatWebSocketHandler = Objects.requireNonNull(chatWebSocketHandler); // Aggiorna il campo chatWebSocketHandler dell'istanza.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) { // Definisce il metodo registerWebSocketHandlers che supporta la logica di dominio.
        registry.addHandler(Objects.requireNonNull(chatWebSocketHandler), "/ws/chat") // Esegue l'istruzione necessaria alla logica applicativa.
                .setAllowedOrigins("*"); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
