package com.example.GestoreAgenti.service.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Cliente;
import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.model.Fattura;
import com.example.GestoreAgenti.model.Servizio;

/**
 * Servizio applicativo responsabile della generazione dei report Excel delle fatture.
 */
@Service
public class FatturaExcelReportService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] generaReport(List<Fattura> fatture) {
        Objects.requireNonNull(fatture, "fatture");

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CellStyle headerStyle = createHeaderStyle(workbook);
            Map<InvoiceState, SheetContext> contexts = new EnumMap<>(InvoiceState.class);
            List<SheetContext> orderedContexts = new ArrayList<>();

            for (Fattura fattura : fatture) {
                if (fattura == null) {
                    continue;
                }
                InvoiceState stato = resolveState(fattura);
                SheetContext context = contexts.computeIfAbsent(stato, t -> {
                    SheetContext created = createSheet(workbook, t, headerStyle);
                    orderedContexts.add(created);
                    return created;
                });

                Row row = context.sheet.createRow(context.nextRow++);
                List<String> values = renderValues(fattura, stato);
                for (int i = 0; i < values.size(); i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(values.get(i));
                }
            }

            for (SheetContext context : orderedContexts) {
                for (int columnIndex = 0; columnIndex < context.columnCount; columnIndex++) {
                    context.sheet.autoSizeColumn(columnIndex);
                }
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Impossibile generare il report Excel delle fatture", e);
        }
    }

    private SheetContext createSheet(Workbook workbook, InvoiceState stato, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet(stato.getLabel());
        String[] headers = headerFor(stato);
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        return new SheetContext(sheet, 1, headers.length);
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private String[] headerFor(InvoiceState stato) {
        return switch (stato) {
            case EMESSA -> new String[] {"Numero", "Data emissione", "Cliente", "Contratto", "Imponibile", "IVA", "Totale",
                    "Stato"};
            case IN_SOLLECITO -> new String[] {"Numero", "Cliente", "Contratto", "Data emissione", "Totale fattura",
                    "Stato"};
            case SALDATA -> new String[] {"Numero", "Data emissione", "Cliente", "Totale", "Commissione servizio",
                    "Servizio", "Stato"};
        };
    }

    private List<String> renderValues(Fattura fattura, InvoiceState stato) {
        return switch (stato) {
            case EMESSA -> List.of(
                    valueOrEmpty(fattura.getNumeroFattura()),
                    formatDate(fattura.getDataEmissione()),
                    formatCliente(fattura.getCliente()),
                    formatContratto(fattura.getContratto()),
                    formatNumber(fattura.getImponibile()),
                    formatNumber(fattura.getIva()),
                    formatNumber(fattura.getTotale()),
                    valueOrEmpty(fattura.getStato()));
            case IN_SOLLECITO -> {
                Contratto contratto = fattura.getContratto();
                yield List.of(
                        valueOrEmpty(fattura.getNumeroFattura()),
                        formatCliente(fattura.getCliente()),
                        formatContratto(contratto),
                        formatDate(fattura.getDataEmissione()),
                        formatNumber(fattura.getTotale()),
                        valueOrEmpty(fattura.getStato()));
            }
            case SALDATA -> {
                Contratto contratto = fattura.getContratto();
                Servizio servizio = contratto != null ? contratto.getServizio() : null;
                yield List.of(
                        valueOrEmpty(fattura.getNumeroFattura()),
                        formatDate(fattura.getDataEmissione()),
                        formatCliente(fattura.getCliente()),
                        formatNumber(fattura.getTotale()),
                        servizio != null ? formatNumber(servizio.getCommissionePercentuale()) : "",
                        servizio != null ? valueOrEmpty(servizio.getNome()) : "",
                        valueOrEmpty(fattura.getStato()));
            }
        };
    }

    private InvoiceState resolveState(Fattura fattura) {
        return fattura != null ? InvoiceState.fromPersistence(fattura.getStato()) : InvoiceState.EMESSA;
    }

    private String formatCliente(Cliente cliente) {
        if (cliente == null) {
            return "";
        }
        String denominazione = cliente.getRagioneSociale();
        if (denominazione != null && !denominazione.isBlank()) {
            return denominazione;
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
        return builder.toString();
    }

    private String formatContratto(Contratto contratto) {
        if (contratto == null) {
            return "";
        }
        if (contratto.getIdContratto() != null) {
            return "CTR-" + contratto.getIdContratto();
        }
        return "Contratto";
    }

    private String formatDate(java.time.LocalDate date) {
        return date != null ? DATE_FORMAT.format(date) : "";
    }

    private String formatNumber(java.math.BigDecimal value) {
        return value != null ? value.toPlainString() : "";
    }

    private String valueOrEmpty(String value) {
        return value != null ? value : "";
    }

    private static final class SheetContext {

        private final Sheet sheet;
        private final int columnCount;
        private int nextRow;

        private SheetContext(Sheet sheet, int nextRow, int columnCount) {
            this.sheet = sheet;
            this.nextRow = nextRow;
            this.columnCount = columnCount;
        }
    }

    private enum InvoiceState {
        EMESSA("Emessa"),
        IN_SOLLECITO("In sollecito"),
        SALDATA("Saldato");

        private final String label;

        InvoiceState(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public static InvoiceState fromPersistence(String raw) {
            if (raw == null || raw.isBlank()) {
                return EMESSA;
            }
            String normalized = raw.trim().toUpperCase(Locale.ROOT);
            return switch (normalized) {
                case "IN_SOLLECITO" -> IN_SOLLECITO;
                case "SALDATA", "SALDATO", "PAGATA", "PAGATO" -> SALDATA;
                default -> EMESSA;
            };
        }
    }
}
