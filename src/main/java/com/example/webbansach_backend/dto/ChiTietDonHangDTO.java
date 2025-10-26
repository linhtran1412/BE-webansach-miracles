//package com.example.webbansach_backend.dto;
//
//import lombok.Data;
//
//@Data
//public class
//ChiTietDonHangDTO {
//    private int maSach;
//    private String tenSach; // Thêm tên sách cho dễ hiển thị
//    private int soLuong;
//    private double giaBan; // Giá tại thời điểm mua
//
//    // Không cần các thông tin khác như entity Sach đầy đủ hay DonHang ở đây
//}

package com.example.webbansach_backend.dto;

import lombok.Data;

@Data
public class ChiTietDonHangDTO {
    private int maSach;
    private String tenSach;        // tên sách hiển thị
    private int soLuong;
    private double giaBan;         // giá tại thời điểm mua (đơn giá)
    private double thanhTien;      // = soLuong * giaBan (tính trong Service)
    private String hinhAnhDaiDien; // (tùy chọn) base64/URL ảnh đại diện sách
}
