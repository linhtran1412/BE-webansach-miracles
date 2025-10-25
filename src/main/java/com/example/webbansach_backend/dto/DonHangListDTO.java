package com.example.webbansach_backend.dto;

public class DonHangListDTO {
    private int maDonHang;
    private String ngayTao;           // "YYYY-MM-DD"
    private Double tongTien;
    private String diaChiNhanHang;
    private String trangThai;

    public int getMaDonHang() { return maDonHang; }
    public void setMaDonHang(int maDonHang) { this.maDonHang = maDonHang; }

    public String getNgayTao() { return ngayTao; }
    public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }

    public Double getTongTien() { return tongTien; }
    public void setTongTien(Double tongTien) { this.tongTien = tongTien; }

    public String getDiaChiNhanHang() { return diaChiNhanHang; }
    public void setDiaChiNhanHang(String diaChiNhanHang) { this.diaChiNhanHang = diaChiNhanHang; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
