package com.example.GestoreAgenti.service.report; // Definisce il pacchetto com.example.GestoreAgenti.service.report che contiene questa classe.

import java.awt.Color; // Importa java.awt.Color per abilitare le funzionalità utilizzate nel file.
import java.io.ByteArrayOutputStream; // Importa java.io.ByteArrayOutputStream per abilitare le funzionalità utilizzate nel file.
import java.io.IOException; // Importa java.io.IOException per abilitare le funzionalità utilizzate nel file.
import java.text.NumberFormat; // Importa java.text.NumberFormat per abilitare le funzionalità utilizzate nel file.
import java.time.format.DateTimeFormatter; // Importa java.time.format.DateTimeFormatter per abilitare le funzionalità utilizzate nel file.
import java.util.Locale; // Importa java.util.Locale per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per abilitare le funzionalità utilizzate nel file.
import java.util.function.Consumer; // Importa java.util.function.Consumer per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Cliente; // Importa com.example.GestoreAgenti.model.Cliente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Contratto; // Importa com.example.GestoreAgenti.model.Contratto per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Fattura; // Importa com.example.GestoreAgenti.model.Fattura per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Servizio; // Importa com.example.GestoreAgenti.model.Servizio per abilitare le funzionalità utilizzate nel file.
import com.lowagie.text.Document; // Importa com.lowagie.text.Document per abilitare le funzionalità utilizzate nel file.
import com.lowagie.text.DocumentException; // Importa com.lowagie.text.DocumentException per abilitare le funzionalità utilizzate nel file.
import com.lowagie.text.Element; // Importa com.lowagie.text.Element per abilitare le funzionalità utilizzate nel file.
import com.lowagie.text.Font; // Importa com.lowagie.text.Font per abilitare le funzionalità utilizzate nel file.
import com.lowagie.text.FontFactory; // Importa com.lowagie.text.FontFactory per abilitare le funzionalità utilizzate nel file.
import com.lowagie.text.PageSize; // Importa com.lowagie.text.PageSize per abilitare le funzionalità utilizzate nel file.
import com.lowagie.text.Paragraph; // Importa com.lowagie.text.Paragraph per abilitare le funzionalità utilizzate nel file.
import com.lowagie.text.Phrase; // Importa com.lowagie.text.Phrase per abilitare le funzionalità utilizzate nel file.
import com.lowagie.text.pdf.PdfPCell; // Importa com.lowagie.text.pdf.PdfPCell per abilitare le funzionalità utilizzate nel file.
import com.lowagie.text.pdf.PdfPTable; // Importa com.lowagie.text.pdf.PdfPTable per abilitare le funzionalità utilizzate nel file.
import com.lowagie.text.pdf.PdfWriter; // Importa com.lowagie.text.pdf.PdfWriter per abilitare le funzionalità utilizzate nel file.

/**
 * Genera un PDF coerente per la rappresentazione di una fattura. Il layout è
 * stato progettato per essere riutilizzabile per tutte le fatture in modo da
 * garantire uniformità visiva.
 */
