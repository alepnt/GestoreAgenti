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
import com.example.GestoreAgenti.model.Dipendente;
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
 * Genera un PDF basato su un template condiviso per tutti i contratti.
 */
@Service
public class ContrattoPdfReportService {

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

    public byte[] generaPdf(Contratto contratto) {
        Objects.requireNonNull(contratto, "contratto");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 42, 42, 60, 48);
            PdfWriter.getInstance(document, out);
            document.open();

            aggiungiIntestazione(document, "Contratto", formatoCodiceContratto(contratto));
            aggiungiDivider(document);

            aggiungiSezione(document, "Dati contratto", table -> {
                aggiungiRiga(table, "Codice contratto",
                        contratto.getIdContratto() != null ? String.valueOf(contratto.getIdContratto()) : "-");
                aggiungiRiga(table, "Stato", valueOrPlaceholder(contratto.getStato()));
                aggiungiRiga(table, "Data inizio",
                        contratto.getDataInizio() != null ? DATE_FORMATTER.format(contratto.getDataInizio()) : "-");
                aggiungiRiga(table, "Data fine",
                        contratto.getDataFine() != null ? DATE_FORMATTER.format(contratto.getDataFine()) : "-");
                aggiungiRiga(table, "Importo", formatCurrency(contratto.getImporto()));
            });

            Cliente cliente = contratto.getCliente();
            aggiungiSezione(document, "Cliente", table -> {
                aggiungiRiga(table, "Cliente", renderPersona(cliente));
                aggiungiRiga(table, "Email", cliente != null ? valueOrPlaceholder(cliente.getEmail()) : "-");
                aggiungiRiga(table, "Telefono", cliente != null ? valueOrPlaceholder(cliente.getTelefono()) : "-");
                aggiungiRiga(table, "Indirizzo", cliente != null ? valueOrPlaceholder(cliente.getIndirizzo()) : "-");
                aggiungiRiga(table, "Partita IVA", cliente != null ? valueOrPlaceholder(cliente.getPartitaIva()) : "-");
            });

            Dipendente dipendente = contratto.getDipendente();
            aggiungiSezione(document, "Consulente di riferimento", table -> {
                aggiungiRiga(table, "Dipendente", renderPersona(dipendente));
                aggiungiRiga(table, "Email", dipendente != null ? valueOrPlaceholder(dipendente.getEmail()) : "-");
                aggiungiRiga(table, "Telefono", dipendente != null ? valueOrPlaceholder(dipendente.getTelefono()) : "-");
                aggiungiRiga(table, "Team", dipendente != null ? valueOrPlaceholder(dipendente.getTeam()) : "-");
                aggiungiRiga(table, "Ranking", dipendente != null ? valueOrPlaceholder(dipendente.getRanking()) : "-");
            });

            Servizio servizio = contratto.getServizio();
            aggiungiSezione(document, "Servizio erogato", table -> {
                aggiungiRiga(table, "Servizio", servizio != null ? valueOrPlaceholder(servizio.getNome()) : "-");
                aggiungiRiga(table, "Descrizione", servizio != null ? valueOrPlaceholder(servizio.getDescrizione()) : "-");
                aggiungiRiga(table, "Prezzo base", servizio != null ? formatCurrency(servizio.getPrezzoBase()) : "-");
                aggiungiRiga(table, "Commissione", servizio != null ? formatPercentage(servizio.getCommissionePercentuale()) : "-");
            });

            aggiungiBloccoNote(document, contratto.getNote());
            aggiungiFooter(document);

            document.close();
            return out.toByteArray();
        } catch (DocumentException e) {
            throw new IllegalStateException("Impossibile generare il PDF del contratto", e);
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

    private void aggiungiBloccoNote(Document document, String note) throws DocumentException {
        Paragraph section = new Paragraph("NOTE", sectionFont);
        section.setSpacingBefore(6f);
        section.setSpacingAfter(4f);
        document.add(section);

        PdfPCell noteCell = new PdfPCell(new Phrase(valueOrPlaceholder(note), valueFont));
        noteCell.setPadding(10f);
        noteCell.setBorderColor(new Color(220, 224, 231));

        PdfPTable noteTable = new PdfPTable(1);
        noteTable.setWidthPercentage(100f);
        noteTable.addCell(noteCell);
        noteTable.setSpacingAfter(14f);
        document.add(noteTable);
    }

    private void aggiungiFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph(
                "Gestore Agenti S.r.l. · Via Roma 1, 20100 Milano · info@gestoreagenti.example · P.IVA 01234567890",
                footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(24f);
        document.add(footer);
    }

    private String formatoCodiceContratto(Contratto contratto) {
        if (contratto == null || contratto.getIdContratto() == null) {
            return "-";
        }
        return "CTR-" + contratto.getIdContratto();
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

    private String renderPersona(Cliente cliente) {
        if (cliente == null) {
            return "-";
        }
        if (cliente.getRagioneSociale() != null && !cliente.getRagioneSociale().isBlank()) {
            return cliente.getRagioneSociale();
        }
        return buildFullName(cliente.getNome(), cliente.getCognome());
    }

    private String renderPersona(Dipendente dipendente) {
        if (dipendente == null) {
            return "-";
        }
        return buildFullName(dipendente.getNome(), dipendente.getCognome());
    }

    private String buildFullName(String nome, String cognome) {
        StringBuilder builder = new StringBuilder();
        if (nome != null && !nome.isBlank()) {
            builder.append(nome.trim());
        }
        if (cognome != null && !cognome.isBlank()) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(cognome.trim());
        }
        return builder.length() > 0 ? builder.toString() : "-";
    }

    private String valueOrPlaceholder(String value) {
        return value != null && !value.isBlank() ? value : "-";
    }
}

