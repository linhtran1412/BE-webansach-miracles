//package com.example.wedbansach_bakend1.Service;
//
//import com.example.wedbansach_bakend1.dao.DonHangRepository;
//import com.example.wedbansach_bakend1.dto.ReportSummaryDTO;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.sql.Date;
//import java.util.*;
//
//@Service
//public class ReportService {
//
//    private final DonHangRepository donRepo;
//
//    public ReportService(DonHangRepository donRepo) {
//        this.donRepo = donRepo;
//    }
//
//    public ReportSummaryDTO summary(Date from, Date to){
//        ReportSummaryDTO dto = new ReportSummaryDTO();
//
//        // Tổng doanh thu & tổng số đơn
//        Double revenue = donRepo.sumRevenue(from, to);
//        Long orders = donRepo.countOrders(from, to);
//        dto.setRevenue(revenue == null ? 0.0 : revenue);
//        dto.setOrders(orders == null ? 0L : orders);
//
//        // Đếm theo trạng thái
//        Map<String, Long> byStatus = new LinkedHashMap<>();
//        for (Object[] row : donRepo.countByStatus(from, to)) {
//            String status = (String) row[0];
//            Long cnt = ((Number) row[1]).longValue();
//            byStatus.put(status == null ? "UNKNOWN" : status, cnt);
//        }
//        dto.setByStatus(byStatus);
//
//        // Doanh thu theo ngày
//        List<ReportSummaryDTO.SeriesPoint> series = new ArrayList<>();
//        for (Object[] row : donRepo.revenueByDay(from, to)) {
//            Date day = (Date) row[0];
//            Double val = ((Number) row[1]).doubleValue();
//            dto.getClass(); // no-op to please some IDEs
//            series.add(new ReportSummaryDTO.SeriesPoint(day.toString(), val));
//        }
//        dto.setRevenueSeries(series);
//
//        // Top sách: bạn có thể bổ sung sau nếu cần (dựa vào ChiTietDonHang)
//        dto.setTopBooks(Collections.emptyList());
//
//        return dto;
//    }
//
//    public String exportCsv(Date from, Date to){
//        // CSV đơn giản: header + dòng dữ liệu theo ngày
//        StringBuilder sb = new StringBuilder();
//        sb.append("Ngay,DoanhThu\n");
//        for (Object[] row : donRepo.revenueByDay(from, to)) {
//            Date day = (Date) row[0];
//            Double val = ((Number) row[1]).doubleValue();
//            sb.append(day).append(",").append(val).append("\n");
//        }
//        return sb.toString();
//    }
//
//    public ByteArrayOutputStream exportPdf(Date from, Date to) throws Exception{
//        var out = new ByteArrayOutputStream();
//        var doc = new com.lowagie.text.Document();
//        com.lowagie.text.pdf.PdfWriter.getInstance(doc, out);
//        doc.open();
//        Double rev = donRepo.sumRevenue(from, to);
//        long orders = Optional.ofNullable(donRepo.countOrders(from, to)).orElse(0L);
//
//        doc.add(new com.lowagie.text.Paragraph("BÁO CÁO DOANH THU"));
//        doc.add(new com.lowagie.text.Paragraph("Khoảng thời gian: " + from + " → " + to));
//        doc.add(new com.lowagie.text.Paragraph("Tổng doanh thu: " + (rev == null ? 0.0 : rev)));
//        doc.add(new com.lowagie.text.Paragraph("Tổng số đơn: " + orders));
//        doc.add(new com.lowagie.text.Paragraph(" "));
//
//        var table = new com.lowagie.text.pdf.PdfPTable(2);
//        table.addCell("Ngày"); table.addCell("Doanh thu");
//        for (Object[] row : donRepo.revenueByDay(from, to)) {
//            table.addCell(String.valueOf(row[0]));
//            table.addCell(String.valueOf(row[1]));
//        }
//        doc.add(table);
//
//        doc.close();
//        return out;
//    }
//}


package com.example.wedbansach_bakend1.Service;

import com.example.wedbansach_bakend1.dao.DonHangRepository;
import com.example.wedbansach_bakend1.dto.ReportSummaryDTO;
import com.lowagie.text.FontFactory; // Import FontFactory nếu dùng Font PDF
import com.lowagie.text.pdf.BaseFont; // Import BaseFont nếu dùng Font PDF
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
// import java.sql.Date; // <<< BỎ IMPORT NÀY
import java.time.LocalDate; // <<< THÊM IMPORT NÀY
import java.util.*;

@Service
public class ReportService {
    private final DonHangRepository donRepo;

    public ReportService(DonHangRepository donRepo) {
        this.donRepo = donRepo;
    }

