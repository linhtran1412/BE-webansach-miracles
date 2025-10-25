//package com.example.wedbansach_bakend1.controller;
//
//import com.example.wedbansach_bakend1.dao.DonHangRepository;
//import com.example.wedbansach_bakend1.entity.DonHang;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//        import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/don-hang")
//@CrossOrigin("*")
//public class DonHangController {
//
//    @Autowired
//    private DonHangRepository donHangRepository;
//
//    // Lấy danh sách tất cả đơn hàng
//    @GetMapping
//    public List<DonHang> getAllDonHang() {
//        return donHangRepository.findAll();
//    }
//
//    // Lấy thông tin đơn hàng theo ID
//    @GetMapping("/{id}")
//    public Optional<DonHang> getDonHangById(@PathVariable Long id) {
//        return donHangRepository.findById(id);
//    }
//
//    // Thêm mới đơn hàng
//    @PostMapping
//    public DonHang createDonHang(@RequestBody DonHang donHang) {
//        return donHangRepository.save(donHang);
//    }
//
//    // Cập nhật đơn hàng
//    @PutMapping("/{id}")
//    public DonHang updateDonHang(@PathVariable Long id, @RequestBody DonHang donHangMoi) {
//        return donHangRepository.findById(id).map(donHang -> {
//            donHang.setNgayTao(donHangMoi.getNgayTao());
//            donHang.setDiaChiMuaHang(donHangMoi.getDiaChiMuaHang());
//            donHang.setDiaChiNhanHang(donHangMoi.getDiaChiNhanHang());
//            donHang.setTongTienSanPham(donHangMoi.getTongTienSanPham());
//            donHang.setChiPhiGiaoHang(donHangMoi.getChiPhiGiaoHang());
//            donHang.setChiPhiThanhToan(donHangMoi.getChiPhiThanhToan());
//            donHang.setTongTien(donHangMoi.getTongTien());
//            return donHangRepository.save(donHang);
//        }).orElseGet(() -> donHangRepository.save(donHangMoi));
//    }
//
//    // Xóa đơn hàng
//    @DeleteMapping("/{id}")
//    public void deleteDonHang(@PathVariable Long id) {
//        donHangRepository.deleteById(id);
//    }
//}


