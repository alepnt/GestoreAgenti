package com.example.GestoreAgenti.service.report; // Definisce il pacchetto com.example.GestoreAgenti.service.report che contiene questa classe.

import java.io.ByteArrayOutputStream; // Importa java.io.ByteArrayOutputStream per abilitare le funzionalità utilizzate nel file.
import java.io.IOException; // Importa java.io.IOException per abilitare le funzionalità utilizzate nel file.
import java.time.format.DateTimeFormatter; // Importa java.time.format.DateTimeFormatter per abilitare le funzionalità utilizzate nel file.
import java.util.ArrayList; // Importa java.util.ArrayList per abilitare le funzionalità utilizzate nel file.
import java.util.EnumMap; // Importa java.util.EnumMap per abilitare le funzionalità utilizzate nel file.
import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Map; // Importa java.util.Map per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per abilitare le funzionalità utilizzate nel file.

import org.apache.poi.ss.usermodel.Cell; // Importa org.apache.poi.ss.usermodel.Cell per abilitare le funzionalità utilizzate nel file.
import org.apache.poi.ss.usermodel.CellStyle; // Importa org.apache.poi.ss.usermodel.CellStyle per abilitare le funzionalità utilizzate nel file.
import org.apache.poi.ss.usermodel.Font; // Importa org.apache.poi.ss.usermodel.Font per abilitare le funzionalità utilizzate nel file.
import org.apache.poi.ss.usermodel.Row; // Importa org.apache.poi.ss.usermodel.Row per abilitare le funzionalità utilizzate nel file.
import org.apache.poi.ss.usermodel.Sheet; // Importa org.apache.poi.ss.usermodel.Sheet per abilitare le funzionalità utilizzate nel file.
import org.apache.poi.ss.usermodel.Workbook; // Importa org.apache.poi.ss.usermodel.Workbook per abilitare le funzionalità utilizzate nel file.
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Importa org.apache.poi.xssf.usermodel.XSSFWorkbook per abilitare le funzionalità utilizzate nel file.
import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.invoice.InvoiceState; // Importa com.example.GestoreAgenti.invoice.InvoiceState per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Cliente; // Importa com.example.GestoreAgenti.model.Cliente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Contratto; // Importa com.example.GestoreAgenti.model.Contratto per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Fattura; // Importa com.example.GestoreAgenti.model.Fattura per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Servizio; // Importa com.example.GestoreAgenti.model.Servizio per abilitare le funzionalità utilizzate nel file.

/**
 * Servizio applicativo responsabile della generazione dei report Excel delle fatture.
 */
@Service // Applica l'annotazione @Service per configurare il componente.
public class FatturaExcelReportService { // Definisce la classe FatturaExcelReportService che incapsula la logica applicativa.

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Definisce il metodo DateTimeFormatter.ofPattern che supporta la logica di dominio.

