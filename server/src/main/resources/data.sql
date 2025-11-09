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

INSERT INTO dipendente (nome, cognome, sesso, data_nascita, indirizzo, citta, cap, provincia, paese, email, telefono, username, password, ranking, team, contratto_no, tot_provvigione_mensile, tot_provvigione_annuale, commissione_percentuale, ordine_ranking) VALUES
    ('Luca', 'Rossi', 'M', '1982-02-14', 'Via Manzoni 5', 'Milano', '20121', 'MI', 'Italia', 'luca.rossi@gestoreagenti.it', '+39-02-9988776', 'lrossi', '{noop}password1', 'Senior', 'Milano Team A', 'CNT-001', 1850.75, 22209.00, 0.0300, 1),
    ('Marta', 'Bianchi', 'F', '1985-11-02', 'Via Brera 10', 'Milano', '20121', 'MI', 'Italia', 'marta.bianchi@gestoreagenti.it', '+39-02-7788990', 'mbianchi', '{noop}password2', 'Senior', 'Milano Team A', 'CNT-002', 1720.40, 20644.80, 0.0250, 2),
    ('Giulio', 'Ferrari', 'M', '1980-09-18', 'Via Torino 7', 'Milano', '20122', 'MI', 'Italia', 'giulio.ferrari@gestoreagenti.it', '+39-02-6655443', 'gferrari', '{noop}password3', 'Senior', 'Milano Team A', 'CNT-003', 1685.00, 20220.00, 0.0200, 3),
    ('Sara', 'Neri', 'F', '1992-07-03', 'Via Solferino 22', 'Milano', '20124', 'MI', 'Italia', 'sara.neri@gestoreagenti.it', '+39-02-5544332', 'sneri', '{noop}password4', 'Junior', 'Milano Team A', 'CNT-004', 1320.40, 15844.80, 0.0150, 4),
    ('Davide', 'Leone', 'M', '1987-09-19', 'Via Indipendenza 22', 'Milano', '20125', 'MI', 'Italia', 'davide.leone@gestoreagenti.it', '+39-02-8877665', 'dleone', '{noop}password5', 'Junior', 'Milano Team A', 'CNT-005', 940.00, 11280.00, 0.0075, 5),
    ('Chiara', 'Villa', 'F', '1995-04-12', 'Via Larga 18', 'Milano', '20123', 'MI', 'Italia', 'chiara.villa@gestoreagenti.it', '+39-02-2233445', 'cvilla', '{noop}password6', 'Stagista', 'Milano Team A', 'CNT-006', 520.00, 6240.00, 0.0025, 6),
    ('Enrico', 'Sala', 'M', '1983-01-08', 'Via San Marco 15', 'Milano', '20121', 'MI', 'Italia', 'enrico.sala@gestoreagenti.it', '+39-02-7788456', 'esala', '{noop}password7', 'Senior', 'Milano Team B', 'CNT-007', 1980.50, 23766.00, 0.0400, 1),
    ('Laura', 'Conti', 'F', '1984-05-22', 'Via Fiori Chiari 3', 'Milano', '20121', 'MI', 'Italia', 'laura.conti@gestoreagenti.it', '+39-02-6688997', 'lconti', '{noop}password8', 'Senior', 'Milano Team B', 'CNT-008', 1820.10, 21841.20, 0.0300, 2),
    ('Paolo', 'Moretti', 'M', '1981-11-30', 'Via Pontaccio 9', 'Milano', '20121', 'MI', 'Italia', 'paolo.moretti@gestoreagenti.it', '+39-02-8899776', 'pmoretti', '{noop}password9', 'Senior', 'Milano Team B', 'CNT-009', 1765.30, 21183.60, 0.0250, 3),
    ('Giulia', 'Verdi', 'F', '1990-03-15', 'Via Turati 4', 'Milano', '20124', 'MI', 'Italia', 'giulia.verdi@gestoreagenti.it', '+39-02-3322110', 'gverdi', '{noop}password10', 'Junior', 'Milano Team B', 'CNT-010', 1380.60, 16567.20, 0.0150, 4),
    ('Alberto', 'Greco', 'M', '1988-08-19', 'Corso Garibaldi 45', 'Milano', '20121', 'MI', 'Italia', 'alberto.greco@gestoreagenti.it', '+39-02-6677885', 'agreco', '{noop}password11', 'Junior', 'Milano Team B', 'CNT-011', 990.00, 11880.00, 0.0100, 5),
    ('Irene', 'Fontana', 'F', '1996-06-28', 'Via Panfilo Castaldi 8', 'Milano', '20124', 'MI', 'Italia', 'irene.fontana@gestoreagenti.it', '+39-02-7788123', 'ifontana', '{noop}password12', 'Stagista', 'Milano Team B', 'CNT-012', 540.00, 6480.00, 0.0050, 6),
    ('Anna', 'Moretti', 'F', '1980-01-05', 'Via Vittorio Veneto 12', 'Roma', '00187', 'RM', 'Italia', 'anna.moretti@gestoreagenti.it', '+39-06-1122334', 'admin', '{noop}password13', 'Executive', 'Direzione', 'CNT-013', 0.00, 0.00, 0.0000, NULL),
    ('Paolo', 'Galli', 'M', '1992-08-27', 'Via Dante 9', 'Firenze', '50100', 'FI', 'Italia', 'paolo.galli@gestoreagenti.it', '+39-055-6677889', 'support', '{noop}password14', 'Operations', 'Supporto', 'CNT-014', 0.00, 0.00, 0.0000, NULL);