//package com.example.wedbansach_bakend1.controller;
//
//import com.example.wedbansach_bakend1.dao.DonHangRepository;
//import com.example.wedbansach_bakend1.dao.NguoiDungRepository;
//import com.example.wedbansach_bakend1.entity.DonHang;
//import com.example.wedbansach_bakend1.entity.NguoiDung;
//import com.example.wedbansach_bakend1.entity.ThongBao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.ArrayList;
//
//@RestController
//@RequestMapping("/don-hang")
//public class DonHangController {
//
//    @Autowired
//    private DonHangRepository donHangRepository;
//
//    @Autowired
//    private NguoiDungRepository nguoiDungRepository;
//
//    // GET /don-hang (Lấy tất cả đơn hàng - Chỉ Admin)
//    @GetMapping
//    public ResponseEntity<List<DonHang>> getAllDonHang() {
//        List<DonHang> list = donHangRepository.findAll();
//        return ResponseEntity.ok(list);
//    }
//
//    // GET /don-hang/{id} (Lấy đơn hàng theo ID - Chỉ Admin)
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getDonHangById(@PathVariable Long id) {
//        Optional<DonHang> donHangOpt = donHangRepository.findById(id);
//        if(donHangOpt.isPresent()){
//            return ResponseEntity.ok(donHangOpt.get());
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao("Không tìm thấy đơn hàng ID: " + id));
//        }
//    }
//
//    // POST /don-hang (Thêm đơn hàng - Thường gọi từ Service)
//    @PostMapping
//    public ResponseEntity<?> createDonHang(@RequestBody DonHang donHang) {
//        try {
//            DonHang savedDonHang = donHangRepository.save(donHang);
//            return new ResponseEntity<>(savedDonHang, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi tạo đơn hàng: "+ e.getMessage()));
//        }
//    }
//
//    // === SỬA LẠI PHƯƠNG THỨC updateDonHang DÙNG IF/ELSE ===
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateDonHang(@PathVariable Long id, @RequestBody DonHang donHangMoi) {
//        // Tìm đơn hàng theo ID trước
//        Optional<DonHang> donHangOpt = donHangRepository.findById(id);
//
//        // Nếu tìm thấy đơn hàng
//        if (donHangOpt.isPresent()) {
//            DonHang donHang = donHangOpt.get(); // Lấy đối tượng DonHang ra
//
//            // Thực hiện cập nhật các trường cần thiết
//            if (donHangMoi.getTrangThai() != null) {
//                donHang.setTrangThai(donHangMoi.getTrangThai());
//            }
//            // Thêm các cập nhật khác nếu cần...
//            // donHang.setDiaChiNhanHang(donHangMoi.getDiaChiNhanHang());
//
//            try {
//                // Lưu lại đơn hàng đã cập nhật
//                DonHang updatedDonHang = donHangRepository.save(donHang);
//                // Trả về 200 OK với đối tượng DonHang đã cập nhật
//                return ResponseEntity.ok(updatedDonHang);
//            } catch (Exception e) {
//                // Xử lý lỗi nếu không lưu được
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi cập nhật đơn hàng: " + e.getMessage()));
//            }
//        }
//        // Nếu không tìm thấy đơn hàng (donHangOpt rỗng)
//        else {
//            // Trả về lỗi 404 Not Found với đối tượng ThongBao
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao("Không tìm thấy đơn hàng ID: " + id + " để cập nhật."));
//        }
//        // Kiểu trả về của phương thức là ResponseEntity<?> nên cả hai nhánh if/else đều hợp lệ
//    }
//    // === KẾT THÚC SỬA ===
//
//    // DELETE /don-hang/{id} (Xóa đơn hàng - Chỉ Admin)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteDonHang(@PathVariable Long id) {
//        if (donHangRepository.existsById(id)) {
//            try {
//                donHangRepository.deleteById(id);
//                return ResponseEntity.noContent().build();
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ThongBao("Không thể xóa đơn hàng ID: " + id + ". Lỗi: " + e.getMessage()));
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao("Không tìm thấy đơn hàng ID: " + id + " để xóa."));
//        }
//    }
//
//    // GET /don-hang/my-orders (Lấy đơn hàng của user hiện tại)
//    @GetMapping("/my-orders")
//    public ResponseEntity<?> getMyOrders() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao("Yêu cầu đăng nhập."));
//        }
//
//        String username;
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails) principal).getUsername();
//        } else if (principal instanceof String) {
//            username = (String) principal;
//            if ("anonymousUser".equals(username)) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao("Phiên làm việc không hợp lệ."));
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi xác định người dùng."));
//        }
//
//        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username);
//        if (nguoiDung == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao("Không tìm thấy người dùng."));
//        }
//
//        List<DonHang> myOrders = nguoiDung.getDanhSachDonhang();
//        if (myOrders == null) {
//            myOrders = new ArrayList<>();
//        }
//        return ResponseEntity.ok(myOrders);
//    }
//}
//package com.example.wedbansach_bakend1.controller;
//
//import com.example.wedbansach_bakend1.dao.DonHangRepository;
//import com.example.wedbansach_bakend1.dao.NguoiDungRepository;
//import com.example.wedbansach_bakend1.entity.DonHang;
//import com.example.wedbansach_bakend1.entity.NguoiDung;
//import com.example.wedbansach_bakend1.entity.ThongBao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.ArrayList;
//
//@RestController
//@RequestMapping("/don-hang")
//public class DonHangController {
//
//    @Autowired
//    private DonHangRepository donHangRepository;
//
//    @Autowired
//    private NguoiDungRepository nguoiDungRepository;
//
//    // GET /don-hang (Lấy tất cả đơn hàng - Admin/Staff)
//    @GetMapping
//    public ResponseEntity<List<DonHang>> getAllDonHang() {
//        List<DonHang> list = donHangRepository.findAll();
//        return ResponseEntity.ok(list);
//    }
//
//    // GET /don-hang/{id} (Lấy đơn hàng theo ID - Admin/Staff)
//    // === SỬA: Đổi Long thành Integer ===
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getDonHangById(@PathVariable Integer id) {
//        // === KẾT THÚC SỬA ===
//        Optional<DonHang> donHangOpt = donHangRepository.findById(id); // findById giờ nhận Integer
//        if(donHangOpt.isPresent()){
//            return ResponseEntity.ok(donHangOpt.get());
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao("Không tìm thấy đơn hàng ID: " + id));
//        }
//    }
//
//    // POST /don-hang (Thêm đơn hàng - Thường gọi từ Service, giữ nguyên)
//    @PostMapping
//    public ResponseEntity<?> createDonHang(@RequestBody DonHang donHang) {
//        try {
//            // Giả sử maDonHang tự tăng, không cần set ID
//            DonHang savedDonHang = donHangRepository.save(donHang);
//            return new ResponseEntity<>(savedDonHang, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi tạo đơn hàng: "+ e.getMessage()));
//        }
//    }
//
//    // PUT /don-hang/{id} (Cập nhật trạng thái - Admin/Staff)
//    // === SỬA: Đổi Long thành Integer ===
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateDonHang(@PathVariable Integer id, @RequestBody DonHang donHangMoi) {
//        // === KẾT THÚC SỬA ===
//        Optional<DonHang> donHangOpt = donHangRepository.findById(id); // findById giờ nhận Integer
//
//        if (donHangOpt.isPresent()) {
//            DonHang donHang = donHangOpt.get();
//
//            // Chỉ cập nhật trạng thái từ body request
//            if (donHangMoi.getTrangThai() != null) {
//                donHang.setTrangThai(donHangMoi.getTrangThai());
//            } else {
//                return ResponseEntity.badRequest().body(new ThongBao("Vui lòng cung cấp 'trangThai' mới trong body request."));
//            }
//
//            try {
//                DonHang updatedDonHang = donHangRepository.save(donHang);
//                return ResponseEntity.ok(updatedDonHang);
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi cập nhật đơn hàng: " + e.getMessage()));
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao("Không tìm thấy đơn hàng ID: " + id + " để cập nhật."));
//        }
//    }
//
//    // DELETE /don-hang/{id} (Xóa đơn hàng - Chỉ Admin)
//    // === SỬA: Đổi Long thành Integer ===
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteDonHang(@PathVariable Integer id) {
//        // === KẾT THÚC SỬA ===
//        if (donHangRepository.existsById(id)) { // existsById giờ nhận Integer
//            try {
//                donHangRepository.deleteById(id); // deleteById giờ nhận Integer
//                return ResponseEntity.noContent().build(); // 204 No Content
//            } catch (Exception e) {
//                // Ghi log lỗi chi tiết ở server
//                System.err.println("Lỗi khi xóa đơn hàng ID " + id + ": " + e.getMessage());
//                e.printStackTrace();
//                // Trả về lỗi chung cho client
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi hệ thống khi xóa đơn hàng ID: " + id));
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao("Không tìm thấy đơn hàng ID: " + id + " để xóa."));
//        }
//    }
//
//    // GET /don-hang/my-orders (Lấy đơn hàng của user hiện tại - Giữ nguyên)
//    @GetMapping("/my-orders")
//    public ResponseEntity<?> getMyOrders() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao("Yêu cầu đăng nhập."));
//        }
//
//        String username;
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails) principal).getUsername();
//        } else if (principal instanceof String) {
//            username = (String) principal;
//            if ("anonymousUser".equals(username)) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao("Phiên làm việc không hợp lệ."));
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi xác định người dùng."));
//        }
//
//        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username);
//        if (nguoiDung == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao("Không tìm thấy người dùng."));
//        }
//
//        // Lấy danh sách đơn hàng từ người dùng (nên đảm bảo @OneToMany được cấu hình đúng trong NguoiDung Entity)
//        List<DonHang> myOrders = nguoiDung.getDanhSachDonhang();
//        if (myOrders == null) {
//            myOrders = new ArrayList<>(); // Trả về list rỗng thay vì lỗi
//        }
//        return ResponseEntity.ok(myOrders);
//    }
//}



