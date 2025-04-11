package com.example.wedbansach_bakend1.controller;

// === THÊM ĐẦY ĐỦ CÁC IMPORT CẦN THIẾT ===
import com.example.wedbansach_bakend1.Service.CheckoutService;
import com.example.wedbansach_bakend1.dto.CheckoutRequestDTO;
import com.example.wedbansach_bakend1.dto.DonHangDTO; // Import DonHangDTO
import com.example.wedbansach_bakend1.entity.ThongBao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin; // Import CrossOrigin
import org.springframework.web.bind.annotation.PostMapping; // Import PostMapping
import org.springframework.web.bind.annotation.RequestBody; // Import RequestBody
import org.springframework.web.bind.annotation.RequestMapping; // Import RequestMapping
import org.springframework.web.bind.annotation.RestController; // Import RestController
// === KẾT THÚC THÊM IMPORT ===


@RestController // Thêm lại annotation này
@RequestMapping("/api/checkout") // Giữ nguyên RequestMapping
// @CrossOrigin("*") // Bỏ CrossOrigin ở đây vì đã có Global Config trong WebConfig.java
public class CheckoutController {

    @Autowired // Giữ nguyên Autowired
    private CheckoutService checkoutService;

    @PostMapping("/place-order") // Giữ nguyên PostMapping
    // === SỬA KIỂU TRẢ VỀ TRONG ResponseEntity<?> THÀNH DonHangDTO ===
    public ResponseEntity<?> placeOrder(@RequestBody CheckoutRequestDTO checkoutRequestDto) { // Giữ nguyên RequestBody
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao("Vui lòng đăng nhập để đặt hàng."));
        }

        try {
            // === SỬA KIỂU DỮ LIỆU NHẬN TỪ SERVICE ===
            // Gọi service giờ trả về DonHangDTO
            DonHangDTO donHangDTO = checkoutService.placeOrder(checkoutRequestDto, username);
            // === KẾT THÚC SỬA ===

            // === TRẢ VỀ DTO THAY VÌ ENTITY ===
            return ResponseEntity.ok(donHangDTO);
            // === KẾT THÚC SỬA ===

        } catch (RuntimeException e) {
            // Giữ nguyên xử lý lỗi RuntimeException
            return ResponseEntity.badRequest().body(new ThongBao(e.getMessage()));
        } catch (Exception e) {
            // Giữ nguyên xử lý lỗi Exception chung
            e.printStackTrace(); // Log lỗi ra console của server
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Đã xảy ra lỗi không mong muốn khi đặt hàng."));
        }
    }
}