    // === THAY ĐỔI KIỂU THAM SỐ ===
    public ReportSummaryDTO summary(LocalDate from, LocalDate to){
        // === KẾT THÚC THAY ĐỔI ===
        System.out.println("[ReportService] summary called with dates: " + from + " to " + to); // Thêm log
        ReportSummaryDTO dto = new ReportSummaryDTO();

        // --- Gọi các phương thức Repository (vẫn truyền LocalDate) ---
        Double revenue = donRepo.sumRevenue(from, to);
        Long orders = donRepo.countOrders(from, to);
        dto.setRevenue(revenue == null ? 0.0 : revenue);
        dto.setOrders(orders == null ? 0L : orders);
        System.out.println("[ReportService] Revenue: " + dto.getRevenue() + ", Orders: " + dto.getOrders()); // Thêm log

        Map<String, Long> byStatus = new LinkedHashMap<>();
        try { // Thêm try-catch để xem lỗi nếu có
            List<Object[]> statusCounts = donRepo.countByStatus(from, to);
            System.out.println("[ReportService] Status counts fetched: " + statusCounts.size() + " rows"); // Thêm log
            for (Object[] row : statusCounts) {
                // Kiểm tra kiểu dữ liệu trước khi ép kiểu để tránh ClassCastException
                String status = (row[0] instanceof String) ? (String) row[0] : "UNKNOWN";
                Long cnt = (row[1] instanceof Number) ? ((Number) row[1]).longValue() : 0L;
                byStatus.put(status == null ? "UNKNOWN" : status, cnt);
            }
        } catch (Exception e) {
            System.err.println("[ReportService] Error fetching status counts: " + e.getMessage());
            e.printStackTrace(); // In stack trace để debug
        }
        dto.setByStatus(byStatus);
        System.out.println("[ReportService] By status: " + byStatus); // Thêm log


        List<ReportSummaryDTO.SeriesPoint> series = new ArrayList<>();
        try { // Thêm try-catch
            List<Object[]> revenueDaily = donRepo.revenueByDay(from, to);
            System.out.println("[ReportService] Revenue by day fetched: " + revenueDaily.size() + " rows"); // Thêm log
            for (Object[] row : revenueDaily) {
                // --- Sửa cách lấy ngày ---
                LocalDate day;
                if (row[0] instanceof java.sql.Date) {
                    day = ((java.sql.Date) row[0]).toLocalDate(); // Chuyển từ sql.Date sang LocalDate
                } else if (row[0] instanceof LocalDate) {
                    day = (LocalDate) row[0];
                } else if (row[0] != null) { // Thêm kiểm tra null
                    System.err.println("[ReportService] Unexpected date type from revenueByDay: " + row[0].getClass().getName() + " with value: " + row[0]);
                    continue; // Bỏ qua nếu kiểu không đúng
                } else {
                    System.err.println("[ReportService] Date value from revenueByDay is NULL.");
                    continue; // Bỏ qua nếu null
                }
                // --- Kết thúc sửa ---
                // Kiểm tra kiểu dữ liệu value
                Double val = (row[1] instanceof Number) ? ((Number) row[1]).doubleValue() : 0.0;
                series.add(new ReportSummaryDTO.SeriesPoint(day.toString(), val)); // Dùng toString() của LocalDate
            }
        } catch (Exception e) {
            System.err.println("[ReportService] Error fetching revenue by day: " + e.getMessage());
            e.printStackTrace();
        }
        dto.setRevenueSeries(series);
        System.out.println("[ReportService] Revenue series generated: " + series.size() + " points"); // Thêm log

        dto.setTopBooks(Collections.emptyList());
        System.out.println("[ReportService] summary finished."); // Thêm log
        return dto;
    }

    // === THAY ĐỔI KIỂU THAM SỐ ===
    public String exportCsv(LocalDate from, LocalDate to){
        // === KẾT THÚC THAY ĐỔI ===
        System.out.println("[ReportService] exportCsv called with dates: " + from + " to " + to); // Thêm log
        StringBuilder sb = new StringBuilder();
        sb.append("Ngay,DoanhThu\n"); // Header CSV
        try { // Thêm try-catch
            for (Object[] row : donRepo.revenueByDay(from, to)) { // Truyền LocalDate
                // --- Sửa cách lấy ngày ---
                LocalDate day;
                if (row[0] instanceof java.sql.Date) {
                    day = ((java.sql.Date) row[0]).toLocalDate();
                } else if (row[0] instanceof LocalDate) {
                    day = (LocalDate) row[0];
                } else if (row[0] != null) {
                    System.err.println("[ReportService] Unexpected date type in exportCsv: " + row[0].getClass().getName() + " with value: " + row[0]);
                    continue;
                } else {
                    System.err.println("[ReportService] Date value in exportCsv is NULL.");
                    continue;
                }
                // --- Kết thúc sửa ---
                // Kiểm tra value
                Double val = (row[1] instanceof Number) ? ((Number) row[1]).doubleValue() : 0.0;
                // Định dạng số trong CSV (ví dụ: dùng dấu chấm thập phân)
                sb.append(day).append(",").append(String.format(Locale.ROOT, "%.2f", val)).append("\n"); // Dùng Locale.ROOT cho nhất quán
            }
        } catch (Exception e) {
            System.err.println("[ReportService] Error fetching data for exportCsv: " + e.getMessage());
            e.printStackTrace();
            sb.append("ERROR,").append("\"").append(e.getMessage().replace("\"", "\"\"")).append("\"\n"); // Báo lỗi trong CSV (escape dấu ngoặc kép)
        }
        return sb.toString();
    }