@Service // Applica l'annotazione @Service per configurare il componente.
public class FatturaPdfReportService { // Definisce la classe FatturaPdfReportService che incapsula la logica applicativa.

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Definisce il metodo DateTimeFormatter.ofPattern che supporta la logica di dominio.
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.ITALY); // Definisce il metodo NumberFormat.getCurrencyInstance che supporta la logica di dominio.

    private static final Color PRIMARY_COLOR = new Color(32, 60, 117); // Definisce il metodo Color che supporta la logica di dominio.
    private static final Color LIGHT_BACKGROUND = new Color(244, 247, 252); // Definisce il metodo Color che supporta la logica di dominio.

    private final Font brandFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.WHITE); // Definisce il metodo FontFactory.getFont che supporta la logica di dominio.
    private final Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE); // Definisce il metodo FontFactory.getFont che supporta la logica di dominio.
    private final Font headerSmallFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.WHITE); // Definisce il metodo FontFactory.getFont che supporta la logica di dominio.
    private final Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, PRIMARY_COLOR); // Definisce il metodo FontFactory.getFont che supporta la logica di dominio.
    private final Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new Color(68, 68, 68)); // Definisce il metodo FontFactory.getFont che supporta la logica di dominio.
    private final Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK); // Definisce il metodo FontFactory.getFont che supporta la logica di dominio.
    private final Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, new Color(120, 120, 120)); // Definisce il metodo FontFactory.getFont che supporta la logica di dominio.

    public byte[] generaPdf(Fattura fattura) { // Definisce il metodo generaPdf che supporta la logica di dominio.
        Objects.requireNonNull(fattura, "fattura"); // Esegue l'istruzione terminata dal punto e virgola.

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); // Avvia il blocco protetto per intercettare eventuali eccezioni.
                DocumentContainer documentContainer = DocumentContainer.open(out)) { // Apre il blocco di codice associato alla dichiarazione.
            Document document = documentContainer.document(); // Assegna il valore calcolato alla variabile Document document.

            aggiungiIntestazione(document, "Fattura", valueOrPlaceholder(fattura.getNumeroFattura())); // Esegue l'istruzione terminata dal punto e virgola.
            aggiungiDivider(document); // Esegue l'istruzione terminata dal punto e virgola.

            aggiungiSezione(document, "Dati fattura", table -> { // Apre il blocco di codice associato alla dichiarazione.
                aggiungiRiga(table, "Numero fattura", valueOrPlaceholder(fattura.getNumeroFattura())); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Data emissione", // Esegue l'istruzione necessaria alla logica applicativa.
                        fattura.getDataEmissione() != null ? DATE_FORMATTER.format(fattura.getDataEmissione()) : "-"); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Stato", valueOrPlaceholder(fattura.getStato())); // Esegue l'istruzione terminata dal punto e virgola.
            }); // Chiude il blocco di codice precedente.

            aggiungiSezione(document, "Riepilogo importi", table -> { // Apre il blocco di codice associato alla dichiarazione.
                aggiungiRiga(table, "Imponibile", formatCurrency(fattura.getImponibile())); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "IVA", formatCurrency(fattura.getIva())); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Totale", formatCurrency(fattura.getTotale())); // Esegue l'istruzione terminata dal punto e virgola.
            }); // Chiude il blocco di codice precedente.

            Cliente cliente = fattura.getCliente(); // Assegna il valore calcolato alla variabile Cliente cliente.
            aggiungiSezione(document, "Cliente", table -> { // Apre il blocco di codice associato alla dichiarazione.
                aggiungiRiga(table, "Cliente", renderCliente(cliente)); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Indirizzo", // Esegue l'istruzione necessaria alla logica applicativa.
                        cliente != null ? valueOrPlaceholder(cliente.getIndirizzo()) : "-"); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Email", cliente != null ? valueOrPlaceholder(cliente.getEmail()) : "-"); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Telefono", cliente != null ? valueOrPlaceholder(cliente.getTelefono()) : "-"); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Partita IVA", cliente != null ? valueOrPlaceholder(cliente.getPartitaIva()) : "-"); // Esegue l'istruzione terminata dal punto e virgola.
            }); // Chiude il blocco di codice precedente.

            Contratto contratto = fattura.getContratto(); // Assegna il valore calcolato alla variabile Contratto contratto.
            aggiungiSezione(document, "Contratto collegato", table -> { // Apre il blocco di codice associato alla dichiarazione.
                aggiungiRiga(table, "Codice contratto", // Esegue l'istruzione necessaria alla logica applicativa.
                        contratto != null && contratto.getIdContratto() != null // Esegue l'istruzione necessaria alla logica applicativa.
                                ? String.valueOf(contratto.getIdContratto()) // Esegue l'istruzione necessaria alla logica applicativa.
                                : "-"); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Stato", contratto != null ? valueOrPlaceholder(contratto.getStato()) : "-"); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Periodo", // Esegue l'istruzione necessaria alla logica applicativa.
                        contratto != null ? formatPeriodo(contratto.getDataInizio(), contratto.getDataFine()) : "-"); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Importo", contratto != null ? formatCurrency(contratto.getImporto()) : "-"); // Esegue l'istruzione terminata dal punto e virgola.
            }); // Chiude il blocco di codice precedente.

            Servizio servizio = contratto != null ? contratto.getServizio() : null; // Assegna il valore calcolato alla variabile Servizio servizio.
            aggiungiSezione(document, "Dettagli servizio", table -> { // Apre il blocco di codice associato alla dichiarazione.
                aggiungiRiga(table, "Servizio", servizio != null ? valueOrPlaceholder(servizio.getNome()) : "-"); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Descrizione", // Esegue l'istruzione necessaria alla logica applicativa.
                        servizio != null ? valueOrPlaceholder(servizio.getDescrizione()) : "-"); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Commissione", // Esegue l'istruzione necessaria alla logica applicativa.
                        servizio != null ? formatPercentage(servizio.getCommissionePercentuale()) : "-"); // Esegue l'istruzione terminata dal punto e virgola.
                aggiungiRiga(table, "Prezzo base", // Esegue l'istruzione necessaria alla logica applicativa.
                        servizio != null ? formatCurrency(servizio.getPrezzoBase()) : "-"); // Esegue l'istruzione terminata dal punto e virgola.
            }); // Chiude il blocco di codice precedente.

            aggiungiFooter(document); // Esegue l'istruzione terminata dal punto e virgola.
            return out.toByteArray(); // Restituisce il risultato dell'espressione out.toByteArray().
        } catch (DocumentException | IOException e) { // Apre il blocco di codice associato alla dichiarazione.
            throw new IllegalStateException("Impossibile generare il PDF della fattura", e); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    private void aggiungiIntestazione(Document document, String titolo, String codiceRiferimento) throws DocumentException { // Definisce il metodo aggiungiIntestazione che supporta la logica di dominio.
        PdfPTable header = new PdfPTable(new float[] { 2f, 1f }); // Assegna il valore calcolato alla variabile PdfPTable header.
        header.setWidthPercentage(100f); // Esegue l'istruzione terminata dal punto e virgola.
        header.setSpacingAfter(16f); // Esegue l'istruzione terminata dal punto e virgola.

        PdfPCell brandCell = new PdfPCell(); // Assegna il valore calcolato alla variabile PdfPCell brandCell.
        brandCell.setPadding(12f); // Esegue l'istruzione terminata dal punto e virgola.
        brandCell.setBackgroundColor(PRIMARY_COLOR); // Esegue l'istruzione terminata dal punto e virgola.
        brandCell.setBorder(PdfPCell.NO_BORDER); // Esegue l'istruzione terminata dal punto e virgola.

        Paragraph brand = new Paragraph("Gestore Agenti", brandFont); // Assegna il valore calcolato alla variabile Paragraph brand.
        brand.setSpacingAfter(4f); // Esegue l'istruzione terminata dal punto e virgola.
        brandCell.addElement(brand); // Esegue l'istruzione terminata dal punto e virgola.
        Paragraph subtitle = new Paragraph("Soluzioni gestionali per reti commerciali", headerSmallFont); // Assegna il valore calcolato alla variabile Paragraph subtitle.
        brandCell.addElement(subtitle); // Esegue l'istruzione terminata dal punto e virgola.
        header.addCell(brandCell); // Esegue l'istruzione terminata dal punto e virgola.

        PdfPCell titleCell = new PdfPCell(); // Assegna il valore calcolato alla variabile PdfPCell titleCell.
        titleCell.setPadding(12f); // Esegue l'istruzione terminata dal punto e virgola.
        titleCell.setBackgroundColor(PRIMARY_COLOR); // Esegue l'istruzione terminata dal punto e virgola.
        titleCell.setBorder(PdfPCell.NO_BORDER); // Esegue l'istruzione terminata dal punto e virgola.

        Paragraph docTitle = new Paragraph(titolo.toUpperCase(Locale.ITALIAN), headerFont); // Assegna il valore calcolato alla variabile Paragraph docTitle.
        docTitle.setAlignment(Element.ALIGN_RIGHT); // Esegue l'istruzione terminata dal punto e virgola.
        titleCell.addElement(docTitle); // Esegue l'istruzione terminata dal punto e virgola.

        Paragraph docCode = new Paragraph("Riferimento: " + valueOrPlaceholder(codiceRiferimento), headerSmallFont); // Assegna il valore calcolato alla variabile Paragraph docCode.
        docCode.setAlignment(Element.ALIGN_RIGHT); // Esegue l'istruzione terminata dal punto e virgola.
        titleCell.addElement(docCode); // Esegue l'istruzione terminata dal punto e virgola.

        header.addCell(titleCell); // Esegue l'istruzione terminata dal punto e virgola.
        document.add(header); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private void aggiungiDivider(Document document) throws DocumentException { // Definisce il metodo aggiungiDivider che supporta la logica di dominio.
        PdfPTable divider = new PdfPTable(1); // Assegna il valore calcolato alla variabile PdfPTable divider.
        divider.setWidthPercentage(100f); // Esegue l'istruzione terminata dal punto e virgola.
        divider.setSpacingAfter(12f); // Esegue l'istruzione terminata dal punto e virgola.

        PdfPCell cell = new PdfPCell(); // Assegna il valore calcolato alla variabile PdfPCell cell.
        cell.setFixedHeight(2f); // Esegue l'istruzione terminata dal punto e virgola.
        cell.setBorder(PdfPCell.NO_BORDER); // Esegue l'istruzione terminata dal punto e virgola.
        cell.setBackgroundColor(PRIMARY_COLOR); // Esegue l'istruzione terminata dal punto e virgola.
        divider.addCell(cell); // Esegue l'istruzione terminata dal punto e virgola.

        document.add(divider); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private void aggiungiSezione(Document document, String titolo, Consumer<PdfPTable> builder) // Definisce il metodo aggiungiSezione che supporta la logica di dominio.
            throws DocumentException { // Apre il blocco di codice associato alla dichiarazione.
        Paragraph section = new Paragraph(titolo.toUpperCase(Locale.ITALIAN), sectionFont); // Assegna il valore calcolato alla variabile Paragraph section.
        section.setSpacingBefore(6f); // Esegue l'istruzione terminata dal punto e virgola.
        section.setSpacingAfter(4f); // Esegue l'istruzione terminata dal punto e virgola.
        document.add(section); // Esegue l'istruzione terminata dal punto e virgola.

        PdfPTable table = creaTabellaSezione(); // Assegna il valore calcolato alla variabile PdfPTable table.
        builder.accept(table); // Esegue l'istruzione terminata dal punto e virgola.
        table.setSpacingAfter(14f); // Esegue l'istruzione terminata dal punto e virgola.
        document.add(table); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private PdfPTable creaTabellaSezione() { // Definisce il metodo creaTabellaSezione che supporta la logica di dominio.
        PdfPTable table = new PdfPTable(new float[] { 1f, 2f }); // Assegna il valore calcolato alla variabile PdfPTable table.
        table.setWidthPercentage(100f); // Esegue l'istruzione terminata dal punto e virgola.
        table.setSpacingBefore(2f); // Esegue l'istruzione terminata dal punto e virgola.
        return table; // Restituisce il risultato dell'espressione table.
    } // Chiude il blocco di codice precedente.

    private void aggiungiRiga(PdfPTable table, String etichetta, String valore) { // Definisce il metodo aggiungiRiga che supporta la logica di dominio.
        PdfPCell labelCell = new PdfPCell(new Phrase(valueOrPlaceholder(etichetta), labelFont)); // Assegna il valore calcolato alla variabile PdfPCell labelCell.
        labelCell.setBackgroundColor(LIGHT_BACKGROUND); // Esegue l'istruzione terminata dal punto e virgola.
        labelCell.setPadding(8f); // Esegue l'istruzione terminata dal punto e virgola.
        labelCell.setBorderColor(new Color(220, 224, 231)); // Esegue l'istruzione terminata dal punto e virgola.
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT); // Esegue l'istruzione terminata dal punto e virgola.
        table.addCell(labelCell); // Esegue l'istruzione terminata dal punto e virgola.

        PdfPCell valueCell = new PdfPCell(new Phrase(valueOrPlaceholder(valore), valueFont)); // Assegna il valore calcolato alla variabile PdfPCell valueCell.
        valueCell.setPadding(8f); // Esegue l'istruzione terminata dal punto e virgola.
        valueCell.setBorderColor(new Color(220, 224, 231)); // Esegue l'istruzione terminata dal punto e virgola.
        valueCell.setHorizontalAlignment(Element.ALIGN_LEFT); // Esegue l'istruzione terminata dal punto e virgola.
        table.addCell(valueCell); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private void aggiungiFooter(Document document) throws DocumentException { // Definisce il metodo aggiungiFooter che supporta la logica di dominio.
        Paragraph footer = new Paragraph( // Esegue l'istruzione necessaria alla logica applicativa.
                "Gestore Agenti S.r.l. · Via Roma 1, 20100 Milano · info@gestoreagenti.example · P.IVA 01234567890", // Esegue l'istruzione necessaria alla logica applicativa.
                footerFont); // Esegue l'istruzione terminata dal punto e virgola.
        footer.setAlignment(Element.ALIGN_CENTER); // Esegue l'istruzione terminata dal punto e virgola.
        footer.setSpacingBefore(24f); // Esegue l'istruzione terminata dal punto e virgola.
        document.add(footer); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private String formatCurrency(java.math.BigDecimal value) { // Definisce il metodo formatCurrency che supporta la logica di dominio.
        return value != null ? CURRENCY_FORMATTER.format(value) : "-"; // Restituisce il risultato dell'espressione value != null ? CURRENCY_FORMATTER.format(value) : "-".
    } // Chiude il blocco di codice precedente.

    private String formatPercentage(java.math.BigDecimal value) { // Definisce il metodo formatPercentage che supporta la logica di dominio.
        if (value == null) { // Valuta la condizione per controllare il flusso applicativo.
            return "-"; // Restituisce il risultato dell'espressione "-".
        } // Chiude il blocco di codice precedente.
        NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.ITALY); // Assegna il valore calcolato alla variabile NumberFormat percentFormat.
        percentFormat.setMinimumFractionDigits(2); // Esegue l'istruzione terminata dal punto e virgola.
        percentFormat.setMaximumFractionDigits(2); // Esegue l'istruzione terminata dal punto e virgola.
        return percentFormat.format(value.divide(java.math.BigDecimal.valueOf(100))); // Restituisce il risultato dell'espressione percentFormat.format(value.divide(java.math.BigDecimal.valueOf(100))).
    } // Chiude il blocco di codice precedente.

    private String formatPeriodo(java.time.LocalDate inizio, java.time.LocalDate fine) { // Definisce il metodo formatPeriodo che supporta la logica di dominio.
        if (inizio == null && fine == null) { // Valuta la condizione per controllare il flusso applicativo.
            return "-"; // Restituisce il risultato dell'espressione "-".
        } // Chiude il blocco di codice precedente.
        StringBuilder builder = new StringBuilder(); // Assegna il valore calcolato alla variabile StringBuilder builder.
        if (inizio != null) { // Valuta la condizione per controllare il flusso applicativo.
            builder.append(DATE_FORMATTER.format(inizio)); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        builder.append(" - "); // Esegue l'istruzione terminata dal punto e virgola.
        if (fine != null) { // Valuta la condizione per controllare il flusso applicativo.
            builder.append(DATE_FORMATTER.format(fine)); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        return builder.toString(); // Restituisce il risultato dell'espressione builder.toString().
    } // Chiude il blocco di codice precedente.

    private String renderCliente(Cliente cliente) { // Definisce il metodo renderCliente che supporta la logica di dominio.
        if (cliente == null) { // Valuta la condizione per controllare il flusso applicativo.
            return "-"; // Restituisce il risultato dell'espressione "-".
        } // Chiude il blocco di codice precedente.
        if (cliente.getRagioneSociale() != null && !cliente.getRagioneSociale().isBlank()) { // Valuta la condizione per controllare il flusso applicativo.
            return cliente.getRagioneSociale(); // Restituisce il risultato dell'espressione cliente.getRagioneSociale().
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
        return builder.length() > 0 ? builder.toString() : "-"; // Restituisce il risultato dell'espressione builder.length() > 0 ? builder.toString() : "-".
    } // Chiude il blocco di codice precedente.

    private String valueOrPlaceholder(String value) { // Definisce il metodo valueOrPlaceholder che supporta la logica di dominio.
        return value != null && !value.isBlank() ? value : "-"; // Restituisce il risultato dell'espressione value != null && !value.isBlank() ? value : "-".
    } // Chiude il blocco di codice precedente.

    private static final class DocumentContainer implements AutoCloseable { // Apre il blocco di codice associato alla dichiarazione.

        private final Document document; // Dichiara il campo document dell'oggetto.

        private DocumentContainer(Document document) { // Definisce il metodo DocumentContainer che supporta la logica di dominio.
            this.document = document; // Aggiorna il campo document dell'istanza.
        } // Chiude il blocco di codice precedente.

        static DocumentContainer open(ByteArrayOutputStream out) throws DocumentException { // Definisce il metodo open che supporta la logica di dominio.
            Document document = new Document(PageSize.A4, 42, 42, 60, 48); // Assegna il valore calcolato alla variabile Document document.
            PdfWriter.getInstance(document, out); // Esegue l'istruzione terminata dal punto e virgola.
            document.open(); // Esegue l'istruzione terminata dal punto e virgola.
            return new DocumentContainer(document); // Restituisce il risultato dell'espressione new DocumentContainer(document).
        } // Chiude il blocco di codice precedente.

        Document document() { // Apre il blocco di codice associato alla dichiarazione.
            return document; // Restituisce il risultato dell'espressione document.
        } // Chiude il blocco di codice precedente.

        @Override // Applica l'annotazione @Override per configurare il componente.
        public void close() { // Definisce il metodo close che supporta la logica di dominio.
            document.close(); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

