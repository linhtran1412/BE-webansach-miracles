//package com.example.webbansach_backend.Service;
//
//import com.example.webbansach_backend.dao.DonHangRepository;
//import com.example.webbansach_backend.dao.NguoiDungRepository;
//import com.example.webbansach_backend.dto.DonHangListDTO;
//import com.example.webbansach_backend.entity.DonHang;
//import com.example.webbansach_backend.entity.NguoiDung;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class DonHangService {
//
//    @Autowired private DonHangRepository donHangRepository;
//    @Autowired private NguoiDungRepository nguoiDungRepository;
//
//    private NguoiDung getCurrentUser() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
//            throw new RuntimeException("Người dùng chưa đăng nhập.");
//        }
//        NguoiDung u = nguoiDungRepository.findByTenDangNhap(auth.getName());
//        if (u == null) throw new RuntimeException("Người dùng không tồn tại.");
//        return u;
//    }
//
//    @Transactional
//    public List<DonHangListDTO> getMyOrders() {
//        int maNguoiDung = getCurrentUser().getMaNguoiDung();
//        List<DonHang> list = donHangRepository
//                .findByNguoiDung_MaNguoiDungOrderByNgayTaoDesc(maNguoiDung);
//
//        return list.stream().map(dh -> {
//            DonHangListDTO dto = new DonHangListDTO();
//            dto.setMaDonHang(dh.getMaDonHang());
//            // java.sql.Date -> String YYYY-MM-DD
//            dto.setNgayTao(dh.getNgayTao() != null ? dh.getNgayTao().toLocalDate().toString() : null);
//            dto.setTongTien(dh.getTongTien());
//            dto.setDiaChiNhanHang(dh.getDiaChiNhanHang());
//            dto.setTrangThai(dh.getTrangThai());
//            return dto;
//        }).collect(Collectors.toList());
//    }
//}

