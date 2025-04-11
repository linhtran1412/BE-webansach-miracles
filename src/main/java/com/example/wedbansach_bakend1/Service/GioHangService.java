package com.example.wedbansach_bakend1.Service;

import com.example.wedbansach_bakend1.dao.*;
import com.example.wedbansach_bakend1.dto.ChiTietGioHangDTO;
import com.example.wedbansach_bakend1.dto.GioHangDTO;
import com.example.wedbansach_bakend1.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GioHangService {

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private ChiTietGioHangRepository chiTietGioHangRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private SachRepository sachRepository;

    // Lấy tên đăng nhập của người dùng hiện tại từ context security
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Trả về username
    }

    // Lấy thông tin giỏ hàng của người dùng hiện tại
    @Transactional // Đảm bảo các thao tác trong transaction
    public GioHangDTO getGioHangDTOByCurrentUser() {
        String username = getCurrentUsername();
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username);
        if (nguoiDung == null) {
            throw new RuntimeException("Người dùng không tồn tại.");
        }

        // Tìm giỏ hàng, nếu chưa có thì tạo mới
        GioHang gioHang = gioHangRepository.findByNguoiDung_MaNguoiDung(nguoiDung.getMaNguoiDung())
                .orElseGet(() -> {
                    GioHang newCart = new GioHang();
                    newCart.setNguoiDung(nguoiDung);
                    return gioHangRepository.save(newCart);
                });

        return convertToDTO(gioHang);
    }


    // Thêm sách vào giỏ
    @Transactional
    public GioHangDTO themSachVaoGio(int maSach, int soLuong) {
        String username = getCurrentUsername();
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username);
        if (nguoiDung == null) throw new RuntimeException("Người dùng không tồn tại.");

        Sach sach = sachRepository.findById(maSach)
                .orElseThrow(() -> new RuntimeException("Sách không tồn tại."));

        if (sach.getSoLuong() < soLuong) {
            throw new RuntimeException("Số lượng sách trong kho không đủ.");
        }

        GioHang gioHang = gioHangRepository.findByNguoiDung_MaNguoiDung(nguoiDung.getMaNguoiDung())
                .orElseGet(() -> {
                    GioHang newCart = new GioHang();
                    newCart.setNguoiDung(nguoiDung);
                    return gioHangRepository.save(newCart);
                });

        // Kiểm tra sách đã có trong giỏ chưa
        Optional<ChiTietGioHang> chiTietOpt = chiTietGioHangRepository.findByGioHang_MaGioHangAndSach_MaSach(gioHang.getMaGioHang(), maSach);

        if (chiTietOpt.isPresent()) {
            // Nếu có, cập nhật số lượng
            ChiTietGioHang chiTiet = chiTietOpt.get();
            int soLuongMoi = chiTiet.getSoLuong() + soLuong;
            if (sach.getSoLuong() < soLuongMoi) {
                throw new RuntimeException("Số lượng sách trong kho không đủ để thêm.");
            }
            chiTiet.setSoLuong(soLuongMoi);
            chiTietGioHangRepository.save(chiTiet);
        } else {
            // Nếu chưa, tạo chi tiết mới
            ChiTietGioHang chiTietMoi = new ChiTietGioHang();
            chiTietMoi.setSach(sach);
            chiTietMoi.setSoLuong(soLuong);
            chiTietMoi.setGioHang(gioHang);
            // gioHang.addChiTietGioHang(chiTietMoi); // Dòng này không cần thiết nếu cascade hoạt động đúng
            chiTietGioHangRepository.save(chiTietMoi);
        }

        // gioHangRepository.save(gioHang); // Lưu lại giỏ hàng (có thể không cần nếu cascade hoạt động)
        // Cần reload giỏ hàng để lấy danh sách chi tiết mới nhất sau khi save chi tiết
        GioHang gioHangUpdated = gioHangRepository.findById(gioHang.getMaGioHang()).orElse(gioHang);
        return convertToDTO(gioHangUpdated);
    }

