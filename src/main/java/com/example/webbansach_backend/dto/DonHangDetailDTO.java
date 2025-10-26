package com.example.webbansach_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class DonHangDetailDTO {
    private int maDonHang;

    // Dùng String cho an toàn khi serialize (FE hiển thị dễ):
    // ISO/hoặc yyyy-MM-dd tùy bạn format ở Service
    private String ngayTao;

    // Thông tin địa chỉ
    private String diaChiMuaHang;
    private String diaChiNhanHang;

    // Hình thức
    private String hinhThucThanhToan; // tên
    private String hinhThucGiaoHang;  // tên

    // Tiền/Phí
    private double tongTienSanPham;
    private double chiPhiGiaoHang;
    private double chiPhiThanhToan;
    private double tongTien;          // tổng cuối

    // Trạng thái đơn
    private String trangThai;

    // Danh sách dòng sản phẩm
    private List<ChiTietDonHangDTO> chiTiet;
}