//package com.example.webbansach_backend.Service;
//
//import com.example.webbansach_backend.dao.DonHangRepository;
//import com.example.webbansach_backend.dao.NguoiDungRepository;
//import com.example.webbansach_backend.dto.ChiTietDonHangDTO;
//import com.example.webbansach_backend.dto.DonHangDetailDTO;
//import com.example.webbansach_backend.dto.DonHangListDTO;
//import com.example.webbansach_backend.entity.DonHang;
//import com.example.webbansach_backend.entity.HinhAnh;
//import com.example.webbansach_backend.entity.NguoiDung;
//import com.example.webbansach_backend.entity.Sach;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class DonHangService {
//
//    @Autowired private DonHangRepository donHangRepository;
//    @Autowired private NguoiDungRepository nguoiDungRepository;
//
//    /** Lấy user hiện tại (throw nếu chưa đăng nhập) */
//    private NguoiDung getCurrentUser() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
//            throw new RuntimeException("Người dùng chưa đăng nhập.");
//        }
//        NguoiDung u = nguoiDungRepository.findByTenDangNhap(auth.getName());
//        if (u == null) throw new RuntimeException("Người dùng không tồn tại.");
//        return u;
//    }
//
//    /** Danh sách đơn của chính user (DTO gọn) */
//    @Transactional
//    public List<DonHangListDTO> getMyOrders() {
//        int maNguoiDung = getCurrentUser().getMaNguoiDung();
//        List<DonHang> list =
//                donHangRepository.findByNguoiDung_MaNguoiDungOrderByNgayTaoDesc(maNguoiDung);
//
//        return list.stream().map(dh -> {
//            DonHangListDTO dto = new DonHangListDTO();
//            dto.setMaDonHang(dh.getMaDonHang());
//            dto.setNgayTao(dh.getNgayTao() != null ? dh.getNgayTao().toLocalDate().toString() : null);
//            dto.setTongTien(dh.getTongTien());
//            dto.setDiaChiNhanHang(dh.getDiaChiNhanHang());
//            dto.setTrangThai(dh.getTrangThai());
//            return dto;
//        }).collect(Collectors.toList());
//    }
//
//    /** Chi tiết 1 đơn của chính user (DTO đầy đủ) */
//    @Transactional
//    public DonHangDetailDTO getMyOrderDetail(int maDonHang) {
//        NguoiDung current = getCurrentUser();
//
//        DonHang dh = donHangRepository.findById(maDonHang)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng #" + maDonHang));
//
//        // Chặn xem chéo đơn
//        if (dh.getNguoiDung() == null || dh.getNguoiDung().getMaNguoiDung() != current.getMaNguoiDung()) {
//            throw new RuntimeException("Bạn không có quyền xem đơn hàng này.");
//        }
//
//        DonHangDetailDTO dto = new DonHangDetailDTO();
//        dto.setMaDonHang(dh.getMaDonHang());
//        dto.setNgayTao(dh.getNgayTao() != null ? dh.getNgayTao().toLocalDate().toString() : null);
//
//        // Địa chỉ
//        dto.setDiaChiMuaHang(dh.getDiaChiMuaHang());
//        dto.setDiaChiNhanHang(dh.getDiaChiNhanHang());
//
//        // Hình thức (tạm dùng toString() cho chắc; nếu entity có field tên, đổi lại getter tương ứng)
//        dto.setHinhThucThanhToan(dh.getHinhThucThanhToan() != null ? dh.getHinhThucThanhToan().toString() : null);
//        dto.setHinhThucGiaoHang(dh.getHinhThucGiaoHang() != null ? dh.getHinhThucGiaoHang().toString() : null);
//        // Ví dụ sau khi xác nhận field:
//        // dto.setHinhThucThanhToan(dh.getHinhThucThanhToan()!=null ? dh.getHinhThucThanhToan().getTenHinhThuc() : null);
//        // dto.setHinhThucGiaoHang(dh.getHinhThucGiaoHang()!=null ? dh.getHinhThucGiaoHang().getTenHinhThuc() : null);
//
//        // Phí/tiền
//        dto.setTongTienSanPham(dh.getTongTienSanPham());
//        dto.setChiPhiGiaoHang(dh.getChiPhiGiaoHang());
//        dto.setChiPhiThanhToan(dh.getChiPhiThanhToan());
//        dto.setTongTien(dh.getTongTien());
//
//        // Trạng thái
//        dto.setTrangThai(dh.getTrangThai());
//
//        // Dòng sản phẩm
//        if (dh.getDanhSachChiTietDonHang() == null || dh.getDanhSachChiTietDonHang().isEmpty()) {
//            dto.setChiTiet(Collections.emptyList());
//            return dto;
//        }
//
//        dto.setChiTiet(
//                dh.getDanhSachChiTietDonHang().stream().map(ct -> {
//                    ChiTietDonHangDTO c = new ChiTietDonHangDTO();
//
//                    Sach sach = ct.getSach();
//                    if (sach != null) {
//                        c.setMaSach(sach.getMaSach());
//                        c.setTenSach(sach.getTenSach());
//                        c.setHinhAnhDaiDien(layHinhAnhDaiDien(sach));
//                    }
//
//                    c.setSoLuong(ct.getSoLuong());
//
//                    // Đơn giá tại thời điểm mua (ưu tiên lấy từ chi tiết nếu entity có trường này)
//                    double giaBan = 0.0;
//                    try {
//                        giaBan = ct.getGiaBan(); // nếu không tồn tại, catch bên dưới sẽ chạy
//                    } catch (Throwable ignore) {
//                        if (sach != null) {
//                            // getGiaBan() của Sach trong project này thường là double (primitive) → không cần check null
//                            giaBan = sach.getGiaBan();
//                        }
//                    }
//
//                    c.setGiaBan(giaBan);
//                    c.setThanhTien(giaBan * ct.getSoLuong());
//                    return c;
//                }).collect(Collectors.toList())
//        );
//
//        return dto;
//    }
//
//    /** Ảnh đại diện: ưu tiên ảnh isLaIcon; nếu không có thì lấy ảnh đầu tiên; nếu vẫn không có trả null */
//    private String layHinhAnhDaiDien(Sach sach) {
//        if (sach.getDanhSachHinhAnh() == null || sach.getDanhSachHinhAnh().isEmpty()) return null;
//        for (HinhAnh ha : sach.getDanhSachHinhAnh()) {
//            try {
//                if (ha.isLaIcon()) return ha.getDuLieuAnh();
//            } catch (Throwable ignore) { /* bỏ qua ảnh lỗi field */ }
//        }
//        return sach.getDanhSachHinhAnh().get(0).getDuLieuAnh();
//    }
//}
//

