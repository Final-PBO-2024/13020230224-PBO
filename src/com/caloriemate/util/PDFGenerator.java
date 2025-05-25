package com.caloriemate.util;

import com.caloriemate.controller.FoodController;
import com.caloriemate.model.Food;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PDFGenerator {
    public static void generateReport(String username) {
        FoodController foodController = new FoodController();
        List<Food> foods;
        try {
            foods = foodController.getFoods(username, "");
            System.out.println("Jumlah makanan yang diambil: " + foods.size()); // Debugging
        } catch (Exception e) {
            System.err.println("Error mengambil data makanan: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));
            document.open();

            // Judul
            Paragraph title = new Paragraph("Laporan Kalori - CalorieMate");
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Tabel
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Header Tabel
            table.addCell("ID");
            table.addCell("Nama");
            table.addCell("Kalori");
            table.addCell("Kategori");
            table.addCell("Tanggal");

            // Data Tabel
            if (foods.isEmpty()) {
                System.out.println("Tidak ada data makanan untuk dimasukkan ke PDF.");
                Paragraph emptyMessage = new Paragraph("Tidak ada data makanan yang tersedia untuk pengguna ini.");
                document.add(emptyMessage);
            } else {
                for (Food food : foods) {
                    table.addCell(String.valueOf(food.getId()));
                    table.addCell(food.getName());
                    table.addCell(String.valueOf(food.getCalories()));
                    table.addCell(food.getCategory());
                    table.addCell(food.getDate().toString());
                }
                document.add(table);
            }

        } catch (DocumentException e) {
            System.err.println("Error saat membuat dokumen PDF: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error saat menulis file PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }
}