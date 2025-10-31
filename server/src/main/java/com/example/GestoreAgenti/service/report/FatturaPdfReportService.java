package com.example.GestoreAgenti.service.report;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Cliente;
import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.model.Fattura;
import com.example.GestoreAgenti.model.Servizio;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Genera un PDF coerente per la rappresentazione di una fattura. Il layout è
 * stato progettato per essere riutilizzabile per tutte le fatture in modo da
 * garantire uniformità visiva.
 */
@Service
public class FatturaPdfReportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.ITALY);

    private final Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
    private final Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, Color.BLACK);
    private final Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.DARK_GRAY);
    private final Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

    public byte[] generaPdf(Fattura fattura) {
        Objects.requireNonNull(fattura, "fattura");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(document, out);
            document.open();

            aggiungiTitolo(document, "Fattura");

            LinkedHashMap<String, String> datiFattura = new LinkedHashMap<>();
            datiFattura.put("Numero fattura", valueOrPlaceholder(fattura.getNumeroFattura()));
            datiFattura.put("Data emissione",
                    fattura.getDataEmissione() != null ? DATE_FORMATTER.format(fattura.getDataEmissione()) : "-");
            datiFattura.put("Stato", valueOrPlaceholder(fattura.getStato()));
            aggiungiSezione(document, "Dati fattura", datiFattura);

            LinkedHashMap<String, String> importi = new LinkedHashMap<>();
            importi.put("Imponibile", formatCurrency(fattura.getImponibile()));
            importi.put("IVA", formatCurrency(fattura.getIva()));
            importi.put("Totale", formatCurrency(fattura.getTotale()));
            aggiungiSezione(document, "Riepilogo importi", importi);

            Cliente cliente = fattura.getCliente();
            LinkedHashMap<String, String> datiCliente = new LinkedHashMap<>();
            datiCliente.put("Cliente", renderCliente(cliente));
            if (cliente != null) {
                datiCliente.put("Indirizzo", valueOrPlaceholder(cliente.getIndirizzo()));
                datiCliente.put("Email", valueOrPlaceholder(cliente.getEmail()));
                datiCliente.put("Telefono", valueOrPlaceholder(cliente.getTelefono()));
                datiCliente.put("Partita IVA", valueOrPlaceholder(cliente.getPartitaIva()));
            }
            aggiungiSezione(document, "Dati cliente", datiCliente);

            Contratto contratto = fattura.getContratto();
            if (contratto != null) {
                LinkedHashMap<String, String> datiContratto = new LinkedHashMap<>();
                datiContratto.put("Codice contratto",
                        contratto.getIdContratto() != null ? String.valueOf(contratto.getIdContratto()) : "-");
                datiContratto.put("Stato", valueOrPlaceholder(contratto.getStato()));
                datiContratto.put("Periodo",
                        formatPeriodo(contratto.getDataInizio(), contratto.getDataFine()));
                datiContratto.put("Importo", formatCurrency(contratto.getImporto()));
                aggiungiSezione(document, "Contratto collegato", datiContratto);

                Servizio servizio = contratto.getServizio();
                if (servizio != null) {
                    LinkedHashMap<String, String> datiServizio = new LinkedHashMap<>();
                    datiServizio.put("Servizio", valueOrPlaceholder(servizio.getNome()));
                    datiServizio.put("Descrizione", valueOrPlaceholder(servizio.getDescrizione()));
                    datiServizio.put("Commissione", formatPercentage(servizio.getCommissionePercentuale()));
                    datiServizio.put("Prezzo base", formatCurrency(servizio.getPrezzoBase()));
                    aggiungiSezione(document, "Dettagli servizio", datiServizio);
                }
            }

            document.close();
            return out.toByteArray();
        } catch (DocumentException e) {
            throw new IllegalStateException("Impossibile generare il PDF della fattura", e);
        }
    }

    private void aggiungiTitolo(Document document, String testo) throws DocumentException {
        Paragraph title = new Paragraph(testo, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(18f);
        document.add(title);
    }

    private void aggiungiSezione(Document document, String titolo, Map<String, String> contenuto)
            throws DocumentException {
        Paragraph section = new Paragraph(titolo, sectionFont);
        section.setSpacingBefore(8f);
        section.setSpacingAfter(6f);
        document.add(section);

        PdfPTable table = new PdfPTable(new float[] { 1f, 2f });
        table.setWidthPercentage(100f);
        table.setSpacingAfter(12f);

        for (Map.Entry<String, String> entry : contenuto.entrySet()) {
            aggiungiCella(table, entry.getKey(), true);
            aggiungiCella(table, entry.getValue(), false);
        }

        document.add(table);
    }

    private void aggiungiCella(PdfPTable table, String testo, boolean label) {
        PdfPCell cell = new PdfPCell(new Phrase(valueOrPlaceholder(testo), label ? labelFont : valueFont));
        cell.setHorizontalAlignment(label ? Element.ALIGN_LEFT : Element.ALIGN_JUSTIFIED);
        cell.setPadding(8f);
        if (label) {
            cell.setBackgroundColor(new Color(237, 240, 245));
        }
        table.addCell(cell);
    }

    private String formatCurrency(java.math.BigDecimal value) {
        return value != null ? CURRENCY_FORMATTER.format(value) : "-";
    }

    private String formatPercentage(java.math.BigDecimal value) {
        if (value == null) {
            return "-";
        }
        NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.ITALY);
        percentFormat.setMinimumFractionDigits(2);
        percentFormat.setMaximumFractionDigits(2);
        return percentFormat.format(value.divide(java.math.BigDecimal.valueOf(100)));
    }

    private String formatPeriodo(java.time.LocalDate inizio, java.time.LocalDate fine) {
        if (inizio == null && fine == null) {
            return "-";
        }
        StringBuilder builder = new StringBuilder();
        if (inizio != null) {
            builder.append(DATE_FORMATTER.format(inizio));
        }
        builder.append(" - ");
        if (fine != null) {
            builder.append(DATE_FORMATTER.format(fine));
        }
        return builder.toString();
    }

    private String renderCliente(Cliente cliente) {
        if (cliente == null) {
            return "-";
        }
        if (cliente.getRagioneSociale() != null && !cliente.getRagioneSociale().isBlank()) {
            return cliente.getRagioneSociale();
        }
        StringBuilder builder = new StringBuilder();
        if (cliente.getNome() != null) {
            builder.append(cliente.getNome());
        }
        if (cliente.getCognome() != null) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(cliente.getCognome());
        }
        return builder.length() > 0 ? builder.toString() : "-";
    }

    private String valueOrPlaceholder(String value) {
        return value != null && !value.isBlank() ? value : "-";
    }
}