package com.example.webbansach_backend.controller;

import com.example.webbansach_backend.Service.DonHangService;           // <<<< THÊM
import com.example.webbansach_backend.dao.DonHangRepository;
import com.example.webbansach_backend.dao.NguoiDungRepository;
import com.example.webbansach_backend.dto.DonHangListDTO;              // <<<< THÊM
import com.example.webbansach_backend.entity.DonHang;
import com.example.webbansach_backend.entity.ThongBao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/don-hang")
public class DonHangController {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private DonHangService donHangService;                              // <<<< THÊM

    // GET /don-hang (Lấy tất cả đơn hàng - Admin/Staff)
    @GetMapping
    public ResponseEntity<List<DonHang>> getAllDonHang() {
        List<DonHang> list = donHangRepository.findAll();
        return ResponseEntity.ok(list);
    }

    // GET /don-hang/{id} (Lấy đơn hàng theo ID - Admin/Staff)
    @GetMapping("/{id}")
    public ResponseEntity<?> getDonHangById(@PathVariable Integer id) {
        Optional<DonHang> donHangOpt = donHangRepository.findById(id);
        if (donHangOpt.isPresent()) {
            return ResponseEntity.ok(donHangOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ThongBao("Không tìm thấy đơn hàng ID: " + id));
        }
    }