    // === THAY ĐỔI KIỂU THAM SỐ ===
    public ByteArrayOutputStream exportPdf(LocalDate from, LocalDate to) throws Exception{
        // === KẾT THÚC THAY ĐỔI ===
        System.out.println("[ReportService] exportPdf called with dates: " + from + " to " + to); // Thêm log
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        com.lowagie.text.Document doc = new com.lowagie.text.Document(); // Dùng tên class đầy đủ
        try { // Bọc trong try-finally để đảm bảo doc.close()
            com.lowagie.text.pdf.PdfWriter.getInstance(doc, out);
            doc.open();

            // Lấy dữ liệu (truyền LocalDate)
            Double rev = donRepo.sumRevenue(from, to);
            long orders = Optional.ofNullable(donRepo.countOrders(from, to)).orElse(0L);

            // TODO: Thêm Font tiếng Việt vào đây nếu cần hiển thị tiếng Việt có dấu trong PDF
            // Ví dụ: Tạo font
            // BaseFont bf = BaseFont.createFont("path/to/your/font/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            // com.lowagie.text.Font vietnameseFont = new com.lowagie.text.Font(bf, 12);

            doc.add(new com.lowagie.text.Paragraph("BÁO CÁO DOANH THU")); // , vietnameseFont));
            doc.add(new com.lowagie.text.Paragraph("Khoảng thời gian: " + from + " --> " + to)); // , vietnameseFont));
            doc.add(new com.lowagie.text.Paragraph("Tổng doanh thu: " + String.format(Locale.GERMAN, "%,.0f đ", (rev == null ? 0.0 : rev)))); // Định dạng tiền tệ
            doc.add(new com.lowagie.text.Paragraph("Tổng số đơn: " + orders)); // , vietnameseFont));
            doc.add(new com.lowagie.text.Paragraph(" ")); // Dòng trống

            // Tạo bảng
            com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(2);
            table.setWidthPercentage(100);
            // Thêm header cho bảng (nên dùng Font tiếng Việt nếu đã tạo)
            com.lowagie.text.pdf.PdfPCell headerCell1 = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Paragraph("Ngày")); // , vietnameseFont));
            com.lowagie.text.pdf.PdfPCell headerCell2 = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Paragraph("Doanh thu")); // , vietnameseFont));
            headerCell1.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
            headerCell2.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
            headerCell2.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
            table.addCell(headerCell1);
            table.addCell(headerCell2);

            // Thêm dữ liệu vào bảng
            List<Object[]> revenueDaily = donRepo.revenueByDay(from, to);
            if (revenueDaily.isEmpty()) {
                com.lowagie.text.pdf.PdfPCell emptyCell = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Paragraph("Không có dữ liệu")); // , vietnameseFont));
                emptyCell.setColspan(2);
                emptyCell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                table.addCell(emptyCell);
            } else {
                for (Object[] row : revenueDaily) {
                    // --- Sửa cách lấy ngày ---
                    LocalDate day;
                    if (row[0] instanceof java.sql.Date) {
                        day = ((java.sql.Date) row[0]).toLocalDate();
                    } else if (row[0] instanceof LocalDate) {
                        day = (LocalDate) row[0];
                    } else if (row[0] != null) {
                        System.err.println("[ReportService] Unexpected date type in exportPdf: " + row[0].getClass().getName() + " with value: " + row[0]);
                        continue;
                    } else {
                        System.err.println("[ReportService] Date value in exportPdf is NULL.");
                        continue;
                    }
                    // --- Kết thúc sửa ---
                    // Kiểm tra value
                    Double val = (row[1] instanceof Number) ? ((Number) row[1]).doubleValue() : 0.0;

                    table.addCell(day.toString()); // , vietnameseFont)); // Thêm font nếu cần
                    com.lowagie.text.pdf.PdfPCell valueCell = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Paragraph(String.format(Locale.GERMAN, "%,.0f đ", val))); // , vietnameseFont));
                    valueCell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
                    table.addCell(valueCell);
                }
            }
            doc.add(table);

        } catch (Exception e) {
            System.err.println("[ReportService] Error creating PDF: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            if (doc != null && doc.isOpen()) {
                doc.close(); // Luôn đóng document
            }
        }
        return out;
    }
}

