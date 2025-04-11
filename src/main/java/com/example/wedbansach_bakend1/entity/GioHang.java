package com.example.wedbansach_bakend1.entity;

import jakarta.persistence.*;
        import lombok.Data;
import lombok.ToString; // Thêm import này

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "gio_hang")
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_gio_hang")
    private long maGioHang;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nguoi_dung", nullable = false, unique = true) // Mỗi người dùng chỉ có 1 giỏ hàng
    private NguoiDung nguoiDung;

    @OneToMany(mappedBy = "gioHang", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude // Tránh lỗi vòng lặp khi gọi toString()
    private List<ChiTietGioHang> danhSachChiTietGioHang = new ArrayList<>();

    // Các phương thức tiện ích (nếu cần)
    public void addChiTietGioHang(ChiTietGioHang chiTiet) {
        danhSachChiTietGioHang.add(chiTiet);
        chiTiet.setGioHang(this);
    }

    public void removeChiTietGioHang(ChiTietGioHang chiTiet) {
        danhSachChiTietGioHang.remove(chiTiet);
        chiTiet.setGioHang(null);
    }
}
