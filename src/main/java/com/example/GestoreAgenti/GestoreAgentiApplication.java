package com.example.GestoreAgenti; // Definisce il pacchetto com.example.GestoreAgenti a cui appartiene questa classe.

import org.springframework.boot.SpringApplication; // Importa SpringApplication per avviare il contesto Spring Boot.
import org.springframework.boot.autoconfigure.SpringBootApplication; // Importa SpringBootApplication per attivare la configurazione automatica del progetto.

@SpringBootApplication // Applica l'annotazione @SpringBootApplication per configurare il componente.
public class GestoreAgentiApplication { // Dichiara la classe GestoreAgentiApplication che incapsula la logica del dominio.

	public static void main(String[] args) { // Metodo di avvio dell'applicazione che delega a Spring Boot.
		SpringApplication.run(GestoreAgentiApplication.class, args); // Esegue questa istruzione come parte della logica del metodo.
	} // Chiude il blocco di codice precedente.

} // Chiude il blocco di codice precedente.
