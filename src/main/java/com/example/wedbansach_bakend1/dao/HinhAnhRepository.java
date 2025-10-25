package com.example.wedbansach_bakend1.dao;

// import com.example.wedbansach_bakend1.entity.DonHang; // Import này không cần thiết ở đây
import com.example.wedbansach_bakend1.entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
// import org.springframework.stereotype.Repository; // Annotation này không bắt buộc khi dùng JpaRepository

import java.util.List; // <<< THÊM IMPORT NÀY

@RepositoryRestResource(path="hinh-anh") // Giữ nguyên để có API GET /hinh-anh nếu cần
public interface HinhAnhRepository extends JpaRepository<HinhAnh, Integer> {

    /**
     * Tìm tất cả các hình ảnh thuộc về một cuốn sách cụ thể.
     * Cần thiết cho SachAdminController để xóa ảnh cũ khi cập nhật.
     * @param maSach ID của sách cần tìm ảnh.
     * @return Danh sách các đối tượng HinhAnh.
     */
    // <<< THÊM PHƯƠNG THỨC NÀY >>>
    List<HinhAnh> findBySach_MaSach(int maSach);

}
