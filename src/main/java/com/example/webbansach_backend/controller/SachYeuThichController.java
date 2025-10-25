//package com.example.wedbansach_bakend1.controller;
//
//import com.example.wedbansach_bakend1.Service.SachYeuThichService;
//import com.example.wedbansach_bakend1.entity.Sach;
//import com.example.wedbansach_bakend1.entity.SachYeuThich;
//import com.example.wedbansach_bakend1.entity.ThongBao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map; // Import Map
//
//@RestController
//@RequestMapping("/api/yeu-thich") // Prefix chung cho API yêu thích
//public class SachYeuThichController {
//
//    @Autowired
//    private SachYeuThichService sachYeuThichService;
//
//    // Lấy danh sách Sách yêu thích của người dùng hiện tại
//    @GetMapping
//    public ResponseEntity<?> getMyWishlist() {
//        try {
//            List<Sach> wishlist = sachYeuThichService.getWishlist();
//            return ResponseEntity.ok(wishlist);
//        } catch (RuntimeException e) {
//            // Thường là lỗi chưa đăng nhập
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi lấy danh sách yêu thích: " + e.getMessage()));
//        }
//    }
//
//    // Thêm sách vào yêu thích
//    @PostMapping("/them/{maSach}")
//    public ResponseEntity<?> addToWishlist(@PathVariable int maSach) {
//        try {
//            SachYeuThich newItem = sachYeuThichService.addToWishlist(maSach);
//            // Trả về đối tượng SachYeuThich vừa tạo hoặc chỉ cần thông báo thành công
//            return ResponseEntity.status(HttpStatus.CREATED).body(new ThongBao("Đã thêm vào yêu thích thành công!"));
//        } catch (RuntimeException e) {
//            // Có thể là lỗi chưa đăng nhập, sách không tồn tại, hoặc đã tồn tại
//            if (e.getMessage().contains("chưa đăng nhập")) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
//            }
//            if (e.getMessage().contains("Sách không tồn tại")) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao(e.getMessage()));
//            }
//            if (e.getMessage().contains("đã có trong danh sách")) {
//                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ThongBao(e.getMessage())); // 409 Conflict
//            }
//            return ResponseEntity.badRequest().body(new ThongBao(e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi thêm vào yêu thích: " + e.getMessage()));
//        }
//    }
//
//    // Xóa sách khỏi yêu thích
//    @DeleteMapping("/xoa/{maSach}")
//    public ResponseEntity<?> removeFromWishlist(@PathVariable int maSach) {
//        try {
//            sachYeuThichService.removeFromWishlist(maSach);
//            return ResponseEntity.ok(new ThongBao("Đã xóa khỏi yêu thích thành công!")); // Hoặc noContent()
//            // return ResponseEntity.noContent().build(); // 204 No Content
//        } catch (RuntimeException e) {
//            // Có thể là lỗi chưa đăng nhập hoặc sách không có trong danh sách
//            if (e.getMessage().contains("chưa đăng nhập")) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
//            }
//            if (e.getMessage().contains("không có trong danh sách")) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao(e.getMessage()));
//            }
//            return ResponseEntity.badRequest().body(new ThongBao(e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi xóa khỏi yêu thích: " + e.getMessage()));
//        }
//    }
//
//    // Kiểm tra sách có trong yêu thích không
//    @GetMapping("/kiem-tra/{maSach}")
//    public ResponseEntity<?> isInWishlist(@PathVariable int maSach) {
//        try {
//            boolean isInList = sachYeuThichService.isInWishlist(maSach);
//            // Trả về đối tượng JSON { "isInWishlist": true/false }
//            return ResponseEntity.ok(Map.of("isInWishlist", isInList));
//        } catch (Exception e) {
//            // Lỗi không mong muốn
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi kiểm tra danh sách yêu thích: " + e.getMessage()));
//        }
//    }
//}