    // POST /don-hang (Tạo đơn – thường dùng nội bộ service)
    @PostMapping
    public ResponseEntity<?> createDonHang(@RequestBody DonHang donHang) {
        try {
            DonHang savedDonHang = donHangRepository.save(donHang);
            return new ResponseEntity<>(savedDonHang, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ThongBao("Lỗi khi tạo đơn hàng: " + e.getMessage()));
        }
    }

    // PUT /don-hang/{id} (Cập nhật trạng thái - Admin/Staff)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDonHang(@PathVariable Integer id, @RequestBody DonHang donHangMoi) {
        Optional<DonHang> donHangOpt = donHangRepository.findById(id);
        if (donHangOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ThongBao("Không tìm thấy đơn hàng ID: " + id + " để cập nhật."));
        }

        DonHang donHang = donHangOpt.get();
        if (donHangMoi.getTrangThai() == null) {
            return ResponseEntity.badRequest()
                    .body(new ThongBao("Vui lòng cung cấp 'trangThai' mới trong body request."));
        }

        try {
            donHang.setTrangThai(donHangMoi.getTrangThai());
            DonHang updatedDonHang = donHangRepository.save(donHang);
            return ResponseEntity.ok(updatedDonHang);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ThongBao("Lỗi khi cập nhật đơn hàng: " + e.getMessage()));
        }
    }

    // DELETE /don-hang/{id} (Xoá đơn - Admin)
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

    // ===== /don-hang/my-orders (User xem đơn của mình) – TRẢ DTO, KHÔNG TRẢ ENTITY =====
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders() {
        try {
            List<DonHangListDTO> list = donHangService.getMyOrders();   // lấy theo user đăng nhập & map DTO
            return ResponseEntity.ok(list);
        } catch (RuntimeException e) { // chưa đăng nhập, user không tồn tại, ...
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ThongBao("Lỗi khi lấy lịch sử đơn hàng: " + e.getMessage()));
        }
    }
}
