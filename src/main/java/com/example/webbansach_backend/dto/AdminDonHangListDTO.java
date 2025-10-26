// src/main/java/.../dto/AdminDonHangListDTO.java
package com.example.webbansach_backend.dto;

import com.example.webbansach_backend.entity.DonHang;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDonHangListDTO {
    private Integer maDonHang;
    private String  ngayTao;          // format chuỗi cho an toàn
    private String  username;         // hiển thị người đặt
    private String  diaChiNhanHang;
    private Double  tongTien;         // <-- Double cho khớp getTongTien(): double
    private String  trangThai;

    public static AdminDonHangListDTO fromEntity(DonHang d) {
        // Lấy username: ưu tiên email, nếu không có thì lấy tên đăng nhập, không có nữa thì "N/A"
        String user = "N/A";
        if (d.getNguoiDung() != null) {
            if (d.getNguoiDung().getEmail() != null) {
                user = d.getNguoiDung().getEmail();
            } else if (/* nếu có getter tên đăng nhập */ false) {
                // ví dụ: user = d.getNguoiDung().getTenDangNhap();
            }
        }

        return new AdminDonHangListDTO(
                d.getMaDonHang(),
                (d.getNgayTao() != null ? d.getNgayTao().toString() : null),
                user,
                d.getDiaChiNhanHang(),
                d.getTongTien(),           // <-- Double (tự boxing từ double)
                d.getTrangThai()
        );
    }
}
