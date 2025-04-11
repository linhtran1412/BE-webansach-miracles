package com.example.wedbansach_bakend1.dto;

import lombok.Data;
import java.sql.Date;
import java.util.List;

@Data
public class DonHangDTO {
    private int maDonHang;
    private Date ngayTao;
    private double tongTien;
    private String trangThai;
    private List<ChiTietDonHangDTO> danhSachChiTietDonHang;

    // Chỉ chứa các thông tin cơ bản cần thiết cho trang thành công hoặc lịch sử đơn hàng
    // Không cần các đối tượng Entity lồng nhau phức tạp như NguoiDung, HinhThuc...
}