package com.example.webbansach_backend.controller;

import com.example.webbansach_backend.entity.HinhAnh;
import com.example.webbansach_backend.entity.Sach;
import com.example.webbansach_backend.dao.HinhAnhRepository;
import com.example.webbansach_backend.dao.SachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

@RestController
@RequestMapping("/api/admin/images")
public class ImageAdminController {
    @Autowired private SachRepository sachRepo;
    @Autowired private HinhAnhRepository imgRepo;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
                                    @RequestParam("sachId") int sachId,
                                    @RequestParam(value="laIcon", defaultValue="false") boolean laIcon) {
        try {
            Sach sach = sachRepo.findById(sachId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sách: " + sachId));

            String base64 = Base64.getEncoder().encodeToString(file.getBytes());
            HinhAnh img = new HinhAnh();
            img.setSach(sach);
            img.setTenHinhAnh(file.getOriginalFilename());
            img.setLaIcon(laIcon);
            img.setDuLieuAnh("data:" + file.getContentType() + ";base64," + base64);
            imgRepo.save(img);
            return ResponseEntity.ok(img);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
