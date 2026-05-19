package com.comercializadora.utilidades;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.comercializadora.modelo.entidades.DetalleFactura;
import com.comercializadora.modelo.entidades.Factura;
import java.awt.Color;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;

public class PdfGenerador {

    public static void generarFacturaPdf(Factura factura, OutputStream out) throws Exception {
        Document doc = new Document(PageSize.A4, 40, 40, 60, 40);
        PdfWriter.getInstance(doc, out);
        doc.open();

        // Colores corporativos
        Color verde      = new Color(27, 67, 50);
        Color ocre       = new Color(181, 137, 10);
        Color verdeSuave = new Color(216, 243, 220);
        Color grisLinea  = new Color(220, 220, 220);

        Font fTitulo = new Font(Font.HELVETICA, 18, Font.BOLD, Color.WHITE);
        Font fSub    = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.WHITE);
        Font fLabel  = new Font(Font.HELVETICA, 10, Font.BOLD, verde);
        Font fValor  = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.DARK_GRAY);
        Font fHead   = new Font(Font.HELVETICA, 9,  Font.BOLD, Color.WHITE);
        Font fCell   = new Font(Font.HELVETICA, 9,  Font.NORMAL, Color.DARK_GRAY);
        Font fTotal  = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);

        // ─── Encabezado ───────────────────────────────────────
        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(100);
        header.setWidths(new float[]{1.5f, 1f});

        PdfPCell cEmpresa = new PdfPCell();
        cEmpresa.setBackgroundColor(verde);
        cEmpresa.setBorder(Rectangle.NO_BORDER);
        cEmpresa.setPadding(12);
        try {
            InputStream logoStream = PdfGenerador.class.getResourceAsStream("/static/logo.jpg");
            if (logoStream == null) logoStream = PdfGenerador.class.getResourceAsStream("/logo.jpg");
            if (logoStream != null) {
                Image logo = Image.getInstance(logoStream.readAllBytes());
                logo.scaleToFit(80, 50);
                cEmpresa.addElement(logo);
            }
        } catch (Exception ignored) {}
        cEmpresa.addElement(new Paragraph("AgroComercial Andina", fTitulo));
        cEmpresa.addElement(new Paragraph("Comercializadora de Productos Agrícolas", fSub));
        header.addCell(cEmpresa);

        PdfPCell cFactura = new PdfPCell();
        cFactura.setBackgroundColor(ocre);
        cFactura.setBorder(Rectangle.NO_BORDER);
        cFactura.setPadding(12);
        cFactura.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cFactura.addElement(new Paragraph("FACTURA", fTitulo));
        cFactura.addElement(new Paragraph("Código: " + factura.getCodigo(), fSub));
        String fechaStr = factura.getFecha() != null
                ? factura.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
        cFactura.addElement(new Paragraph("Fecha: " + fechaStr, fSub));
        header.addCell(cFactura);
        doc.add(header);
        doc.add(Chunk.NEWLINE);

        // ─── Datos cliente ────────────────────────────────────
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        addInfoRow(infoTable, "Cliente:", factura.getCliente(), fLabel, fValor, verdeSuave);
        doc.add(infoTable);
        doc.add(Chunk.NEWLINE);

        // ─── Tabla de productos ───────────────────────────────
        PdfPTable items = new PdfPTable(new float[]{4f, 1.2f, 1.8f, 1.8f});
        items.setWidthPercentage(100);

        // Encabezados
        String[] cols = {"Producto", "Cant.", "Precio Unit.", "Subtotal"};
        for (String col : cols) {
            PdfPCell c = new PdfPCell(new Phrase(col, fHead));
            c.setBackgroundColor(verde);
            c.setPadding(7);
            c.setBorder(Rectangle.NO_BORDER);
            items.addCell(c);
        }

        // Filas de productos
        boolean alterno = false;
        for (DetalleFactura it : factura.getItems()) {
            Color bg = alterno ? new Color(240, 248, 240) : Color.WHITE;
            addItemCell(items, it.getProducto(),                        fCell, bg);
            addItemCell(items, String.valueOf(it.getCantidad()),         fCell, bg);
            addItemCell(items, String.format("$ %,.2f", it.getPrecio()),    fCell, bg);
            addItemCell(items, String.format("$ %,.2f", it.getSubtotal()), fCell, bg);
            alterno = !alterno;
        }

        // ── Fila separadora vacía (línea gris de separación) ──
        for (int i = 0; i < 4; i++) {
            PdfPCell sep = new PdfPCell(new Phrase(" "));
            sep.setBorder(Rectangle.TOP);
            sep.setBorderColor(grisLinea);
            sep.setBorderWidthTop(1.5f);
            sep.setPaddingTop(6);
            sep.setPaddingBottom(2);
            items.addCell(sep);
        }

        // ── Filas de subtotal, IVA y total dentro de la misma tabla ──
        // Subtotal
        addResumenRow(items, "Subtotal:",
                String.format("$ %,.2f", factura.getSubtotal()),
                fLabel, fValor, Color.WHITE, false);

        // IVA
        addResumenRow(items, "IVA (19%):",
                String.format("$ %,.2f", factura.getIva()),
                fLabel, fValor, Color.WHITE, false);

        // Separador antes del total
        for (int i = 0; i < 4; i++) {
            PdfPCell sep2 = new PdfPCell(new Phrase(" "));
            sep2.setBorder(Rectangle.TOP);
            sep2.setBorderColor(verde);
            sep2.setBorderWidthTop(2f);
            sep2.setPaddingTop(2);
            sep2.setPaddingBottom(2);
            items.addCell(sep2);
        }

        // TOTAL — fila destacada en verde
        addResumenRow(items, "TOTAL:",
                String.format("$ %,.2f", factura.getTotal()),
                fTotal, fTotal, verde, true);

        doc.add(items);

        // ─── Pie ──────────────────────────────────────────────
        doc.add(Chunk.NEWLINE);
        Paragraph pie = new Paragraph(
                "Gracias por su confianza. Este documento es generado automáticamente.",
                new Font(Font.HELVETICA, 8, Font.ITALIC, Color.GRAY));
        pie.setAlignment(Element.ALIGN_CENTER);
        doc.add(pie);

        doc.close();
    }

    // ── Fila de info cliente ──────────────────────────────────
    private static void addInfoRow(PdfPTable t, String label, String valor,
            Font fL, Font fV, Color bgLabel) {
        PdfPCell cl = new PdfPCell(new Phrase(label, fL));
        cl.setBackgroundColor(bgLabel);
        cl.setPadding(6);
        cl.setBorder(Rectangle.BOX);
        t.addCell(cl);

        PdfPCell cv = new PdfPCell(new Phrase(valor != null ? valor : "-", fV));
        cv.setPadding(6);
        cv.setBorder(Rectangle.BOX);
        t.addCell(cv);
    }

    // ── Celda de producto ─────────────────────────────────────
    private static void addItemCell(PdfPTable t, String text, Font f, Color bg) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setBackgroundColor(bg);
        c.setPadding(6);
        c.setBorderColor(new Color(200, 220, 200));
        t.addCell(c);
    }

    // ── Fila de resumen (subtotal / IVA / total) ──────────────
    // Ocupa las 4 columnas: las 2 primeras vacías, label y valor alineado a la derecha
    private static void addResumenRow(PdfPTable t, String label, String val,
            Font fL, Font fV, Color bg, boolean destacado) {

        // Celda vacía que ocupa las 2 primeras columnas
        PdfPCell vacio = new PdfPCell(new Phrase(""));
        vacio.setColspan(2);
        vacio.setBorder(Rectangle.NO_BORDER);
        vacio.setBackgroundColor(bg);
        vacio.setPadding(5);
        t.addCell(vacio);

        // Label (ej: "Subtotal:")
        PdfPCell cl = new PdfPCell(new Phrase(label, fL));
        cl.setBorder(destacado ? Rectangle.NO_BORDER : Rectangle.BOX);
        cl.setBackgroundColor(bg);
        cl.setPadding(6);
        t.addCell(cl);

        // Valor (ej: "$ 1.200,00")
        PdfPCell cv = new PdfPCell(new Phrase(val, fV));
        cv.setBorder(destacado ? Rectangle.NO_BORDER : Rectangle.BOX);
        cv.setBackgroundColor(bg);
        cv.setPadding(6);
        cv.setHorizontalAlignment(Element.ALIGN_RIGHT);
        t.addCell(cv);
    }
}
