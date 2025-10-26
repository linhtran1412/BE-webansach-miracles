package com.example.webbansach_backend.controller;

import com.example.webbansach_backend.dao.DonHangRepository;
import com.example.webbansach_backend.entity.DonHang;
import com.example.webbansach_backend.entity.ThongBao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/don-hang")
public class AdminDonHangController {

    @Autowired
    private DonHangRepository donHangRepository;

//    // Lấy tất cả đơn (Admin/Staff)
//    @GetMapping
//    public ResponseEntity<List<DonHang>> getAllDonHang() {
//        List<DonHang> list = donHangRepository.findAll(
//                org.springframework.data.domain.Sort.by("maDonHang").descending()
//        );
//        System.out.println(">>> ADMIN /don-hang size = " + list.size());
//        return ResponseEntity.ok(list);
//    }

    @GetMapping
    public ResponseEntity<?> getAllDonHang() {
        var list = donHangRepository.findAll(
                org.springframework.data.domain.Sort.by("maDonHang").descending()
        );
        System.out.println(">>> ADMIN /don-hang size = " + list.size());
        var out = list.stream()
                .map(com.example.webbansach_backend.dto.AdminDonHangListDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(out);
    }


    // Lấy 1 đơn theo id (Admin/Staff)
    @GetMapping("/{id}")
    public ResponseEntity<?> getDonHangById(@PathVariable Integer id) {
        Optional<DonHang> donHangOpt = donHangRepository.findById(id);
        return donHangOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ThongBao("Không tìm thấy đơn hàng ID: " + id)));
    }

    // Tạo đơn (nếu cần dùng nội bộ)
    @PostMapping
    public ResponseEntity<?> createDonHang(@RequestBody DonHang donHang) {
        try {
            return new ResponseEntity<>(donHangRepository.save(donHang), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ThongBao("Lỗi khi tạo đơn hàng: " + e.getMessage()));
        }
    }

    // Cập nhật trạng thái (Admin/Staff)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDonHang(@PathVariable Integer id, @RequestBody DonHang donHangMoi) {
        Optional<DonHang> donHangOpt = donHangRepository.findById(id);
        if (donHangOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ThongBao("Không tìm thấy đơn hàng ID: " + id + " để cập nhật."));
        }
        if (donHangMoi.getTrangThai() == null) {
            return ResponseEntity.badRequest()
                    .body(new ThongBao("Vui lòng cung cấp 'trangThai' mới trong body request."));
        }
        try {
            DonHang donHang = donHangOpt.get();
            donHang.setTrangThai(donHangMoi.getTrangThai());
            return ResponseEntity.ok(donHangRepository.save(donHang));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ThongBao("Lỗi khi cập nhật đơn hàng: " + e.getMessage()));
        }
    }

    // Xoá đơn (Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDonHang(@PathVariable Integer id) {
        if (!donHangRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ThongBao("Không tìm thấy đơn hàng ID: " + id + " để xóa."));
        }
        try {
            donHangRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ThongBao("Lỗi hệ thống khi xóa đơn hàng ID: " + id));
        }
    }
}
