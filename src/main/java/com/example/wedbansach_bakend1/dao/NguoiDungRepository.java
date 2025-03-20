package com.example.wedbansach_bakend1.dao;

import com.example.wedbansach_bakend1.entity.DonHang;
import com.example.wedbansach_bakend1.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path="nguoi-dung")
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
   public boolean existsByTenDangNhap(String tenDangNhap);

   public boolean existsByEmail(String email);

   public NguoiDung findByTenDangNhap(String tenDangNhap);

   public NguoiDung findByEmail(String email);
}