//package com.example.wedbansach_bakend1.controller;
//
//import com.example.wedbansach_bakend1.Service.SachYeuThichService;
//import com.example.wedbansach_bakend1.dto.SachSimpleDTO;
//import com.example.wedbansach_bakend1.entity.Sach;
//import com.example.wedbansach_bakend1.entity.SachYeuThich;
//import com.example.wedbansach_bakend1.entity.ThongBao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType; // <<< PHẢI CÓ IMPORT NÀY
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/yeu-thich") // Prefix chung cho API yêu thích
//public class SachYeuThichController {
//
//    @Autowired
//    private SachYeuThichService sachYeuThichService;
//
//    // Lấy danh sách Sách yêu thích của người dùng hiện tại
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // <<< THÊM produces Ở ĐÂY
//    public ResponseEntity<?> getMyWishlist() {
//        try {
//            List<SachSimpleDTO> wishlist = sachYeuThichService.getWishlist();
//            System.out.println("[SachYeuThichController] getMyWishlist successful, returning " + wishlist.size() + " items."); // Thêm log thành công
//            return ResponseEntity.ok(wishlist);
//        } catch (RuntimeException e) {
//            System.err.println("[SachYeuThichController] getMyWishlist RuntimeException: " + e.getMessage()); // Log lỗi Runtime
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
//        } catch (Exception e) {
//            System.err.println("[SachYeuThichController] getMyWishlist Exception: " + e.getMessage()); // Log lỗi khác
//            e.printStackTrace(); // In stack trace để debug sâu hơn nếu cần
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi lấy danh sách yêu thích: " + e.getMessage()));
//        }
//    }
//
//    // Thêm sách vào yêu thích
//    @PostMapping("/them/{maSach}")
//    public ResponseEntity<?> addToWishlist(@PathVariable int maSach) {
//        try {
//            SachYeuThich newItem = sachYeuThichService.addToWishlist(maSach);
//            System.out.println("[SachYeuThichController] addToWishlist successful for sachId: " + maSach); // Log thành công
//            return ResponseEntity.status(HttpStatus.CREATED).body(new ThongBao("Đã thêm vào yêu thích thành công!"));
//        } catch (RuntimeException e) {
//            System.err.println("[SachYeuThichController] addToWishlist RuntimeException for sachId " + maSach + ": " + e.getMessage()); // Log lỗi Runtime
//            if (e.getMessage().contains("chưa đăng nhập")) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
//            }
//            if (e.getMessage().contains("Sách không tồn tại")) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao(e.getMessage()));
//            }
//            if (e.getMessage().contains("đã có trong danh sách")) {
//                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ThongBao(e.getMessage()));
//            }
//            return ResponseEntity.badRequest().body(new ThongBao(e.getMessage()));
//        } catch (Exception e) {
//            System.err.println("[SachYeuThichController] addToWishlist Exception for sachId " + maSach + ": " + e.getMessage()); // Log lỗi khác
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi thêm vào yêu thích: " + e.getMessage()));
//        }
//    }
//
//    // Xóa sách khỏi yêu thích
//    @DeleteMapping("/xoa/{maSach}")
//    public ResponseEntity<?> removeFromWishlist(@PathVariable int maSach) {
//        try {
//            sachYeuThichService.removeFromWishlist(maSach);
//            System.out.println("[SachYeuThichController] removeFromWishlist successful for sachId: " + maSach); // Log thành công
//            return ResponseEntity.ok(new ThongBao("Đã xóa khỏi yêu thích thành công!"));
//        } catch (RuntimeException e) {
//            System.err.println("[SachYeuThichController] removeFromWishlist RuntimeException for sachId " + maSach + ": " + e.getMessage()); // Log lỗi Runtime
//            if (e.getMessage().contains("chưa đăng nhập")) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
//            }
//            if (e.getMessage().contains("không có trong danh sách")) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao(e.getMessage()));
//            }
//            return ResponseEntity.badRequest().body(new ThongBao(e.getMessage()));
//        } catch (Exception e) {
//            System.err.println("[SachYeuThichController] removeFromWishlist Exception for sachId " + maSach + ": " + e.getMessage()); // Log lỗi khác
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi xóa khỏi yêu thích: " + e.getMessage()));
//        }
//    }
//
//    // Kiểm tra sách có trong yêu thích không
//    @GetMapping("/kiem-tra/{maSach}")
//    public ResponseEntity<?> isInWishlist(@PathVariable int maSach) {
//        try {
//            boolean isInList = sachYeuThichService.isInWishlist(maSach);
//            // System.out.println("[SachYeuThichController] isInWishlist check for sachId " + maSach + ": " + isInList); // Bỏ log này vì có thể gọi nhiều lần
//            return ResponseEntity.ok(Map.of("isInWishlist", isInList));
//        } catch (Exception e) {
//            System.err.println("[SachYeuThichController] isInWishlist Exception for sachId " + maSach + ": " + e.getMessage()); // Log lỗi
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi kiểm tra danh sách yêu thích: " + e.getMessage()));
//        }
//    }
//}


