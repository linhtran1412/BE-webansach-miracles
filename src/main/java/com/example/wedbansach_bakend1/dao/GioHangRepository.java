package com.example.wedbansach_bakend1.dao;

import com.example.wedbansach_bakend1.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang, Long> {
    // Tìm giỏ hàng theo mã người dùng
    Optional<GioHang> findByNguoiDung_MaNguoiDung(int maNguoiDung);
    // Tìm giỏ hàng theo tên đăng nhập của người dùng
    Optional<GioHang> findByNguoiDung_TenDangNhap(String tenDangNhap);
}

