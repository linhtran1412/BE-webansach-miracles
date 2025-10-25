package com.example.wedbansach_bakend1.controller;

import com.example.wedbansach_bakend1.dao.HinhAnhRepository;
import com.example.wedbansach_bakend1.dao.SachRepository;
import com.example.wedbansach_bakend1.entity.HinhAnh;
import com.example.wedbansach_bakend1.entity.Sach;
import com.fasterxml.jackson.databind.ObjectMapper; // <<< THÊM IMPORT
import jakarta.persistence.EntityNotFoundException; // <<< THÊM IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // <<< THÊM IMPORT
import org.springframework.transaction.annotation.Transactional; // <<< THÊM IMPORT
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException; // <<< THÊM IMPORT
import java.util.Base64;
import java.util.List; // <<< THÊM IMPORT
import java.util.Optional; // <<< THÊM IMPORT

@RestController
@RequestMapping("/api/admin/sach") // Đường dẫn chung cho controller này
public class SachAdminController {

    @Autowired
    private SachRepository sachRepository;

    @Autowired
    private HinhAnhRepository hinhAnhRepository;

    @Autowired
    private ObjectMapper objectMapper; // Dùng để parse JSON string

    // API THÊM SÁCH MỚI (kèm ảnh nếu có)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin', 'STAFF')") // Bảo mật bằng annotation
    @Transactional // Đảm bảo tất cả lưu hoặc không lưu gì cả
    public ResponseEntity<?> createSach(@RequestPart("sach") String sachJson,
                                        @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            // 1. Parse thông tin sách từ JSON string
            Sach sach = objectMapper.readValue(sachJson, Sach.class);
            sach.setMaSach(0); // Reset ID để tạo mới

            // 2. Lưu sách vào DB để lấy ID
            Sach savedSach = sachRepository.save(sach);

            // 3. Xử lý ảnh nếu có file được gửi lên
            if (file != null && !file.isEmpty()) {
                saveOrUpdateImage(savedSach, file, true); // true: là icon (ảnh đại diện)
            }

            // 4. Trả về sách đã lưu
            Sach sachWithImage = sachRepository.findById(savedSach.getMaSach()).orElse(savedSach);
            return ResponseEntity.status(HttpStatus.CREATED).body(sachWithImage);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Lỗi parse dữ liệu hoặc xử lý ảnh: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi máy chủ: " + e.getMessage());
        }
    }

    // API CẬP NHẬT SÁCH (kèm ảnh nếu có)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin', 'STAFF')")
    @Transactional
    public ResponseEntity<?> updateSach(@PathVariable int id,
                                        @RequestPart("sach") String sachJson,
                                        @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            // 1. Tìm sách hiện có
            Sach existingSach = sachRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sách ID: " + id));

            // 2. Parse thông tin sách mới
            Sach updatedSachData = objectMapper.readValue(sachJson, Sach.class);

            // 3. Cập nhật các trường
            existingSach.setTenSach(updatedSachData.getTenSach());
            existingSach.setTenTacGia(updatedSachData.getTenTacGia());
            existingSach.setISBN(updatedSachData.getISBN());
            existingSach.setMoTa(updatedSachData.getMoTa());
            existingSach.setGiaNiemYet(updatedSachData.getGiaNiemYet());
            existingSach.setGiaBan(updatedSachData.getGiaBan());
            existingSach.setSoLuong(updatedSachData.getSoLuong());

            // 4. Lưu lại sách
            Sach savedSach = sachRepository.save(existingSach);

            // 5. Xử lý ảnh nếu có file mới
            if (file != null && !file.isEmpty()) {
                deleteExistingImages(savedSach); // Xóa ảnh cũ
                saveOrUpdateImage(savedSach, file, true); // Lưu ảnh mới là icon
            }

            // 6. Trả về sách đã cập nhật
            Sach sachWithImage = sachRepository.findById(savedSach.getMaSach()).orElse(savedSach);
            return ResponseEntity.ok(sachWithImage);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Lỗi parse dữ liệu hoặc xử lý ảnh: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi máy chủ: " + e.getMessage());
        }
    }

    // --- Hàm tiện ích để lưu/cập nhật ảnh ---
    private void saveOrUpdateImage(Sach sach, MultipartFile file, boolean isIcon) throws IOException {
        String base64 = Base64.getEncoder().encodeToString(file.getBytes());
        HinhAnh img = new HinhAnh();
        img.setSach(sach);
        img.setTenHinhAnh(file.getOriginalFilename());
        img.setLaIcon(isIcon);
        img.setDuLieuAnh("data:" + file.getContentType() + ";base64," + base64);
        hinhAnhRepository.save(img);
    }

    // --- Hàm tiện ích để xóa ảnh cũ ---
    private void deleteExistingImages(Sach sach) {
        List<HinhAnh> imagesToDelete = hinhAnhRepository.findBySach_MaSach(sach.getMaSach());
        if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
            hinhAnhRepository.deleteAll(imagesToDelete);
        }
    }
}