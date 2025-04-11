package com.example.wedbansach_bakend1.Service;

import com.example.wedbansach_bakend1.dao.*;
import com.example.wedbansach_bakend1.dto.CheckoutRequestDTO;
import com.example.wedbansach_bakend1.dto.DonHangDTO;
import com.example.wedbansach_bakend1.dto.ChiTietDonHangDTO;
import com.example.wedbansach_bakend1.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Autowired private NguoiDungRepository nguoiDungRepository;
    @Autowired private GioHangRepository gioHangRepository;
    @Autowired private ChiTietGioHangRepository chiTietGioHangRepository;
    @Autowired private SachRepository sachRepository;
    @Autowired private DonHangRepository donHangRepository;
    @Autowired private ChiTietDonHangRepository chiTietDonHangRepository;
    @Autowired private HinhThucGiaoHangRepository hinhThucGiaoHangRepository;
    @Autowired private HinhThucThanhToanRepository hinhThucThanhToanRepository;

    @Override
    @Transactional
    public DonHangDTO placeOrder(CheckoutRequestDTO checkoutRequestDto, String username) {
        // 1. Lấy người dùng (giữ nguyên)
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username);
        if (nguoiDung == null) {
            throw new RuntimeException("Người dùng không tồn tại.");
        }

        // 2. Lấy giỏ hàng (giữ nguyên)
        GioHang gioHang = gioHangRepository.findByNguoiDung_MaNguoiDung(nguoiDung.getMaNguoiDung())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng hoặc giỏ hàng không thuộc về bạn."));

        List<ChiTietGioHang> chiTietGioHangs = gioHang.getDanhSachChiTietGioHang();
        if (chiTietGioHangs == null || chiTietGioHangs.isEmpty()) {
            throw new RuntimeException("Giỏ hàng đang trống.");
        }

        // 3. Kiểm tra tồn kho và tính tổng tiền sản phẩm
        double tongTienSanPham = 0;
        List<Sach> sachTrongDonHang = new ArrayList<>();
        for (ChiTietGioHang ctgh : chiTietGioHangs) {
            if (ctgh.getSach() == null) {
                throw new RuntimeException("Chi tiết giỏ hàng không hợp lệ (thiếu thông tin sách).");
            }
            Sach sach = sachRepository.findById(ctgh.getSach().getMaSach())
                    .orElseThrow(() -> new RuntimeException("Sách với mã " + ctgh.getSach().getMaSach() + " không tồn tại."));

            // === SỬA LỖI 1: Thêm kiểm tra null cho getSoLuong() trước khi so sánh ===
            // Mặc dù là int, nhưng để chắc chắn và có thể làm hài lòng IDE
            Integer soLuongSach = sach.getSoLuong(); // Lấy số lượng sách
            if (soLuongSach == null || soLuongSach < ctgh.getSoLuong()) { // So sánh
                throw new RuntimeException("Sách '" + sach.getTenSach() + "' không đủ số lượng tồn kho (còn " + (soLuongSach != null ? soLuongSach : 0) +", cần "+ ctgh.getSoLuong() +").");
            }
            // === KẾT THÚC SỬA LỖI 1 ===

            // === SỬA LỖI 2: Xóa bỏ kiểm tra getGiaBan() == null ===
            // Không cần kiểm tra null cho kiểu double nguyên thủy
            // === KẾT THÚC SỬA LỖI 2 ===

            // === SỬA LỖI 3 (Tự hết sau khi sửa lỗi 2) và Thêm kiểm tra null cho giá bán ===
            Double giaBanSach = sach.getGiaBan(); // Lấy giá bán
            if (giaBanSach == null) { // Kiểm tra null cho Double (nếu là Double) hoặc kiểm tra <= 0 nếu là double và giá 0 là không hợp lệ
                throw new RuntimeException("Sách '" + sach.getTenSach() + "' chưa có giá bán hợp lệ.");
            }
            tongTienSanPham += giaBanSach * ctgh.getSoLuong(); // Phép tính giờ sẽ ổn
            sachTrongDonHang.add(sach);
        }

        // 4. Lấy hình thức giao hàng và thanh toán (giữ nguyên)
        HinhThucGiaoHang htgh = hinhThucGiaoHangRepository.findById(checkoutRequestDto.getMaHinhThucGiaoHang())
                .orElseThrow(() -> new RuntimeException("Hình thức giao hàng không hợp lệ."));
        HinhThucThanhToan httt = hinhThucThanhToanRepository.findById(checkoutRequestDto.getMaHinhThucThanhToan())
                .orElseThrow(() -> new RuntimeException("Hình thức thanh toán không hợp lệ."));

        // 5. Tạo Đơn Hàng (giữ nguyên, dùng getChiPhiGiaoHang() cho httt như đã thống nhất)
        DonHang donHang = new DonHang();
        donHang.setNguoiDung(nguoiDung);
        donHang.setNgayTao(new Date(System.currentTimeMillis()));
        donHang.setDiaChiMuaHang(checkoutRequestDto.getDiaChiMuaHang());
        donHang.setDiaChiNhanHang(checkoutRequestDto.getDiaChiNhanHang());
        donHang.setHinhThucGiaoHang(htgh);
        donHang.setHinhThucThanhToan(httt);
        donHang.setTongTienSanPham(tongTienSanPham);
        donHang.setChiPhiGiaoHang(htgh.getChiPhiGiaoHang());
        donHang.setChiPhiThanhToan(httt.getChiPhiGiaoHang()); // Giữ nguyên vì bạn không sửa tên biến Entity
        donHang.setTongTien(tongTienSanPham + donHang.getChiPhiGiaoHang() + donHang.getChiPhiThanhToan());
        donHang.setTrangThai("Chờ xử lý");

        // 6. Lưu đơn hàng (giữ nguyên)
        DonHang savedDonHang = donHangRepository.save(donHang);

        // 7. Tạo chi tiết đơn hàng và cập nhật tồn kho (giữ nguyên)
        List<ChiTietDonHang> listChiTietDonHang = new ArrayList<>();
        for (int i = 0; i < chiTietGioHangs.size(); i++) {
            ChiTietGioHang ctgh = chiTietGioHangs.get(i);
            Sach sach = sachTrongDonHang.get(i);
            ChiTietDonHang ctdh = new ChiTietDonHang();
            ctdh.setDonHang(savedDonHang);
            ctdh.setSach(sach);
            ctdh.setSoLuong(ctgh.getSoLuong());
            ctdh.setGiaBan(sach.getGiaBan()); // Nên lấy giá bán đã kiểm tra null ở trên
            listChiTietDonHang.add(ctdh);
            sach.setSoLuong(sach.getSoLuong() - ctgh.getSoLuong()); // Giờ soLuongSach là Integer, cần xử lý unboxing nếu cần hoặc giữ Integer
        }
        chiTietDonHangRepository.saveAll(listChiTietDonHang);
        sachRepository.saveAll(sachTrongDonHang);
        savedDonHang.setDanhSachChiTietDonHang(listChiTietDonHang);


        // 8. Xóa Giỏ Hàng (giữ nguyên)
        chiTietGioHangRepository.deleteAll(chiTietGioHangs);
        gioHang.getDanhSachChiTietGioHang().clear();
        gioHangRepository.save(gioHang);

        // Trả về DTO (giữ nguyên)
        return convertToDonHangDTO(savedDonHang);
    }


    // Giữ nguyên hàm convertToDonHangDTO
    private DonHangDTO convertToDonHangDTO(DonHang donHang) {
        // ... (code như lần trước) ...
        if (donHang == null) {
            return null;
        }
        DonHangDTO dto = new DonHangDTO();
        dto.setMaDonHang(donHang.getMaDonHang());
        dto.setNgayTao(donHang.getNgayTao());
        dto.setTongTien(donHang.getTongTien());
        dto.setTrangThai(donHang.getTrangThai());

        if (donHang.getDanhSachChiTietDonHang() != null) {
            List<ChiTietDonHangDTO> chiTietDTOs = new ArrayList<>();
            for (ChiTietDonHang ctdh : donHang.getDanhSachChiTietDonHang()) {
                if (ctdh != null && ctdh.getSach() != null) {
                    ChiTietDonHangDTO ctDTO = new ChiTietDonHangDTO();
                    ctDTO.setMaSach(ctdh.getSach().getMaSach());
                    ctDTO.setTenSach(ctdh.getSach().getTenSach());
                    ctDTO.setSoLuong(ctdh.getSoLuong());
                    ctDTO.setGiaBan(ctdh.getGiaBan());
                    chiTietDTOs.add(ctDTO);
                } else {
                    System.err.println("Cảnh báo: ChiTietDonHang hoặc Sach liên kết bị null khi chuyển đổi DTO cho đơn hàng: " + donHang.getMaDonHang());
                }
            }
            dto.setDanhSachChiTietDonHang(chiTietDTOs);
        } else {
            dto.setDanhSachChiTietDonHang(new ArrayList<>());
            System.err.println("Cảnh báo: danhSachChiTietDonHang bị null khi chuyển đổi DTO cho đơn hàng: " + donHang.getMaDonHang());
        }
        return dto;
    }

}