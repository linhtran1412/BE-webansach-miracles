package com.example.webbansach_backend.Service;

import com.example.webbansach_backend.dao.DonHangRepository;
import com.example.webbansach_backend.dao.NguoiDungRepository;
import com.example.webbansach_backend.dto.DonHangListDTO;
import com.example.webbansach_backend.entity.DonHang;
import com.example.webbansach_backend.entity.NguoiDung;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonHangService {

    @Autowired private DonHangRepository donHangRepository;
    @Autowired private NguoiDungRepository nguoiDungRepository;

    private NguoiDung getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new RuntimeException("Người dùng chưa đăng nhập.");
        }
        NguoiDung u = nguoiDungRepository.findByTenDangNhap(auth.getName());
        if (u == null) throw new RuntimeException("Người dùng không tồn tại.");
        return u;
    }

    @Transactional
    public List<DonHangListDTO> getMyOrders() {
        int maNguoiDung = getCurrentUser().getMaNguoiDung();
        List<DonHang> list = donHangRepository
                .findByNguoiDung_MaNguoiDungOrderByNgayTaoDesc(maNguoiDung);

        return list.stream().map(dh -> {
            DonHangListDTO dto = new DonHangListDTO();
            dto.setMaDonHang(dh.getMaDonHang());
            // java.sql.Date -> String YYYY-MM-DD
            dto.setNgayTao(dh.getNgayTao() != null ? dh.getNgayTao().toLocalDate().toString() : null);
            dto.setTongTien(dh.getTongTien());
            dto.setDiaChiNhanHang(dh.getDiaChiNhanHang());
            dto.setTrangThai(dh.getTrangThai());
            return dto;
        }).collect(Collectors.toList());
    }
}
