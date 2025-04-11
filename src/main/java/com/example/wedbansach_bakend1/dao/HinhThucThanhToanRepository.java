
package com.example.wedbansach_bakend1.dao;

import com.example.wedbansach_bakend1.entity.HinhThucThanhToan; // Import Entity HinhThucThanhToan
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="hinh-thuc-thanh-toan")
// Entity là HinhThucThanhToan
// Kiểu dữ liệu của ID (maHinhThucGiaoHang - đáng lẽ nên là maHinhThucThanhToan) là int, nên dùng Integer
public interface HinhThucThanhToanRepository extends JpaRepository<HinhThucThanhToan, Integer> {
    // Để trống vì JpaRepository đã cung cấp các hàm cơ bản
}