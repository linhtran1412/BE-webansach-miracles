package com.example.webbansach_backend.entity;

import jakarta.persistence.*;
        import lombok.Data;

@Entity
@Data
@Table(name = "chi_tiet_gio_hang")
public class ChiTietGioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_tiet_gio_hang")
    private long maChiTietGioHang;

    @Column(name = "so_luong")
    private int soLuong;

    @ManyToOne(fetch = FetchType.EAGER) // Load thông tin sách luôn khi lấy chi tiết
    @JoinColumn(name = "ma_sach", nullable = false)
    private Sach sach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_gio_hang", nullable = false)
    private GioHang gioHang;
}