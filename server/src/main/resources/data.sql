-- Ripulisce le tabelle per permettere il reinserimento dei dati di prova.
DELETE FROM notification;
DELETE FROM pagamento;
DELETE FROM provvigione;
DELETE FROM fattura;
DELETE FROM contratto;
DELETE FROM utente;
DELETE FROM dipendente;
DELETE FROM cliente;
DELETE FROM servizio;
DELETE FROM Team;

-- Tabelle di anagrafica di base.
INSERT INTO servizio (nome, descrizione, prezzo_base, commissione_percentuale) VALUES
    ('Consulenza Assicurativa', 'Analisi personalizzata dei bisogni assicurativi e predisposizione delle polizze ottimali.', 1200.00, 0.12),
    ('Formazione Commerciale', 'Percorso formativo trimestrale dedicato alla forza vendita con focus su tecniche di negoziazione.', 850.00, 0.08),
    ('Piano Welfare Aziendale', 'Implementazione completa di un piano welfare per aziende PMI con monitoraggio annuale.', 2100.00, 0.15);

INSERT INTO cliente (nome, cognome, sesso, data_nascita, indirizzo, citta, cap, provincia, paese, email, telefono, [type], ragione_sociale, partita_iva) VALUES
    ('Marco', 'Bianchi', 'M', '1978-05-09', 'Via Roma 12', 'Milano', '20100', 'MI', 'Italia', 'marco.bianchi@acme.it', '+39-02-1234567', 'Azienda', 'ACME Consulting S.r.l.', 'IT12345678901'),
    ('Elisa', 'Verdi', 'F', '1985-11-22', 'Corso Italia 45', 'Torino', '10100', 'TO', 'Italia', 'elisa.verdi@fashionlab.it', '+39-011-7654321', 'Azienda', 'Fashion Lab S.p.A.', 'IT10987654321'),
    ('Giorgio', 'Russo', 'M', '1990-03-30', 'Via dei Tigli 8', 'Bologna', '40100', 'BO', 'Italia', 'giorgio.russo@email.com', '+39-051-246810', 'Privato', NULL, NULL);

INSERT INTO dipendente (nome, cognome, sesso, data_nascita, indirizzo, citta, cap, provincia, paese, email, telefono, username, password, ranking, team, contratto_no, tot_provvigione_mensile, tot_provvigione_annuale) VALUES
    ('Luca', 'Rossi', 'M', '1982-02-14', 'Via Manzoni 5', 'Milano', '20121', 'MI', 'Italia', 'luca.rossi@gestoreagenti.it', '+39-02-9988776', 'lrossi', '{noop}password1', 'Senior', 'Lombardia', 'CNT-001', 1850.75, 22209.00),
    ('Sara', 'Conti', 'F', '1991-07-03', 'Via Garibaldi 88', 'Torino', '10128', 'TO', 'Italia', 'sara.conti@gestoreagenti.it', '+39-011-5544332', 'sconti', '{noop}password2', 'Mid', 'Piemonte', 'CNT-002', 1320.40, 15844.80),
    ('Davide', 'Leone', 'M', '1987-09-19', 'Via Indipendenza 22', 'Bologna', '40121', 'BO', 'Italia', 'davide.leone@gestoreagenti.it', '+39-051-8877665', 'dleone', '{noop}password3', 'Junior', 'Emilia', 'CNT-003', 940.00, 11280.00);

INSERT INTO Team (provincia, responsabile_id, tot_profitto_mensile, tot_provvigione_mensile, tot_provvigione_annuo) VALUES
    ('Lombardia', 1, 48500.00, 6200.00, 74400.00),
    ('Piemonte', 2, 32750.00, 4100.00, 49200.00),
    ('Emilia-Romagna', 3, 28900.00, 3550.00, 42600.00);

-- Tabelle dipendenti da chiavi esterne.
INSERT INTO contratto (id_cliente, id_dipendente, id_servizio, data_inizio, data_fine, importo, stato, note) VALUES
    (1, 1, 1, '2024-01-15', '2024-12-31', 9600.00, 'ATTIVO', 'Rinnovo annuale del pacchetto assicurativo corporate.'),
    (2, 2, 2, '2024-03-01', '2024-09-30', 5100.00, 'ATTIVO', 'Percorso formativo dedicato al reparto retail.'),
    (3, 3, 3, '2024-05-10', NULL, 7800.00, 'IN_TRATTATIVA', 'Implementazione graduale del piano welfare privati.');

INSERT INTO fattura (numero_fattura, data_emissione, id_cliente, id_contratto, imponibile, iva, totale, stato) VALUES
    ('FA-2024-001', '2024-02-01', 1, 1, 8000.00, 1760.00, 9760.00, 'EMESSA'),
    ('FA-2024-002', '2024-04-05', 2, 2, 5100.00, 1122.00, 6222.00, 'PAGATA'),
    ('FA-2024-003', '2024-06-12', 3, 3, 3900.00, 858.00, 4758.00, 'BOZZA');

INSERT INTO pagamento (id_fattura, data_pagamento, importo, metodo, stato) VALUES
    (1, NULL, 0.00, 'Bonifico', 'IN_ATTESA'),
    (2, '2024-04-20', 6222.00, 'Carta di Credito', 'COMPLETATO'),
    (3, NULL, 0.00, 'RID Bancario', 'IN_ELABORAZIONE');

INSERT INTO provvigione (id_dipendente, id_contratto, percentuale, importo, stato, data_calcolo) VALUES
    (1, 1, 0.12, 1152.00, 'CALCOLATA', '2024-02-02'),
    (2, 2, 0.10, 510.00, 'EROGATA', '2024-04-21'),
    (3, 3, 0.15, 585.00, 'IN_ATTESA', '2024-06-15');

INSERT INTO notification (recipient_id, type, title, message, created_at, is_read) VALUES
    (1, 'TEAM_MEMBER_ADDED', 'Nuovo membro nel team Lombardia', 'È stato aggiunto un nuovo agente junior nel team Lombardia.', '2024-01-20T09:15:00', 0),
    (2, 'INVOICE_CREATED', 'Nuova fattura emessa', 'È stata generata la fattura FA-2024-002 per Fashion Lab S.p.A.', '2024-04-05T14:05:00', 1),
    (3, 'PAYMENT_EVENT', 'Pagamento in elaborazione', 'Il pagamento RID per il cliente Giorgio Russo è in corso di elaborazione.', '2024-06-12T10:45:00', 0);

INSERT INTO utente (username, password_hash, ruolo, id_dipendente) VALUES
    ('admin', '$2a$10$7Fh1uO17s8b9L0G0v1apUeqWJkLkS9YyH1kUuXjz7wA7kVdRkBp5G', 'ADMIN', NULL),
    ('lrossi', '$2a$10$3YB2FLS7hfrnVQx9Xj9UVeE6zK1d4bnQ0rXjW8w1D03Gf6X4B8yOm', 'AGENTE', 1),
    ('sconti', '$2a$10$IRL3n5wA0O9KQz1rY2ZJ1uP7x2/9C8dA9nT6GQ0mP5sV7yR4jX8y2', 'AGENTE', 2);
