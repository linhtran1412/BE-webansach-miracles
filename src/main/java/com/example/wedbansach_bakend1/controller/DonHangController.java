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


package com.example.wedbansach_bakend1.controller;

import com.example.wedbansach_bakend1.dao.DonHangRepository;
import com.example.wedbansach_bakend1.dao.NguoiDungRepository;
import com.example.wedbansach_bakend1.entity.DonHang;
import com.example.wedbansach_bakend1.entity.NguoiDung;
import com.example.wedbansach_bakend1.entity.ThongBao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@RestController
@RequestMapping("/don-hang")
public class DonHangController {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    // GET /don-hang (Lấy tất cả đơn hàng - Chỉ Admin)
    @GetMapping
    public ResponseEntity<List<DonHang>> getAllDonHang() {
        List<DonHang> list = donHangRepository.findAll();
        return ResponseEntity.ok(list);
    }

    // GET /don-hang/{id} (Lấy đơn hàng theo ID - Chỉ Admin)
    @GetMapping("/{id}")
    public ResponseEntity<?> getDonHangById(@PathVariable Long id) {
        Optional<DonHang> donHangOpt = donHangRepository.findById(id);
        if(donHangOpt.isPresent()){
            return ResponseEntity.ok(donHangOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao("Không tìm thấy đơn hàng ID: " + id));
        }
    }

    // POST /don-hang (Thêm đơn hàng - Thường gọi từ Service)
    @PostMapping
    public ResponseEntity<?> createDonHang(@RequestBody DonHang donHang) {
        try {
            DonHang savedDonHang = donHangRepository.save(donHang);
            return new ResponseEntity<>(savedDonHang, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi tạo đơn hàng: "+ e.getMessage()));
        }
    }

    // === SỬA LẠI PHƯƠNG THỨC updateDonHang DÙNG IF/ELSE ===
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDonHang(@PathVariable Long id, @RequestBody DonHang donHangMoi) {
        // Tìm đơn hàng theo ID trước
        Optional<DonHang> donHangOpt = donHangRepository.findById(id);

        // Nếu tìm thấy đơn hàng
        if (donHangOpt.isPresent()) {
            DonHang donHang = donHangOpt.get(); // Lấy đối tượng DonHang ra

            // Thực hiện cập nhật các trường cần thiết
            if (donHangMoi.getTrangThai() != null) {
                donHang.setTrangThai(donHangMoi.getTrangThai());
            }
            // Thêm các cập nhật khác nếu cần...
            // donHang.setDiaChiNhanHang(donHangMoi.getDiaChiNhanHang());

            try {
                // Lưu lại đơn hàng đã cập nhật
                DonHang updatedDonHang = donHangRepository.save(donHang);
                // Trả về 200 OK với đối tượng DonHang đã cập nhật
                return ResponseEntity.ok(updatedDonHang);
            } catch (Exception e) {
                // Xử lý lỗi nếu không lưu được
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi khi cập nhật đơn hàng: " + e.getMessage()));
            }
        }
        // Nếu không tìm thấy đơn hàng (donHangOpt rỗng)
        else {
            // Trả về lỗi 404 Not Found với đối tượng ThongBao
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao("Không tìm thấy đơn hàng ID: " + id + " để cập nhật."));
        }
        // Kiểu trả về của phương thức là ResponseEntity<?> nên cả hai nhánh if/else đều hợp lệ
    }
    // === KẾT THÚC SỬA ===

    // DELETE /don-hang/{id} (Xóa đơn hàng - Chỉ Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDonHang(@PathVariable Long id) {
        if (donHangRepository.existsById(id)) {
            try {
                donHangRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ThongBao("Không thể xóa đơn hàng ID: " + id + ". Lỗi: " + e.getMessage()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao("Không tìm thấy đơn hàng ID: " + id + " để xóa."));
        }
    }

    // GET /don-hang/my-orders (Lấy đơn hàng của user hiện tại)
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao("Yêu cầu đăng nhập."));
        }

        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
            if ("anonymousUser".equals(username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao("Phiên làm việc không hợp lệ."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi xác định người dùng."));
        }

        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username);
        if (nguoiDung == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThongBao("Không tìm thấy người dùng."));
        }

        List<DonHang> myOrders = nguoiDung.getDanhSachDonhang();
        if (myOrders == null) {
            myOrders = new ArrayList<>();
        }
        return ResponseEntity.ok(myOrders);
    }
}