package com.example.webbansach_backend.controller;

import com.example.webbansach_backend.Service.SachYeuThichService;
import com.example.webbansach_backend.dto.SachSimpleDTO;
import com.example.webbansach_backend.entity.SachYeuThich;
import com.example.webbansach_backend.entity.ThongBao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/yeu-thich")
public class SachYeuThichController {

    @Autowired
    private SachYeuThichService sachYeuThichService;

    // Lấy danh sách yêu thích
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMyWishlist() {
        try {
            List<SachSimpleDTO> wishlist = sachYeuThichService.getWishlist();
            System.out.println("[SachYeuThichController] getMyWishlist ok, size=" + wishlist.size());
            return ResponseEntity.ok(wishlist);
        } catch (RuntimeException e) {
            System.err.println("[SachYeuThichController] getMyWishlist Runtime: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
        } catch (Exception e) {
            System.err.println("[SachYeuThichController] getMyWishlist EX: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ThongBao("Lỗi khi lấy danh sách yêu thích: " + e.getMessage()));
        }
    }

    // Thêm
    @PostMapping("/them/{maSach}")
    public ResponseEntity<?> addToWishlist(@PathVariable int maSach) {
        try {
            SachYeuThich newItem = sachYeuThichService.addToWishlist(maSach);
            System.out.println("[SachYeuThichController] addToWishlist ok, sachId=" + maSach);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ThongBao("Đã thêm vào yêu thích thành công!"));
        } catch (RuntimeException e) {
            System.err.println("[SachYeuThichController] addToWishlist Runtime sachId=" + maSach + ": " + e.getMessage());
            if (e.getMessage().contains("chưa đăng nhập")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
            }
            if (e.getMessage().contains("không tồn tại")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao(e.getMessage()));
            }
            if (e.getMessage().contains("đã có trong danh sách")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ThongBao(e.getMessage()));
            }
            return ResponseEntity.badRequest().body(new ThongBao(e.getMessage()));
        } catch (Exception e) {
            System.err.println("[SachYeuThichController] addToWishlist EX sachId=" + maSach + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ThongBao("Lỗi khi thêm vào yêu thích: " + e.getMessage()));
        }
    }

    // Xoá
    @DeleteMapping("/xoa/{maSach}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable int maSach) {
        try {
            sachYeuThichService.removeFromWishlist(maSach);
            System.out.println("[SachYeuThichController] removeFromWishlist ok, sachId=" + maSach);
            // Giữ 200 + ThongBao cho đúng FE đang xử lý; nếu muốn chuẩn REST có thể trả 204 No Content
            return ResponseEntity.ok(new ThongBao("Đã xóa khỏi yêu thích thành công!"));
        } catch (RuntimeException e) {
            System.err.println("[SachYeuThichController] removeFromWishlist Runtime sachId=" + maSach + ": " + e.getMessage());
            if (e.getMessage().contains("chưa đăng nhập")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
            }
            if (e.getMessage().contains("không có trong danh sách")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao(e.getMessage()));
            }
            return ResponseEntity.badRequest().body(new ThongBao(e.getMessage()));
        } catch (Exception e) {
            System.err.println("[SachYeuThichController] removeFromWishlist EX sachId=" + maSach + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ThongBao("Lỗi khi xóa khỏi yêu thích: " + e.getMessage()));
        }
    }

    // Kiểm tra
    @GetMapping("/kiem-tra/{maSach}")
    public ResponseEntity<?> isInWishlist(@PathVariable int maSach) {
        try {
            boolean isInList = sachYeuThichService.isInWishlist(maSach);
            return ResponseEntity.ok(Map.of("isInWishlist", isInList));
        } catch (Exception e) {
            System.err.println("[SachYeuThichController] isInWishlist EX sachId=" + maSach + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ThongBao("Lỗi khi kiểm tra danh sách yêu thích: " + e.getMessage()));
        }
    }
}
