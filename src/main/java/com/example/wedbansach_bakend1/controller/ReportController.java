//package com.example.wedbansach_bakend1.controller;
//
//import com.example.wedbansach_bakend1.dto.ReportSummaryDTO;
//import com.example.wedbansach_bakend1.Service.ReportService;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.*;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.ByteArrayOutputStream;
//import java.sql.Date;
//
//@RestController
//@RequestMapping("/api/admin/reports")
//// @PreAuthorize("hasAuthority('admin')")
//public class ReportController {
//
//    private final ReportService reportService;
//    public ReportController(ReportService reportService) {
//        this.reportService = reportService;
//    }
//
//    @GetMapping("/summary")
//    public ReportSummaryDTO summary(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to
//    ){
//        return reportService.summary(from, to);
//    }
//
//    @GetMapping(value="/export.csv", produces="text/csv; charset=UTF-8")
//    public ResponseEntity<String> exportCsv(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to
//    ){
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.csv")
//                .body(reportService.exportCsv(from, to));
//    }
//
//    @GetMapping(value="/export.pdf", produces="application/pdf")
//    public ResponseEntity<byte[]> exportPdf(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to
//    ) throws Exception {
//        ByteArrayOutputStream out = reportService.exportPdf(from, to);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
//                .body(out.toByteArray());
//    }
//}

package com.example.wedbansach_bakend1.controller;

import com.example.wedbansach_bakend1.dto.ReportSummaryDTO;
import com.example.wedbansach_bakend1.Service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
// import org.springframework.security.access.prepost.PreAuthorize; // <<< VẪN COMMENT DÒNG NÀY
import org.springframework.security.core.Authentication; // <<< THÊM IMPORT NÀY
import org.springframework.security.core.context.SecurityContextHolder; // <<< THÊM IMPORT NÀY
import org.springframework.web.bind.annotation.*;
import com.example.wedbansach_bakend1.entity.ThongBao;
import java.io.ByteArrayOutputStream;
// import java.sql.Date; // <<< BỎ IMPORT NÀY
import java.time.LocalDate; // <<< THÊM IMPORT NÀY

@RestController
@RequestMapping("/api/admin/reports")
// @PreAuthorize("hasAuthority('admin')") // <<< VẪN COMMENT DÒNG NÀY
public class ReportController {

    private final ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/summary")
    public ResponseEntity<?> summary( // <<< Sửa kiểu trả về thành ResponseEntity<?>
                                      // === THAY ĐỔI KIỂU DỮ LIỆU Ở ĐÂY ===
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
                                      // === KẾT THÚC THAY ĐỔI ===
    ){
        // === THÊM LOG ĐỂ XEM AUTHENTICATION MÀ CONTROLLER NHẬN ĐƯỢC ===
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("[ReportController] === Received request for /summary ==="); // Dấu hiệu bắt đầu log cho request này
        if (authentication != null && authentication.isAuthenticated()) { // Kiểm tra authenticated
            System.out.println("[ReportController] Authentication details: ");
            System.out.println("  Principal: " + authentication.getName()); // Lấy tên user
            System.out.println("  Authorities: " + authentication.getAuthorities()); // In quyền
            System.out.println("  Is Authenticated: true");

            // Kiểm tra quyền 'admin' một cách tường minh
            boolean hasAdminAuthority = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equalsIgnoreCase("admin")); // Dùng equalsIgnoreCase cho chắc
            System.out.println("[ReportController] Checking for 'admin' authority (case-insensitive): " + hasAdminAuthority);

            if (!hasAdminAuthority) {
                System.err.println("[ReportController] ACCESS DENIED! User does NOT have 'admin' authority.");
                // Trả về lỗi 403 ngay tại đây để debug
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ThongBao("Access Denied - Missing 'admin' authority at Controller level check.")); // Sử dụng ThongBao DTO
            } else {
                System.out.println("[ReportController] User HAS 'admin' authority. Proceeding...");
            }

        } else {
            System.err.println("[ReportController] Authentication is NULL or NOT Authenticated!");
            // Trả về lỗi 401 nếu không có authentication
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao("Authentication required.")); // Sử dụng ThongBao DTO
        }
        // === KẾT THÚC THÊM LOG ===

        // --- Gọi Service ---
        try {
            System.out.println("[ReportController] Calling reportService.summary with LocalDate..."); // Sửa log
            // Truyền LocalDate vào service
            ReportSummaryDTO dto = reportService.summary(from, to);
            System.out.println("[ReportController] reportService.summary finished successfully.");
            return ResponseEntity.ok(dto); // Trả về 200 OK nếu thành công
        } catch (Exception e) {
            System.err.println("[ReportController] Error calling reportService.summary: " + e.getMessage());
            e.printStackTrace(); // In stack trace của lỗi service
            // Trả về lỗi 500 nếu service có vấn đề
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi xử lý báo cáo: " + e.getMessage())); // Sử dụng ThongBao DTO
        } finally {
            System.out.println("[ReportController] === Finished processing request for /summary ==="); // Dấu hiệu kết thúc log
        }
    }

    // === SỬA TƯƠNG TỰ CHO exportCsv ===
    @GetMapping(value="/export.csv", produces="text/csv; charset=UTF-8")
    public ResponseEntity<?> exportCsv( // Sửa kiểu trả về
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ){
        // Thêm log kiểm tra Authentication tương tự hàm summary() nếu cần
        System.out.println("[ReportController] Received request for /export.csv");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !authentication.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equalsIgnoreCase("admin"))) {
            System.err.println("[ReportController] ACCESS DENIED for /export.csv!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied."); // Trả về text lỗi
        }
        try {
            String csvData = reportService.exportCsv(from, to); // Truyền LocalDate
            System.out.println("[ReportController] exportCsv finished successfully.");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.csv")
                    .body(csvData);
        } catch (Exception e) {
            System.err.println("[ReportController] Error calling reportService.exportCsv: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xuất CSV: " + e.getMessage()); // Trả về lỗi rõ ràng hơn
        }
    }

    // === SỬA TƯƠNG TỰ CHO exportPdf ===
    @GetMapping(value="/export.pdf", produces=MediaType.APPLICATION_PDF_VALUE) // Sửa produces
    public ResponseEntity<?> exportPdf( // Sửa kiểu trả về
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        System.out.println("[ReportController] Received request for /export.pdf");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !authentication.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equalsIgnoreCase("admin"))) {
            System.err.println("[ReportController] ACCESS DENIED for /export.pdf!");
            // Trả về lỗi 403, có thể kèm theo header text/plain nếu muốn
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.TEXT_PLAIN).body("Access Denied.");
        }
        try {
            ByteArrayOutputStream out = reportService.exportPdf(from, to); // Truyền LocalDate
            System.out.println("[ReportController] exportPdf finished successfully.");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());
        } catch (Exception e) {
            System.err.println("[ReportController] Error calling reportService.exportPdf: " + e.getMessage());
            e.printStackTrace();
            // Trả về lỗi 500 với thông báo lỗi dạng text/plain
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN) // Đổi content type
                    .body("Lỗi khi xuất PDF: " + e.getMessage()); // Trả về text lỗi
        }
    }
}
