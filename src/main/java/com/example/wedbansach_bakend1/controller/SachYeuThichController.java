package com.example.wedbansach_bakend1.controller;

import com.example.wedbansach_bakend1.Service.SachYeuThichService;
import com.example.wedbansach_bakend1.entity.Sach;
import com.example.wedbansach_bakend1.entity.SachYeuThich;
import com.example.wedbansach_bakend1.entity.ThongBao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // Import Map

@RestController
@RequestMapping("/api/yeu-thich") // Prefix chung cho API yêu thích
public class SachYeuThichController {

    @Autowired
    private SachYeuThichService sachYeuThichService;

    // Lấy danh sách Sách yêu thích của người dùng hiện tại
    @GetMapping
    public ResponseEntity<?> getMyWishlist() {
        try {
            List<Sach> wishlist = sachYeuThichService.getWishlist();
            return ResponseEntity.ok(wishlist);
        } catch (RuntimeException e) {
            // Thường là lỗi chưa đăng nhập
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi lấy danh sách yêu thích: " + e.getMessage()));
        }
    }

    // Thêm sách vào yêu thích
    @PostMapping("/them/{maSach}")
    public ResponseEntity<?> addToWishlist(@PathVariable int maSach) {
        try {
            SachYeuThich newItem = sachYeuThichService.addToWishlist(maSach);
            // Trả về đối tượng SachYeuThich vừa tạo hoặc chỉ cần thông báo thành công
            return ResponseEntity.status(HttpStatus.CREATED).body(new ThongBao("Đã thêm vào yêu thích thành công!"));
        } catch (RuntimeException e) {
            // Có thể là lỗi chưa đăng nhập, sách không tồn tại, hoặc đã tồn tại
            if (e.getMessage().contains("chưa đăng nhập")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
            }
            if (e.getMessage().contains("Sách không tồn tại")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao(e.getMessage()));
            }
            if (e.getMessage().contains("đã có trong danh sách")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ThongBao(e.getMessage())); // 409 Conflict
            }
            return ResponseEntity.badRequest().body(new ThongBao(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi thêm vào yêu thích: " + e.getMessage()));
        }
    }

    // Xóa sách khỏi yêu thích
    @DeleteMapping("/xoa/{maSach}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable int maSach) {
        try {
            sachYeuThichService.removeFromWishlist(maSach);
            return ResponseEntity.ok(new ThongBao("Đã xóa khỏi yêu thích thành công!")); // Hoặc noContent()
            // return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            // Có thể là lỗi chưa đăng nhập hoặc sách không có trong danh sách
            if (e.getMessage().contains("chưa đăng nhập")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
            }
            if (e.getMessage().contains("không có trong danh sách")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao(e.getMessage()));
            }
            return ResponseEntity.badRequest().body(new ThongBao(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi xóa khỏi yêu thích: " + e.getMessage()));
        }
    }

    // Kiểm tra sách có trong yêu thích không
    @GetMapping("/kiem-tra/{maSach}")
    public ResponseEntity<?> isInWishlist(@PathVariable int maSach) {
        try {
            boolean isInList = sachYeuThichService.isInWishlist(maSach);
            // Trả về đối tượng JSON { "isInWishlist": true/false }
            return ResponseEntity.ok(Map.of("isInWishlist", isInList));
        } catch (Exception e) {
            // Lỗi không mong muốn
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi kiểm tra danh sách yêu thích: " + e.getMessage()));
        }
    }
}