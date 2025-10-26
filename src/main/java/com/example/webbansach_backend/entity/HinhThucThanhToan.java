//package com.example.webbansach_backend.entity;
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.util.List;
//@Entity
//@Data
//@Table(name = "hinh_thuc_thanh_toan")
//public class HinhThucThanhToan {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ma_hinh_thuc_thanh_toan")
//    private int maHinhThucGiaoHang;
//    @Column(name = "ten_hinh_thuc_thanh_toan")
//    private String tenHinhThucGiaoHang;
//    @Column(name = "mo_ta")
//    private String moTa;
//    @Column(name = "chi_phi_thanh_toan")
//    private double chiPhiGiaoHang;
//
//    @OneToMany(mappedBy ="hinhThucThanhToan" ,fetch = FetchType.LAZY, cascade = {
//            CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.DETACH, CascadeType.REFRESH
//    })
//    private List<DonHang> danhSachDonHang;
//}


package com.example.webbansach_backend.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Entity
@Data
@Table(name = "hinh_thuc_thanh_toan")
public class HinhThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_hinh_thuc_thanh_toan")
    private int maHinhThucThanhToan;                 // ✅ đúng tên

    @Column(name = "ten_hinh_thuc_thanh_toan")
    private String tenHinhThucThanhToan;             // ✅ đúng tên

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "chi_phi_thanh_toan")
    private double chiPhiThanhToan;                  // ✅ đúng tên

    @OneToMany(mappedBy = "hinhThucThanhToan", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH
    })
    private List<DonHang> danhSachDonHang;
}
