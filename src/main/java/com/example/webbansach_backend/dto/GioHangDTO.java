package com.example.webbansach_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class GioHangDTO {
    private long maGioHang;
    private int maNguoiDung;
    private List<ChiTietGioHangDTO> danhSachChiTietGioHang;
    private double tongTien;
}