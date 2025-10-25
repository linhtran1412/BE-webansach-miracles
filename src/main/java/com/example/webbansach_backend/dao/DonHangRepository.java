//package com.example.wedbansach_bakend1.dao;
//
//import com.example.wedbansach_bakend1.entity.DonHang;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//
//import java.sql.Date;
//import java.util.List;
//
//@RepositoryRestResource(path="don-hang")
//public interface DonHangRepository extends JpaRepository<DonHang, Long> {
//
//    // Tổng doanh thu trong khoảng ngày
//    @Query("SELECT COALESCE(SUM(d.tongTien), 0) " +
//            "FROM DonHang d " +
//            "WHERE d.ngayTao BETWEEN :from AND :to")
//    Double sumRevenue(@Param("from") Date from, @Param("to") Date to);
//
//    // Tổng số đơn trong khoảng ngày
//    @Query("SELECT COUNT(d) FROM DonHang d WHERE d.ngayTao BETWEEN :from AND :to")
//    Long countOrders(@Param("from") Date from, @Param("to") Date to);
//
//    // Đếm đơn theo trạng thái
//    @Query("SELECT d.trangThai, COUNT(d) " +
//            "FROM DonHang d " +
//            "WHERE d.ngayTao BETWEEN :from AND :to " +
//            "GROUP BY d.trangThai")
//    List<Object[]> countByStatus(@Param("from") Date from, @Param("to") Date to);
//
//    // Doanh thu theo ngày (vì ngayTao của bạn là DATE, group thẳng theo ngayTao)
//    @Query("SELECT d.ngayTao, COALESCE(SUM(d.tongTien), 0) " +
//            "FROM DonHang d " +
//            "WHERE d.ngayTao BETWEEN :from AND :to " +
//            "GROUP BY d.ngayTao " +
//            "ORDER BY d.ngayTao")
//    List<Object[]> revenueByDay(@Param("from") Date from, @Param("to") Date to);
//}

package com.example.webbansach_backend.dao;

import com.example.webbansach_backend.entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// import java.sql.Date; // <<< BỎ IMPORT NÀY
import java.time.LocalDate; // <<< THÊM IMPORT NÀY
import java.util.List;

@RepositoryRestResource(path="don-hang")
// <<< KIỂM TRA LẠI KIỂU ID CỦA DonHang TRONG ENTITY >>>
// Nếu là int thì dùng Integer, nếu là long thì dùng Long
public interface DonHangRepository extends JpaRepository<DonHang, Integer> { // Giả sử ID là Integer (maDonHang là int)

    // === THAY ĐỔI KIỂU THAM SỐ TRONG CÁC QUERY ===

    // Tổng doanh thu trong khoảng ngày
    @Query("SELECT COALESCE(SUM(d.tongTien), 0.0) " + // Thêm .0 để chắc chắn là Double
            "FROM DonHang d " +
            "WHERE d.ngayTao BETWEEN :from AND :to") // Giả sử cột ngayTao trong DB là DATE hoặc DATETIME
    Double sumRevenue(@Param("from") LocalDate from, @Param("to") LocalDate to); // Đổi thành LocalDate

    // Tổng số đơn trong khoảng ngày
    @Query("SELECT COUNT(d) FROM DonHang d WHERE d.ngayTao BETWEEN :from AND :to")
    Long countOrders(@Param("from") LocalDate from, @Param("to") LocalDate to); // Đổi thành LocalDate

    // Đếm đơn theo trạng thái
    @Query("SELECT d.trangThai, COUNT(d) " +
            "FROM DonHang d " +
            "WHERE d.ngayTao BETWEEN :from AND :to " +
            "GROUP BY d.trangThai")
    List<Object[]> countByStatus(@Param("from") LocalDate from, @Param("to") LocalDate to); // Đổi thành LocalDate

    // Doanh thu theo ngày
    // Giả sử ngayTao là kiểu DATE hoặc DATETIME trong DB
    // Nếu là DATETIME, có thể cần CAST(d.ngayTao as DATE) hoặc dùng hàm date() tùy DB
    @Query("SELECT d.ngayTao, COALESCE(SUM(d.tongTien), 0.0) " + // Thêm .0
            "FROM DonHang d " +
            "WHERE d.ngayTao BETWEEN :from AND :to " +
            "GROUP BY d.ngayTao " + // Group theo ngày (cần đảm bảo kiểu dữ liệu DB hỗ trợ group theo date)
            "ORDER BY d.ngayTao")
    List<Object[]> revenueByDay(@Param("from") LocalDate from, @Param("to") LocalDate to); // Đổi thành LocalDate

    // ====== THÊM MỚI: Lấy đơn của 1 user, sắp xếp mới → cũ ======
    List<DonHang> findByNguoiDung_MaNguoiDungOrderByNgayTaoDesc(int maNguoiDung);

    // (tuỳ chọn) theo tên đăng nhập
    List<DonHang> findByNguoiDung_TenDangNhapOrderByNgayTaoDesc(String tenDangNhap);
}