//    // Xóa sách khỏi giỏ
//    @Transactional
//    public GioHangDTO xoaSachKhoiGio(int maSach) {
//        String username = getCurrentUsername();
//        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username);
//        if (nguoiDung == null) throw new RuntimeException("Người dùng không tồn tại.");
//
//        GioHang gioHang = gioHangRepository.findByNguoiDung_MaNguoiDung(nguoiDung.getMaNguoiDung())
//                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại."));
//
//        Optional<ChiTietGioHang> chiTietOpt = chiTietGioHangRepository.findByGioHang_MaGioHangAndSach_MaSach(gioHang.getMaGioHang(), maSach);
//
//        if (chiTietOpt.isPresent()) {
//            chiTietGioHangRepository.delete(chiTietOpt.get());
//            // Cần reload giỏ hàng để lấy danh sách chi tiết mới nhất sau khi xóa
//            GioHang gioHangUpdated = gioHangRepository.findById(gioHang.getMaGioHang()).orElse(gioHang);
//            return convertToDTO(gioHangUpdated);
//        } else {
//            throw new RuntimeException("Sách không có trong giỏ hàng.");
//        }
//        // Không cần save gioHang vì cascade REMOVE hoặc orphanRemoval=true sẽ xử lý
//    }

    @Autowired private EntityManager entityManager;
    @Transactional
    public void xoaSachKhoiGioVaKhongTraVeDTO(int maSach) { // Đổi tên và kiểu trả về void
        System.out.println("==> [Service] Bắt đầu xoaSachKhoiGioVaKhongTraVeDTO cho maSach: " + maSach);
        String username = getCurrentUsername();
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username);
        if (nguoiDung == null) {
            System.err.println("==> [Service] LỖI: Người dùng không tồn tại: " + username);
            throw new RuntimeException("Người dùng không tồn tại.");
        }

        GioHang gioHang = gioHangRepository.findByNguoiDung_MaNguoiDung(nguoiDung.getMaNguoiDung())
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại."));

        Optional<ChiTietGioHang> chiTietOpt = chiTietGioHangRepository.findByGioHang_MaGioHangAndSach_MaSach(gioHang.getMaGioHang(), maSach);

        if (chiTietOpt.isPresent()) {
            ChiTietGioHang chiTietCanXoa = chiTietOpt.get();
            long chiTietId = chiTietCanXoa.getMaChiTietGioHang();
            System.out.println("==> [Service] Tìm thấy chiTietGioHang ID: " + chiTietId + " để xóa.");
            System.out.println("==> [Service] Chuẩn bị gọi chiTietGioHangRepository.delete() cho ID: " + chiTietId);
            try {
                chiTietGioHangRepository.delete(chiTietCanXoa);
                entityManager.flush(); // <-- Thêm dòng này để ép lệnh xuống DB
                System.out.println("==> [Service] Đã gọi delete() và flush() thành công.");
            } catch (Exception e) {
                System.err.println("==> [Service] LỖI khi gọi delete()/flush(): " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Lỗi khi xóa chi tiết giỏ hàng khỏi DB.", e);
            }
        } else {
            System.err.println("==> [Service] LỖI: Sách (maSach=" + maSach + ") không có trong giỏ hàng ID: " + gioHang.getMaGioHang());
            throw new RuntimeException("Sách không có trong giỏ hàng.");
        }
        System.out.println("==> [Service] Hoàn thành xoaSachKhoiGioVaKhongTraVeDTO.");
        // Không return DTO nữa
    }


    // Cập nhật số lượng sách trong giỏ
    @Transactional
    public GioHangDTO capNhatSoLuong(int maSach, int soLuongMoi) {
        String username = getCurrentUsername();
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username);
        if (nguoiDung == null) throw new RuntimeException("Người dùng không tồn tại.");

        GioHang gioHang = gioHangRepository.findByNguoiDung_MaNguoiDung(nguoiDung.getMaNguoiDung())
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại."));

        ChiTietGioHang chiTiet = chiTietGioHangRepository.findByGioHang_MaGioHangAndSach_MaSach(gioHang.getMaGioHang(), maSach)
                .orElseThrow(() -> new RuntimeException("Sách không có trong giỏ hàng."));

        Sach sach = chiTiet.getSach();
        if (sach.getSoLuong() < soLuongMoi) {
            throw new RuntimeException("Số lượng sách trong kho không đủ.");
        }
        if (soLuongMoi <= 0) {
            // Nếu số lượng <= 0 thì xóa khỏi giỏ
            chiTietGioHangRepository.delete(chiTiet);
        } else {
            chiTiet.setSoLuong(soLuongMoi);
            chiTietGioHangRepository.save(chiTiet);
        }
        // Cần reload giỏ hàng để lấy danh sách chi tiết mới nhất
        GioHang gioHangUpdated = gioHangRepository.findById(gioHang.getMaGioHang()).orElse(gioHang);
        return convertToDTO(gioHangUpdated);
    }

    // Hàm chuyển đổi Entity sang DTO
    private GioHangDTO convertToDTO(GioHang gioHang) {
        GioHangDTO dto = new GioHangDTO();
        dto.setMaGioHang(gioHang.getMaGioHang());
        dto.setMaNguoiDung(gioHang.getNguoiDung().getMaNguoiDung());

        List<ChiTietGioHangDTO> chiTietDTOs = gioHang.getDanhSachChiTietGioHang().stream()
                .map(chiTiet -> {
                    ChiTietGioHangDTO ctDTO = new ChiTietGioHangDTO();
                    ctDTO.setMaChiTietGioHang(chiTiet.getMaChiTietGioHang());
                    ctDTO.setSoLuong(chiTiet.getSoLuong());
                    ctDTO.setMaSach(chiTiet.getSach().getMaSach());
                    ctDTO.setTenSach(chiTiet.getSach().getTenSach());
                    ctDTO.setGiaBan(chiTiet.getSach().getGiaBan());
                    // Lấy ảnh đầu tiên (nếu có)
                    if (chiTiet.getSach().getDanhSachHinhAnh() != null && !chiTiet.getSach().getDanhSachHinhAnh().isEmpty()) {
                        ctDTO.setHinhAnh(chiTiet.getSach().getDanhSachHinhAnh().get(0).getDuLieuAnh()); // Hoặc duongDan
                    } else {
                        ctDTO.setHinhAnh(null); // Hoặc ảnh mặc định
                    }
                    return ctDTO;
                })
                .collect(Collectors.toList());

        dto.setDanhSachChiTietGioHang(chiTietDTOs);

        // Tính tổng tiền
        double tongTien = chiTietDTOs.stream()
                .mapToDouble(ct -> ct.getGiaBan() * ct.getSoLuong())
                .sum();
        dto.setTongTien(tongTien);

        return dto;
    }
}