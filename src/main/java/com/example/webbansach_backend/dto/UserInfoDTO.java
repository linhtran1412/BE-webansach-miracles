package com.example.webbansach_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserInfoDTO {
    private int maNguoiDung;
    private String tenDangNhap;
    private String email;
    private String hoDem;
    private String ten;
    private List<String> roles; // Quan trọng: danh sách tên quyền
}


