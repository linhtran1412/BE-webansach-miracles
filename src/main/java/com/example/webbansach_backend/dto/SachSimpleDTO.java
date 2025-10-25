package com.example.webbansach_backend.dto;

import lombok.Data;

@Data
public class SachSimpleDTO {
    private int maSach;
    private String tenSach;
    private Double giaBan; // Dùng Double để có thể null
    private String hinhAnhDaiDien; // Lưu URL hoặc Base64 của ảnh đầu tiên (icon)

}