package com.example.webbansach_backend.Service;

import com.example.webbansach_backend.dao.DonHangRepository;
import com.example.webbansach_backend.dao.NguoiDungRepository;
import com.example.webbansach_backend.dto.ChiTietDonHangDTO;
import com.example.webbansach_backend.dto.DonHangDetailDTO;
import com.example.webbansach_backend.dto.DonHangListDTO;
import com.example.webbansach_backend.entity.DonHang;
import com.example.webbansach_backend.entity.HinhAnh;
import com.example.webbansach_backend.entity.NguoiDung;
import com.example.webbansach_backend.entity.Sach;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class DonHangService {

    @Autowired private DonHangRepository donHangRepository;
    @Autowired private NguoiDungRepository nguoiDungRepository;

    /* ===================== Helpers (label/normalize) ===================== */

    /** Chuẩn hoá & gán label trạng thái để FE hiển thị thống nhất. */
    private String statusLabel(String raw) {
        if (raw == null || raw.isBlank()) return "Chờ xử lý";
        String s = raw.trim().toLowerCase(Locale.ROOT);
        switch (s) {
            case "cho xu ly":
            case "chờ xử lý":
            case "pending":
            case "new":
                return "Chờ xử lý";
            case "dang giao hang":
            case "đang giao hàng":
            case "shipping":
            case "in_transit":
                return "Đang giao hàng";
            case "da giao hang":
            case "đã giao hàng":
            case "delivered":
                return "Đã giao hàng";
            case "da huy":
            case "đã hủy":
            case "cancelled":
            case "canceled":
                return "Đã hủy";
            default:
                // nếu BE có trạng thái khác, vẫn trả về raw đã capitalize sơ bộ
                return Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
        }
    }

    /** Lấy label tên hình thức thanh toán (an toàn null). */
    private String paymentLabel(DonHang dh) {
        if (dh.getHinhThucThanhToan() == null) return null;
        // Entity đã sửa tên field đúng: getTenHinhThucThanhToan()
        return dh.getHinhThucThanhToan().getTenHinhThucThanhToan();
    }

    /** Lấy label tên hình thức giao hàng (an toàn null). */
    private String shippingLabel(DonHang dh) {
        if (dh.getHinhThucGiaoHang() == null) return null;
        // Entity Giao hàng đã đúng sẵn: getTenHinhThucGiaoHang()
        return dh.getHinhThucGiaoHang().getTenHinhThucGiaoHang();
    }

    /** Ảnh đại diện sách: ưu tiên ảnh isLaIcon, nếu không có lấy ảnh đầu tiên. */
    private String layHinhAnhDaiDien(Sach sach) {
        if (sach == null || sach.getDanhSachHinhAnh() == null || sach.getDanhSachHinhAnh().isEmpty()) {
            return null;
        }
        for (HinhAnh ha : sach.getDanhSachHinhAnh()) {
            try {
                if (ha.isLaIcon()) return ha.getDuLieuAnh();
            } catch (Throwable ignore) { /* bỏ qua field lỗi */ }
        }
        return sach.getDanhSachHinhAnh().get(0).getDuLieuAnh();
    }

    /** Lấy user hiện tại (throw nếu chưa đăng nhập) */
    private NguoiDung getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new RuntimeException("Người dùng chưa đăng nhập.");
        }
        NguoiDung u = nguoiDungRepository.findByTenDangNhap(auth.getName());
        if (u == null) throw new RuntimeException("Người dùng không tồn tại.");
        return u;
    }

    /* ========================= APIs cho người dùng ========================= */

    /** Danh sách đơn của chính user (DTO gọn, có label trạng thái). */
    @Transactional
    public List<DonHangListDTO> getMyOrders() {
        int maNguoiDung = getCurrentUser().getMaNguoiDung();
        List<DonHang> list =
                donHangRepository.findByNguoiDung_MaNguoiDungOrderByNgayTaoDesc(maNguoiDung);

        return list.stream().map(dh -> {
            DonHangListDTO dto = new DonHangListDTO();
            dto.setMaDonHang(dh.getMaDonHang());
            dto.setNgayTao(dh.getNgayTao() != null ? dh.getNgayTao().toLocalDate().toString() : null);
            dto.setTongTien(dh.getTongTien());
            dto.setDiaChiNhanHang(dh.getDiaChiNhanHang());
            dto.setTrangThai(statusLabel(dh.getTrangThai()));   // ✅ gán label
            return dto;
        }).collect(Collectors.toList());
    }

    /** Chi tiết 1 đơn của chính user (DTO đầy đủ, có label). */
    @Transactional
    public DonHangDetailDTO getMyOrderDetail(int maDonHang) {
        NguoiDung current = getCurrentUser();

        DonHang dh = donHangRepository.findById(maDonHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng #" + maDonHang));

        // Chặn xem chéo đơn
        if (dh.getNguoiDung() == null || dh.getNguoiDung().getMaNguoiDung() != current.getMaNguoiDung()) {
            throw new RuntimeException("Bạn không có quyền xem đơn hàng này.");
        }

        DonHangDetailDTO dto = new DonHangDetailDTO();
        dto.setMaDonHang(dh.getMaDonHang());
        dto.setNgayTao(dh.getNgayTao() != null ? dh.getNgayTao().toLocalDate().toString() : null);

        // Địa chỉ
        dto.setDiaChiMuaHang(dh.getDiaChiMuaHang());
        dto.setDiaChiNhanHang(dh.getDiaChiNhanHang());

        // Hình thức (label chuẩn)
        dto.setHinhThucThanhToan(paymentLabel(dh));  // ✅ label
        dto.setHinhThucGiaoHang(shippingLabel(dh));  // ✅ label

        // Phí/tiền
        dto.setTongTienSanPham(dh.getTongTienSanPham());
        dto.setChiPhiGiaoHang(dh.getChiPhiGiaoHang());
        dto.setChiPhiThanhToan(dh.getChiPhiThanhToan());
        dto.setTongTien(dh.getTongTien());

        // Trạng thái (label)
        dto.setTrangThai(statusLabel(dh.getTrangThai())); // ✅ label

        // Dòng sản phẩm
        if (dh.getDanhSachChiTietDonHang() == null || dh.getDanhSachChiTietDonHang().isEmpty()) {
            dto.setChiTiet(Collections.emptyList());
            return dto;
        }

        dto.setChiTiet(
                dh.getDanhSachChiTietDonHang().stream().map(ct -> {
                    ChiTietDonHangDTO c = new ChiTietDonHangDTO();

                    Sach sach = ct.getSach();
                    if (sach != null) {
                        c.setMaSach(sach.getMaSach());
                        c.setTenSach(sach.getTenSach());
                        c.setHinhAnhDaiDien(layHinhAnhDaiDien(sach));
                    }

                    c.setSoLuong(ct.getSoLuong());

                    // Đơn giá tại thời điểm mua (ưu tiên field trong chi tiết; nếu project bạn chưa có thì fallback về giá hiện tại)
                    double giaBan;
                    try {
                        giaBan = ct.getGiaBan(); // nếu không tồn tại field, sẽ ném lỗi và vào catch
                    } catch (Throwable ignore) {
                        giaBan = (sach != null) ? sach.getGiaBan() : 0.0;
                    }

                    c.setGiaBan(giaBan);
                    c.setThanhTien(giaBan * ct.getSoLuong());
                    return c;
                }).collect(Collectors.toList())
        );

        return dto;
    }
}
