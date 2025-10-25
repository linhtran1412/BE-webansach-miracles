package com.example.webbansach_backend.dto;

import lombok.Data;

@Data // Dùng Lombok cho nhanh
public class CheckoutRequestDTO {
    private String diaChiMuaHang;
    private String diaChiNhanHang;
    private int maHinhThucGiaoHang;
    private int maHinhThucThanhToan;
    // Không cần gửi thông tin giỏ hàng, sẽ lấy từ DB dựa trên user đăng nhập
}