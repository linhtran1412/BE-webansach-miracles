package com.example.webbansach_backend.dao;

import com.example.webbansach_backend.entity.SachYeuThich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
@RepositoryRestResource(path="sach-yeu-thich")
public interface SachYeuThichRepository extends JpaRepository<SachYeuThich, Integer> {
    Optional<SachYeuThich> findByNguoiDung_MaNguoiDungAndSach_MaSach(int maNguoiDung, int maSach);
    boolean existsByNguoiDung_MaNguoiDungAndSach_MaSach(int maNguoiDung, int maSach);
}
