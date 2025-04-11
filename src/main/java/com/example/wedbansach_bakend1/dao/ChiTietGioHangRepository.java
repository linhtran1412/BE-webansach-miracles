package com.example.wedbansach_bakend1.dao;

import com.example.wedbansach_bakend1.entity.ChiTietGioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, Long> {
    // Tìm chi tiết giỏ hàng cụ thể theo mã giỏ hàng và mã sách
    Optional<ChiTietGioHang> findByGioHang_MaGioHangAndSach_MaSach(long maGioHang, int maSach);
}