INSERT INTO Team (provincia, responsabile_id, tot_profitto_mensile, tot_provvigione_mensile, tot_provvigione_annuo, percentuale_provvigione, distribuzione_provvigioni, base_calcolo) VALUES
    ('Milano Team A', 1, 48500.00, 6200.00, 74400.00, 0.10, 'PERCENTUALE', 'IMPONIBILE'),
    ('Milano Team B', 7, 51200.00, 6800.00, 81600.00, 0.12, 'SBARRAMENTO', 'TOTALE');

-- Tabelle dipendenti da chiavi esterne.
INSERT INTO contratto (id_cliente, id_dipendente, id_servizio, data_inizio, data_fine, importo, stato, note, provvigione_alla_firma) VALUES
    (1, 1, 1, '2024-01-15', '2024-12-31', 9600.00, 'ATTIVO', 'Rinnovo annuale del pacchetto assicurativo corporate.', 1),
    (2, 7, 2, '2024-03-01', '2024-09-30', 5100.00, 'ATTIVO', 'Percorso formativo dedicato al reparto retail.', 0),
    (3, 8, 3, '2024-05-10', NULL, 7800.00, 'IN_TRATTATIVA', 'Implementazione graduale del piano welfare privati.', 0);

INSERT INTO fattura (numero_fattura, data_emissione, id_cliente, id_contratto, imponibile, iva, totale, stato) VALUES
    ('FA-2024-001', '2024-02-01', 1, 1, 8000.00, 1760.00, 9760.00, 'EMESSA'),
    ('FA-2024-002', '2024-04-05', 2, 2, 5100.00, 1122.00, 6222.00, 'PAGATA'),
    ('FA-2024-003', '2024-06-12', 3, 3, 3900.00, 858.00, 4758.00, 'BOZZA');

INSERT INTO pagamento (id_fattura, data_pagamento, importo, metodo, stato) VALUES
    (1, NULL, 0.00, 'Bonifico', 'IN_ATTESA'),
    (2, '2024-04-20', 6222.00, 'Carta di Credito', 'COMPLETATO'),
    (3, NULL, 0.00, 'RID Bancario', 'IN_ELABORAZIONE');

INSERT INTO provvigione (id_dipendente, id_contratto, id_fattura, percentuale, importo, stato, data_calcolo) VALUES
    (1, 1, 1, 0.0300, 240.00, 'EROGATA', '2024-02-02'),
    (2, 1, 1, 0.0250, 200.00, 'EROGATA', '2024-02-02'),
    (3, 1, 1, 0.0200, 160.00, 'EROGATA', '2024-02-02'),
    (4, 1, 1, 0.0150, 120.00, 'EROGATA', '2024-02-02'),
    (5, 1, 1, 0.0075, 60.00, 'EROGATA', '2024-02-02'),
    (6, 1, 1, 0.0025, 20.00, 'EROGATA', '2024-02-02'),
    (7, 2, 2, 0.0400, 248.88, 'IN_ATTESA', '2024-04-06'),
    (8, 2, 2, 0.0300, 186.66, 'IN_ATTESA', '2024-04-06'),
    (9, 2, 2, 0.0250, 155.55, 'IN_ATTESA', '2024-04-06'),
    (10, 2, 2, 0.0150, 93.33, 'IN_ATTESA', '2024-04-06'),
    (11, 2, 2, 0.0100, 62.22, 'IN_ATTESA', '2024-04-06'),
    (12, 2, 2, 0.0000, 0.00, 'IN_ATTESA', '2024-04-06');

INSERT INTO notification (recipient_id, type, title, message, created_at, is_read) VALUES
    (1, 'TEAM_MEMBER_ADDED', 'Nuovo membro nel team Milano Team A', 'È stato aggiunto un nuovo agente junior nel team Milano Team A.', '2024-01-20T09:15:00', 0),
    (7, 'INVOICE_CREATED', 'Nuova fattura emessa', 'È stata generata la fattura FA-2024-002 per Fashion Lab S.p.A.', '2024-04-05T14:05:00', 1),
    (8, 'PAYMENT_EVENT', 'Pagamento in elaborazione', 'Il pagamento RID per il cliente Giorgio Russo è in corso di elaborazione.', '2024-06-12T10:45:00', 0);

INSERT INTO utente (username, password_hash, ruolo, id_dipendente, id_cliente) VALUES
    ('admin', '$2a$10$7Fh1uO17s8b9L0G0v1apUeqWJkLkS9YyH1kUuXjz7wA7kVdRkBp5G', 'ADMIN', 13, NULL),
    ('lrossi', '$2a$10$3YB2FLS7hfrnVQx9Xj9UVeE6zK1d4bnQ0rXjW8w1D03Gf6X4B8yOm', 'AGENTE', 1, NULL),
    ('esala', '$2a$10$IRL3n5wA0O9KQz1rY2ZJ1uP7x2/9C8dA9nT6GQ0mP5sV7yR4jX8y2', 'TEAM_LEAD', 7, NULL),
    ('support', '$2a$10$7Fh1uO17s8b9L0G0v1apUeqWJkLkS9YyH1kUuXjz7wA7kVdRkBp5G', 'OPERATIONS', 14, NULL),
    ('mbianchi', '$2a$10$7Fh1uO17s8b9L0G0v1apUeqWJkLkS9YyH1kUuXjz7wA7kVdRkBp5G', 'CLIENTE', NULL, 1);
