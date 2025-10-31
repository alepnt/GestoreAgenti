package com.example.GestoreAgenti.service.report;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

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

    private static final Color PRIMARY_COLOR = new Color(32, 60, 117);
    private static final Color LIGHT_BACKGROUND = new Color(244, 247, 252);

    private final Font brandFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.WHITE);
    private final Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
    private final Font headerSmallFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.WHITE);
    private final Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, PRIMARY_COLOR);
    private final Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new Color(68, 68, 68));
    private final Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
    private final Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, new Color(120, 120, 120));

    public byte[] generaPdf(Fattura fattura) {
        Objects.requireNonNull(fattura, "fattura");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 42, 42, 60, 48);
            PdfWriter.getInstance(document, out);
            document.open();

            aggiungiIntestazione(document, "Fattura", valueOrPlaceholder(fattura.getNumeroFattura()));
            aggiungiDivider(document);

            aggiungiSezione(document, "Dati fattura", table -> {
                aggiungiRiga(table, "Numero fattura", valueOrPlaceholder(fattura.getNumeroFattura()));
                aggiungiRiga(table, "Data emissione",
                        fattura.getDataEmissione() != null ? DATE_FORMATTER.format(fattura.getDataEmissione()) : "-");
                aggiungiRiga(table, "Stato", valueOrPlaceholder(fattura.getStato()));
            });

            aggiungiSezione(document, "Riepilogo importi", table -> {
                aggiungiRiga(table, "Imponibile", formatCurrency(fattura.getImponibile()));
                aggiungiRiga(table, "IVA", formatCurrency(fattura.getIva()));
                aggiungiRiga(table, "Totale", formatCurrency(fattura.getTotale()));
            });

            Cliente cliente = fattura.getCliente();
            aggiungiSezione(document, "Cliente", table -> {
                aggiungiRiga(table, "Cliente", renderCliente(cliente));
                aggiungiRiga(table, "Indirizzo",
                        cliente != null ? valueOrPlaceholder(cliente.getIndirizzo()) : "-");
                aggiungiRiga(table, "Email", cliente != null ? valueOrPlaceholder(cliente.getEmail()) : "-");
                aggiungiRiga(table, "Telefono", cliente != null ? valueOrPlaceholder(cliente.getTelefono()) : "-");
                aggiungiRiga(table, "Partita IVA", cliente != null ? valueOrPlaceholder(cliente.getPartitaIva()) : "-");
            });

            Contratto contratto = fattura.getContratto();
            aggiungiSezione(document, "Contratto collegato", table -> {
                aggiungiRiga(table, "Codice contratto",
                        contratto != null && contratto.getIdContratto() != null
                                ? String.valueOf(contratto.getIdContratto())
                                : "-");
                aggiungiRiga(table, "Stato", contratto != null ? valueOrPlaceholder(contratto.getStato()) : "-");
                aggiungiRiga(table, "Periodo",
                        contratto != null ? formatPeriodo(contratto.getDataInizio(), contratto.getDataFine()) : "-");
                aggiungiRiga(table, "Importo", contratto != null ? formatCurrency(contratto.getImporto()) : "-");
            });

            Servizio servizio = contratto != null ? contratto.getServizio() : null;
            aggiungiSezione(document, "Dettagli servizio", table -> {
                aggiungiRiga(table, "Servizio", servizio != null ? valueOrPlaceholder(servizio.getNome()) : "-");
                aggiungiRiga(table, "Descrizione",
                        servizio != null ? valueOrPlaceholder(servizio.getDescrizione()) : "-");
                aggiungiRiga(table, "Commissione",
                        servizio != null ? formatPercentage(servizio.getCommissionePercentuale()) : "-");
                aggiungiRiga(table, "Prezzo base",
                        servizio != null ? formatCurrency(servizio.getPrezzoBase()) : "-");
            });

            aggiungiFooter(document);

            document.close();
            return out.toByteArray();
        } catch (DocumentException e) {
            throw new IllegalStateException("Impossibile generare il PDF della fattura", e);
        }
    }

    private void aggiungiIntestazione(Document document, String titolo, String codiceRiferimento) throws DocumentException {
        PdfPTable header = new PdfPTable(new float[] { 2f, 1f });
        header.setWidthPercentage(100f);
        header.setSpacingAfter(16f);

        PdfPCell brandCell = new PdfPCell();
        brandCell.setPadding(12f);
        brandCell.setBackgroundColor(PRIMARY_COLOR);
        brandCell.setBorder(PdfPCell.NO_BORDER);

        Paragraph brand = new Paragraph("Gestore Agenti", brandFont);
        brand.setSpacingAfter(4f);
        brandCell.addElement(brand);
        Paragraph subtitle = new Paragraph("Soluzioni gestionali per reti commerciali", headerSmallFont);
        brandCell.addElement(subtitle);
        header.addCell(brandCell);

        PdfPCell titleCell = new PdfPCell();
        titleCell.setPadding(12f);
        titleCell.setBackgroundColor(PRIMARY_COLOR);
        titleCell.setBorder(PdfPCell.NO_BORDER);

        Paragraph docTitle = new Paragraph(titolo.toUpperCase(Locale.ITALIAN), headerFont);
        docTitle.setAlignment(Element.ALIGN_RIGHT);
        titleCell.addElement(docTitle);

        Paragraph docCode = new Paragraph("Riferimento: " + valueOrPlaceholder(codiceRiferimento), headerSmallFont);
        docCode.setAlignment(Element.ALIGN_RIGHT);
        titleCell.addElement(docCode);

        header.addCell(titleCell);
        document.add(header);
    }

    private void aggiungiDivider(Document document) throws DocumentException {
        PdfPTable divider = new PdfPTable(1);
        divider.setWidthPercentage(100f);
        divider.setSpacingAfter(12f);

        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(2f);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(PRIMARY_COLOR);
        divider.addCell(cell);

        document.add(divider);
    }

    private void aggiungiSezione(Document document, String titolo, Consumer<PdfPTable> builder)
            throws DocumentException {
        Paragraph section = new Paragraph(titolo.toUpperCase(Locale.ITALIAN), sectionFont);
        section.setSpacingBefore(6f);
        section.setSpacingAfter(4f);
        document.add(section);

        PdfPTable table = creaTabellaSezione();
        builder.accept(table);
        table.setSpacingAfter(14f);
        document.add(table);
    }

    private PdfPTable creaTabellaSezione() {
        PdfPTable table = new PdfPTable(new float[] { 1f, 2f });
        table.setWidthPercentage(100f);
        table.setSpacingBefore(2f);
        return table;
    }

    private void aggiungiRiga(PdfPTable table, String etichetta, String valore) {
        PdfPCell labelCell = new PdfPCell(new Phrase(valueOrPlaceholder(etichetta), labelFont));
        labelCell.setBackgroundColor(LIGHT_BACKGROUND);
        labelCell.setPadding(8f);
        labelCell.setBorderColor(new Color(220, 224, 231));
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(valueOrPlaceholder(valore), valueFont));
        valueCell.setPadding(8f);
        valueCell.setBorderColor(new Color(220, 224, 231));
        valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(valueCell);
    }

    private void aggiungiFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph(
                "Gestore Agenti S.r.l. · Via Roma 1, 20100 Milano · info@gestoreagenti.example · P.IVA 01234567890",
                footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(24f);
        document.add(footer);
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