    public byte[] generaReport(List<Fattura> fatture) { // Definisce il metodo generaReport che supporta la logica di dominio.
        Objects.requireNonNull(fatture, "fatture"); // Esegue l'istruzione terminata dal punto e virgola.

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) { // Avvia il blocco protetto per intercettare eventuali eccezioni.
            CellStyle headerStyle = createHeaderStyle(workbook); // Assegna il valore calcolato alla variabile CellStyle headerStyle.
            Map<InvoiceState, SheetContext> contexts = new EnumMap<>(InvoiceState.class); // Assegna il valore calcolato alla variabile Map<InvoiceState, SheetContext> contexts.
            List<SheetContext> orderedContexts = new ArrayList<>(); // Assegna il valore calcolato alla variabile List<SheetContext> orderedContexts.

            for (Fattura fattura : fatture) { // Itera sugli elementi richiesti dalla logica.
                if (fattura == null) { // Valuta la condizione per controllare il flusso applicativo.
                    continue; // Passa direttamente all'iterazione successiva del ciclo.
                } // Chiude il blocco di codice precedente.
                InvoiceState stato = resolveState(fattura); // Assegna il valore calcolato alla variabile InvoiceState stato.
                SheetContext context = contexts.computeIfAbsent(stato, t -> { // Apre il blocco di codice associato alla dichiarazione.
                    SheetContext created = createSheet(workbook, t, headerStyle); // Assegna il valore calcolato alla variabile SheetContext created.
                    orderedContexts.add(created); // Esegue l'istruzione terminata dal punto e virgola.
                    return created; // Restituisce il risultato dell'espressione created.
                }); // Chiude il blocco di codice precedente.

                Row row = context.sheet.createRow(context.nextRow++); // Assegna il valore calcolato alla variabile Row row.
                List<String> values = renderValues(fattura, stato); // Assegna il valore calcolato alla variabile List<String> values.
                for (int i = 0; i < values.size(); i++) { // Itera sugli elementi richiesti dalla logica.
                    Cell cell = row.createCell(i); // Assegna il valore calcolato alla variabile Cell cell.
                    cell.setCellValue(values.get(i)); // Esegue l'istruzione terminata dal punto e virgola.
                } // Chiude il blocco di codice precedente.
            } // Chiude il blocco di codice precedente.

            for (SheetContext context : orderedContexts) { // Itera sugli elementi richiesti dalla logica.
                for (int columnIndex = 0; columnIndex < context.columnCount; columnIndex++) { // Itera sugli elementi richiesti dalla logica.
                    context.sheet.autoSizeColumn(columnIndex); // Esegue l'istruzione terminata dal punto e virgola.
                } // Chiude il blocco di codice precedente.
            } // Chiude il blocco di codice precedente.

            workbook.write(out); // Esegue l'istruzione terminata dal punto e virgola.
            return out.toByteArray(); // Restituisce il risultato dell'espressione out.toByteArray().
        } catch (IOException e) { // Apre il blocco di codice associato alla dichiarazione.
            throw new IllegalStateException("Impossibile generare il report Excel delle fatture", e); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    private SheetContext createSheet(Workbook workbook, InvoiceState stato, CellStyle headerStyle) { // Definisce il metodo createSheet che supporta la logica di dominio.
        Sheet sheet = workbook.createSheet(stato.getLabel()); // Assegna il valore calcolato alla variabile Sheet sheet.
        String[] headers = headerFor(stato); // Assegna il valore calcolato alla variabile String[] headers.
        Row headerRow = sheet.createRow(0); // Assegna il valore calcolato alla variabile Row headerRow.
        for (int i = 0; i < headers.length; i++) { // Itera sugli elementi richiesti dalla logica.
            Cell cell = headerRow.createCell(i); // Assegna il valore calcolato alla variabile Cell cell.
            cell.setCellValue(headers[i]); // Esegue l'istruzione terminata dal punto e virgola.
            cell.setCellStyle(headerStyle); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        return new SheetContext(sheet, 1, headers.length); // Restituisce il risultato dell'espressione new SheetContext(sheet, 1, headers.length).
    } // Chiude il blocco di codice precedente.

    private CellStyle createHeaderStyle(Workbook workbook) { // Definisce il metodo createHeaderStyle che supporta la logica di dominio.
        CellStyle style = workbook.createCellStyle(); // Assegna il valore calcolato alla variabile CellStyle style.
        Font font = workbook.createFont(); // Assegna il valore calcolato alla variabile Font font.
        font.setBold(true); // Esegue l'istruzione terminata dal punto e virgola.
        style.setFont(font); // Esegue l'istruzione terminata dal punto e virgola.
        return style; // Restituisce il risultato dell'espressione style.
    } // Chiude il blocco di codice precedente.

    private String[] headerFor(InvoiceState stato) { // Definisce il metodo headerFor che supporta la logica di dominio.
        return switch (stato) { // Restituisce il risultato dell'espressione switch (stato) {.
            case EMESSA -> new String[] {"Numero", "Data emissione", "Cliente", "Contratto", "Imponibile", "IVA", "Totale", // Gestisce uno dei possibili casi nello switch.
                    "Stato"}; // Esegue l'istruzione terminata dal punto e virgola.
            case IN_SOLLECITO -> new String[] {"Numero", "Cliente", "Contratto", "Data emissione", "Totale fattura", // Gestisce uno dei possibili casi nello switch.
                    "Stato"}; // Esegue l'istruzione terminata dal punto e virgola.
            case SALDATA -> new String[] {"Numero", "Data emissione", "Cliente", "Totale", "Commissione servizio", // Gestisce uno dei possibili casi nello switch.
                    "Servizio", "Stato"}; // Esegue l'istruzione terminata dal punto e virgola.
        }; // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    private List<String> renderValues(Fattura fattura, InvoiceState stato) { // Definisce il metodo renderValues che supporta la logica di dominio.
        return switch (stato) { // Restituisce il risultato dell'espressione switch (stato) {.
            case EMESSA -> List.of( // Gestisce uno dei possibili casi nello switch.
                    valueOrEmpty(fattura.getNumeroFattura()), // Esegue l'istruzione necessaria alla logica applicativa.
                    formatDate(fattura.getDataEmissione()), // Esegue l'istruzione necessaria alla logica applicativa.
                    formatCliente(fattura.getCliente()), // Esegue l'istruzione necessaria alla logica applicativa.
                    formatContratto(fattura.getContratto()), // Esegue l'istruzione necessaria alla logica applicativa.
                    formatNumber(fattura.getImponibile()), // Esegue l'istruzione necessaria alla logica applicativa.
                    formatNumber(fattura.getIva()), // Esegue l'istruzione necessaria alla logica applicativa.
                    formatNumber(fattura.getTotale()), // Esegue l'istruzione necessaria alla logica applicativa.
                    valueOrEmpty(fattura.getStato())); // Esegue l'istruzione terminata dal punto e virgola.
            case IN_SOLLECITO -> { // Gestisce uno dei possibili casi nello switch.
                Contratto contratto = fattura.getContratto(); // Assegna il valore calcolato alla variabile Contratto contratto.
                yield List.of( // Esegue l'istruzione necessaria alla logica applicativa.
                        valueOrEmpty(fattura.getNumeroFattura()), // Esegue l'istruzione necessaria alla logica applicativa.
                        formatCliente(fattura.getCliente()), // Esegue l'istruzione necessaria alla logica applicativa.
                        formatContratto(contratto), // Esegue l'istruzione necessaria alla logica applicativa.
                        formatDate(fattura.getDataEmissione()), // Esegue l'istruzione necessaria alla logica applicativa.
                        formatNumber(fattura.getTotale()), // Esegue l'istruzione necessaria alla logica applicativa.
                        valueOrEmpty(fattura.getStato())); // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.
            case SALDATA -> { // Gestisce uno dei possibili casi nello switch.
                Contratto contratto = fattura.getContratto(); // Assegna il valore calcolato alla variabile Contratto contratto.
                Servizio servizio = contratto != null ? contratto.getServizio() : null; // Assegna il valore calcolato alla variabile Servizio servizio.
                yield List.of( // Esegue l'istruzione necessaria alla logica applicativa.
                        valueOrEmpty(fattura.getNumeroFattura()), // Esegue l'istruzione necessaria alla logica applicativa.
                        formatDate(fattura.getDataEmissione()), // Esegue l'istruzione necessaria alla logica applicativa.
                        formatCliente(fattura.getCliente()), // Esegue l'istruzione necessaria alla logica applicativa.
                        formatNumber(fattura.getTotale()), // Esegue l'istruzione necessaria alla logica applicativa.
                        servizio != null ? formatNumber(servizio.getCommissionePercentuale()) : "", // Esegue l'istruzione necessaria alla logica applicativa.
                        servizio != null ? valueOrEmpty(servizio.getNome()) : "", // Esegue l'istruzione necessaria alla logica applicativa.
                        valueOrEmpty(fattura.getStato())); // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.
        }; // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    private InvoiceState resolveState(Fattura fattura) { // Definisce il metodo resolveState che supporta la logica di dominio.
        return fattura != null ? InvoiceState.fromPersistence(fattura.getStato()) : InvoiceState.EMESSA; // Restituisce il risultato dell'espressione fattura != null ? InvoiceState.fromPersistence(fattura.getStato()) : InvoiceState.EMESSA.
    } // Chiude il blocco di codice precedente.

    private String formatCliente(Cliente cliente) { // Definisce il metodo formatCliente che supporta la logica di dominio.
        if (cliente == null) { // Valuta la condizione per controllare il flusso applicativo.
            return ""; // Restituisce il risultato dell'espressione "".
        } // Chiude il blocco di codice precedente.
        String denominazione = cliente.getRagioneSociale(); // Assegna il valore calcolato alla variabile String denominazione.
        if (denominazione != null && !denominazione.isBlank()) { // Valuta la condizione per controllare il flusso applicativo.
            return denominazione; // Restituisce il risultato dell'espressione denominazione.
        } // Chiude il blocco di codice precedente.
        StringBuilder builder = new StringBuilder(); // Assegna il valore calcolato alla variabile StringBuilder builder.
        if (cliente.getNome() != null) { // Valuta la condizione per controllare il flusso applicativo.
            builder.append(cliente.getNome()); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        if (cliente.getCognome() != null) { // Valuta la condizione per controllare il flusso applicativo.
            if (builder.length() > 0) { // Valuta la condizione per controllare il flusso applicativo.
                builder.append(' '); // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.
            builder.append(cliente.getCognome()); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        return builder.toString(); // Restituisce il risultato dell'espressione builder.toString().
    } // Chiude il blocco di codice precedente.

    private String formatContratto(Contratto contratto) { // Definisce il metodo formatContratto che supporta la logica di dominio.
        if (contratto == null) { // Valuta la condizione per controllare il flusso applicativo.
            return ""; // Restituisce il risultato dell'espressione "".
        } // Chiude il blocco di codice precedente.
        if (contratto.getIdContratto() != null) { // Valuta la condizione per controllare il flusso applicativo.
            return "CTR-" + contratto.getIdContratto(); // Restituisce il risultato dell'espressione "CTR-" + contratto.getIdContratto().
        } // Chiude il blocco di codice precedente.
        return "Contratto"; // Restituisce il risultato dell'espressione "Contratto".
    } // Chiude il blocco di codice precedente.

    private String formatDate(java.time.LocalDate date) { // Definisce il metodo formatDate che supporta la logica di dominio.
        return date != null ? DATE_FORMAT.format(date) : ""; // Restituisce il risultato dell'espressione date != null ? DATE_FORMAT.format(date) : "".
    } // Chiude il blocco di codice precedente.

    private String formatNumber(java.math.BigDecimal value) { // Definisce il metodo formatNumber che supporta la logica di dominio.
        return value != null ? value.toPlainString() : ""; // Restituisce il risultato dell'espressione value != null ? value.toPlainString() : "".
    } // Chiude il blocco di codice precedente.

    private String valueOrEmpty(String value) { // Definisce il metodo valueOrEmpty che supporta la logica di dominio.
        return value != null ? value : ""; // Restituisce il risultato dell'espressione value != null ? value : "".
    } // Chiude il blocco di codice precedente.

    private static final class SheetContext { // Apre il blocco di codice associato alla dichiarazione.

        private final Sheet sheet; // Dichiara il campo sheet dell'oggetto.
        private final int columnCount; // Dichiara il campo columnCount dell'oggetto.
        private int nextRow; // Dichiara il campo nextRow dell'oggetto.

        private SheetContext(Sheet sheet, int nextRow, int columnCount) { // Definisce il metodo SheetContext che supporta la logica di dominio.
            this.sheet = sheet; // Aggiorna il campo sheet dell'istanza.
            this.nextRow = nextRow; // Aggiorna il campo nextRow dell'istanza.
            this.columnCount = columnCount; // Aggiorna il campo columnCount dell'istanza.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
