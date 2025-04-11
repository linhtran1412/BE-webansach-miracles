package com.example.wedbansach_bakend1.dto;

import lombok.Data;

@Data
public class ChiTietDonHangDTO {
    private int maSach;
    private String tenSach; // Thêm tên sách cho dễ hiển thị
    private int soLuong;
    private double giaBan; // Giá tại thời điểm mua

    // Không cần các thông tin khác như entity Sach đầy đủ hay DonHang ở đây
}