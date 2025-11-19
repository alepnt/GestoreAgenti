# Endpoints REST per Postman

Base URL: `http://localhost:8081`

## Configurazione ambiente
1. **Avviare il backend**: `mvn spring-boot:run -pl server -am` (porta 8081).
2. **SQL Server locale** (default): assicurarsi che `sqljdbc_auth.dll` sia nel `PATH` per l'autenticazione integrata.
3. **Profilo SQL con credenziali**: avviare con `-Dspring-boot.run.profiles=sql` e impostare `DB_USERNAME`/`DB_PASSWORD` o `DB_URL`.
4. **Profilo H2** (senza SQL Server): impostare `DB_URL`, `DB_DRIVER` e `DB_USERNAME` per H2 prima di avviare il server.
5. **Ambienti offline**: preparare la cache Maven con `scripts/prime-maven-cache.sh` (macchina online) e installarla con `scripts/install-maven-cache.sh offline/maven-repo.tar.gz /workspace/.m2/repository`, quindi eseguire Maven con `-o -Dmaven.repo.local=/workspace/.m2/repository`.

## Endpoints
### Autenticazione
- `POST http://localhost:8081/api/auth/login`

### Agenda
- `GET http://localhost:8081/api/agenda/dipendenti/{dipendenteId}?from=&to=`
- `POST http://localhost:8081/api/agenda/dipendenti/{dipendenteId}`
- `PUT http://localhost:8081/api/agenda/{id}`
- `DELETE http://localhost:8081/api/agenda/{id}`

### Chat di team
- `GET http://localhost:8081/api/chat/{teamName}`
- `POST http://localhost:8081/api/chat/{teamName}`

### Clienti
- `GET http://localhost:8081/api/clienti`
- `GET http://localhost:8081/api/clienti/{id}`
- `POST http://localhost:8081/api/clienti`
- `PUT http://localhost:8081/api/clienti/{id}`
- `DELETE http://localhost:8081/api/clienti/{id}`

### Servizi
- `GET http://localhost:8081/api/servizi`
- `GET http://localhost:8081/api/servizi/{id}`
- `POST http://localhost:8081/api/servizi`
- `PUT http://localhost:8081/api/servizi/{id}`
- `DELETE http://localhost:8081/api/servizi/{id}`

### Contratti
- `GET http://localhost:8081/api/contratti`
- `GET http://localhost:8081/api/contratti/storico/dipendenti/{dipendenteId}`
- `GET http://localhost:8081/api/contratti/storico/clienti/{clienteId}`
- `GET http://localhost:8081/api/contratti/storico/team?nome=`
- `GET http://localhost:8081/api/contratti/{id}`
- `POST http://localhost:8081/api/contratti`
- `PUT http://localhost:8081/api/contratti/{id}`
- `DELETE http://localhost:8081/api/contratti/{id}`
- `GET http://localhost:8081/api/contratti/{id}/pdf`

### Dipendenti
- `GET http://localhost:8081/api/dipendenti`
- `GET http://localhost:8081/api/dipendenti/{id}`
- `POST http://localhost:8081/api/dipendenti`
- `PUT http://localhost:8081/api/dipendenti/{id}`
- `DELETE http://localhost:8081/api/dipendenti/{id}`
- `GET http://localhost:8081/api/dipendenti/{id}/overview`
- `POST http://localhost:8081/api/dipendenti/{id}/teams/{teamId}`
- `GET http://localhost:8081/api/dipendenti/gerarchia`

### Pagamenti
- `GET http://localhost:8081/api/pagamenti`
- `GET http://localhost:8081/api/pagamenti/{id}`
- `POST http://localhost:8081/api/pagamenti`
- `PUT http://localhost:8081/api/pagamenti/{id}`
- `DELETE http://localhost:8081/api/pagamenti/{id}`
- `POST http://localhost:8081/api/pagamenti/{id}/elabora`
- `POST http://localhost:8081/api/pagamenti/{id}/completa`
- `POST http://localhost:8081/api/pagamenti/{id}/fallisci`
- `POST http://localhost:8081/api/pagamenti/{id}/ripeti`

### Fatture
- `GET http://localhost:8081/api/fatture`
- `GET http://localhost:8081/api/fatture/vendita`
- `GET http://localhost:8081/api/fatture/registrate`
- `GET http://localhost:8081/api/fatture/andamento?dal=&al=`
- `POST http://localhost:8081/api/fatture/{id}/registrazione`
- `DELETE http://localhost:8081/api/fatture/{id}/registrazione`
- `GET http://localhost:8081/api/fatture/report`
- `GET http://localhost:8081/api/fatture/{id}/pdf`
- `GET http://localhost:8081/api/fatture/{id}`
- `POST http://localhost:8081/api/fatture`
- `PUT http://localhost:8081/api/fatture/{id}`
- `DELETE http://localhost:8081/api/fatture/{id}`
- `POST http://localhost:8081/api/fatture/{id}/emetti`
- `POST http://localhost:8081/api/fatture/{id}/paga`
- `POST http://localhost:8081/api/fatture/{id}/annulla`

### Provvigioni
- `GET http://localhost:8081/api/provvigioni`
- `GET http://localhost:8081/api/provvigioni/{id}`
- `POST http://localhost:8081/api/provvigioni`
- `PUT http://localhost:8081/api/provvigioni/{id}`
- `DELETE http://localhost:8081/api/provvigioni/{id}`

### Ruoli
- `GET http://localhost:8081/api/ruoli`

### Team
- `GET http://localhost:8081/api/team`
- `GET http://localhost:8081/api/team/{id}`
- `POST http://localhost:8081/api/team`
- `PUT http://localhost:8081/api/team/{id}`
- `DELETE http://localhost:8081/api/team/{id}`

### Utenti
- `GET http://localhost:8081/api/utenti`
- `GET http://localhost:8081/api/utenti/{id}`
- `POST http://localhost:8081/api/utenti`
- `PUT http://localhost:8081/api/utenti/{id}`
- `DELETE http://localhost:8081/api/utenti/{id}`

### Notifiche
- `GET http://localhost:8081/api/notifications?recipientId=`
- `POST http://localhost:8081/api/notifications`
- `POST http://localhost:8081/api/notifications/{id}/read`

### Email
- `POST http://localhost:8081/api/email`

### Chat
- Canale WebSocket non esposto qui; usare gli endpoint REST sopra per messaggi e verificare eventuali listener client.
