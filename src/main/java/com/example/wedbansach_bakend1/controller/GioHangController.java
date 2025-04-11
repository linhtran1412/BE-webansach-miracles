package com.example.wedbansach_bakend1.controller;

import com.example.wedbansach_bakend1.Service.GioHangService;
import com.example.wedbansach_bakend1.dto.GioHangDTO;
import com.example.wedbansach_bakend1.entity.ThongBao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gio-hang")
//@CrossOrigin(origins = "http://localhost:3000") // Cho phép frontend truy cập
public class GioHangController {

    @Autowired
    private GioHangService gioHangService;

    // Endpoint lấy giỏ hàng của người dùng hiện tại
    @GetMapping
    public ResponseEntity<?> getGioHang() {
        try {
            GioHangDTO gioHangDTO = gioHangService.getGioHangDTOByCurrentUser();
            return ResponseEntity.ok(gioHangDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint thêm sách vào giỏ
    @PostMapping("/them-sach")
    public ResponseEntity<?> themSachVaoGio(@RequestParam int maSach, @RequestParam int soLuong) {
        if (soLuong <= 0) {
            return ResponseEntity.badRequest().body("Số lượng phải lớn hơn 0.");
        }
        try {
            GioHangDTO gioHangDTO = gioHangService.themSachVaoGio(maSach, soLuong);
            return ResponseEntity.ok(gioHangDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint cập nhật số lượng sách
    @PutMapping("/cap-nhat-so-luong")
    public ResponseEntity<?> capNhatSoLuong(@RequestParam int maSach, @RequestParam int soLuongMoi) {
        try {
            GioHangDTO gioHangDTO = gioHangService.capNhatSoLuong(maSach, soLuongMoi);
            return ResponseEntity.ok(gioHangDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


//    // Endpoint xóa sách khỏi giỏ
//    @DeleteMapping("/xoa-sach")
//    public ResponseEntity<?> xoaSachKhoiGio(@RequestParam int maSach) {
//        try {
//            GioHangDTO gioHangDTO = gioHangService.xoaSachKhoiGio(maSach);
//            return ResponseEntity.ok(gioHangDTO);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//}

    @DeleteMapping("/xoa-sach")
    public ResponseEntity<?> xoaSachKhoiGio(@RequestParam int maSach) {
        try {
            // Gọi hàm service mới không trả về DTO
            gioHangService.xoaSachKhoiGioVaKhongTraVeDTO(maSach);
            // Trả về 204 No Content khi xóa thành công
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Trả về lỗi 400 hoặc 404 tùy theo Exception
            if (e.getMessage().contains("không có trong giỏ hàng") || e.getMessage().contains("không tồn tại")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao(e.getMessage()));
            }
            return ResponseEntity.badRequest().body(new ThongBao(e.getMessage()));
        }
    }
}